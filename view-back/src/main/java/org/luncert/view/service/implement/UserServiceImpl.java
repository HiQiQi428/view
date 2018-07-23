package org.luncert.view.service.implement;

import java.util.HashMap;
import java.util.Map;

import org.luncert.mullog.Mullog;
import org.luncert.mullog.annotation.BindAppender;
import org.luncert.simpleutils.CipherHelper;
import org.luncert.springconfigurer.ConfigManager;
import org.luncert.view.component.Session;
import org.luncert.view.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@BindAppender(name = "UserService")
public class UserServiceImpl implements UserService {

    Mullog mullog = new Mullog(this);

    // 进行sid到userId的映射
    Map<String, Session> mapper = new HashMap<>();

    @Autowired
    ConfigManager configManager;

    @Override
    public String validate(String code) {
        String userId = "lun";
        String sid = CipherHelper.hashcode(userId);
        Session session = new Session();
        session.setAttribute("userId", userId);
        mapper.put(sid, session);
        mullog.debug("user vertification successed:", sid, 1);
        return sid;
        /*
        try {
            URL url = new URL(MessageFormat.format(configManager.getProperty("userService:wx-api"), code));
            String rep = Request.get(url);
            JSONObject json = JSONObject.fromObject(rep);
            
            if (json.has("errcode")) return null;
            else {
                String userId = json.getString("openid");
                String sid = CipherHelper.getUUID(16);
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
        */
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