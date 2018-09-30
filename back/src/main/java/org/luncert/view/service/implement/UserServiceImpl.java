package org.luncert.view.service.implement;

import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.luncert.simpleutils.CipherHelper;
import org.luncert.simpleutils.Http;
import org.luncert.simpleutils.JsonResult;
import org.luncert.springauth.AuthManager;
import org.luncert.view.datasource.mysql.AdminMapper;
import org.luncert.view.datasource.mysql.RecordMapper;
import org.luncert.view.datasource.mysql.entity.Admin;
import org.luncert.view.datasource.neo4j.WxUserRepository;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.luncert.view.pojo.Role;
import org.luncert.view.pojo.StatusCode;
import org.luncert.view.service.ImageService;
import org.luncert.view.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    RecordMapper recordMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    WxUserRepository wxUserRepos;

    @Autowired
    ImageService imageService;

    @Autowired
    AuthManager authManager;

    @Value("${wx-api}")
    String wxApi;

    /**
     * 通过微信服务器完成验证
     */
    @Override
    public boolean validate(String code) {
        // 通过微信服务器验证
        try {
            URL url = new URL(MessageFormat.format(wxApi, code));
            String rep = Http.get(url);
            JSONObject json = JSONObject.fromObject(rep);
            if (json.has("errcode"))
                return false;
            else {
                String userId = json.getString("openid");
                WxUser wxUser = wxUserRepos.findByUserId(userId);
                if (wxUser == null) {
                    wxUser = new WxUser();
                    wxUser.setUserId(userId);
                    wxUserRepos.save(wxUser);
                }
                authManager.grant(Role.NORMAL, wxUser);
                return true;
            }
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
	public boolean validateAdmin(String account, String password) {
        Admin admin = adminMapper.queryByAccount(account);
        if (admin != null && CipherHelper.hashcode(password).equals(admin.getPassword())) {
            authManager.grant(Role.ADMIN, admin);
            return true;
        }
        else
            return false;
    }
    
    @Override
    public boolean registerAdmin(String account, String password) {
        Admin admin = adminMapper.queryByAccount(account);
        if (admin == null) {
            admin = new Admin(account, CipherHelper.hashcode(password));
            adminMapper.addAdmin(admin);
            return true;
        }
        else
            return false;
    }

	@Override
	public JsonResult queryAllWxUser() {
        // 查询深度 0
        return new JsonResult(StatusCode.OK, null, wxUserRepos.findAll(0));
	}

	@Override
	public JsonResult deleteWxUser(String userId) {
        for (WxUser wxUser : wxUserRepos.findAll(1)) {
            // 删除 neo4j 数据：关系、节点、图片
            List<Pig> pigs = wxUser.getPigs();
            pigs.forEach((pig) -> imageService.delete(pig.getPicName()));
            wxUserRepos.deleteWxUser(wxUser);
            // 删除 mysql 数据：记录、图片
            recordMapper.fetchPicNameByPigs(pigs).forEach((picName) -> imageService.delete(picName));
            recordMapper.deleteByPigs(pigs);
        }

        return new JsonResult(StatusCode.OK);
	}
    
}