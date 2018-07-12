package org.luncert.view.service.implement;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.luncert.view.component.ConfigManager;
import org.luncert.view.service.UserService;
import org.luncert.view.util.CipherHelper;
import org.luncert.view.util.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class UserServiceImpl implements UserService {

    private class Session {
        static final int TTL = 300000; // 5min
        String userId;
        String sessionKey; // 用于加密传输
        Long lastAccess;
        Session(String userId, String sessionKey) {
            this.userId = userId;
            this.sessionKey = sessionKey;
            lastAccess = System.currentTimeMillis();
        }
        boolean beEmpired() {
            long now = System.currentTimeMillis();
            if (now > TTL + lastAccess) return true;
            else {
                lastAccess = now;
                return false;
            }
        }
    }

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
                String sessionKey = json.getString("session_key");
                String sid = CipherHelper.getUUID(16);
                mapper.put(sid, new Session(userId, sessionKey));
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
        else if (session.beEmpired()) {
            mapper.remove(sid);
            return null;
        }
        else return session.userId;
    }
    
}