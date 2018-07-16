package org.luncert.view.service.implement;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.luncert.view.component.ConfigManager;
import org.luncert.view.component.Session;
import org.luncert.view.service.UserService;
import org.luncert.view.util.CipherHelper;
import org.luncert.view.util.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {

    // 进行sid到userId的映射
    Map<String, Session> mapper = new HashMap<>();

    @Autowired
    ConfigManager configManager;

    @Override
    public String validate(String code) {
        try {
            URL url = new URL(MessageFormat.format(configManager.getProperty("userService:wx-api"), code));
            String rep = Request.get(url);
            JSONObject json = JSONObject.fromObject(rep);
            
            if (json.has("errcode")) return rep;
            else {
                String userId = json.getString("openid");
                String sid = CipherHelper.getUUID(16);
                Session session = new Session();
                session.setAttribute("userId", userId);
                session.setAttribute("sessionKey", json.getString("session_key"));
                mapper.put(sid, session);
                return null;
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
        else return session.getString("usedId");
    }
    
}