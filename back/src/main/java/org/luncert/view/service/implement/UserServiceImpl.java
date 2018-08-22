package org.luncert.view.service.implement;

import java.net.URL;
import java.text.MessageFormat;

import org.luncert.simpleutils.CipherHelper;
import org.luncert.simpleutils.Http;
import org.luncert.springauth.AuthManager;
import org.luncert.springauth.Identity;
import org.luncert.view.datasource.mysql.AdminMapper;
import org.luncert.view.datasource.mysql.entity.Admin;
import org.luncert.view.datasource.neo4j.WxUserRepository;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.luncert.view.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    WxUserRepository wxUserRepos;

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
                authManager.grant(Identity.NormalUser, wxUser);
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
            authManager.grant(Identity.Administrator, admin);
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
    
}