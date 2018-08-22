package org.luncert.view.service.implement;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.luncert.mullog.Mullog;
import org.luncert.simpleutils.CipherHelper;
import org.luncert.simpleutils.Http;
import org.luncert.view.component.Session;
import org.luncert.view.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {

    Mullog mullog = new Mullog("UserService");

    // 进行sid到userId的映射
    Map<String, Session> mapper = new HashMap<>();

    @Value("${wx-api}")
    String wxApi;

    @Override
    public String validate(String code) {
        // 后门：用于免登录下载图片
        if (code.equals("MobileAI403-view")) {
            String sid = CipherHelper.hashcode(code);
            Session session = new Session();
            session.setAttribute("userId", code);
            mapper.put(sid, session);
            mullog.debug("user vertification successed:", sid, code);
            return sid;
        }
        // 通过微信服务器验证
        try {
            URL url = new URL(MessageFormat.format(wxApi, code));
            String rep = Http.get(url);
            JSONObject json = JSONObject.fromObject(rep);
            mullog.debug(code, rep);
            if (json.has("errcode")) return rep;
            else {
                String userId = json.getString("openid");
                String sid = CipherHelper.hashcode(userId);
                Session session = new Session();
                session.setAttribute("userId", userId);
                session.setAttribute("sessionKey", json.getString("session_key"));
                mapper.put(sid, session);
                mullog.debug("user vertification successed:", sid, userId);
                return sid;
            }
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public String getUserId(String sid) {
        Session session = mapper.get(sid);
        if (session == null) return null;
        else if (session.isEmpired()) {
            mapper.remove(sid);
            return null;
        }
        else return session.getString("userId");
    }

    @Override
    public boolean beValidSid(String sid) {
        Session session = mapper.get(sid);
        if (session == null) return false;
        else if (session.isEmpired()) {
            mapper.remove(sid);
            return false;
        }
        else return true;
    }
    
}