package org.luncert.view.component;

import java.util.HashMap;
import java.util.Map;

public class Session {

    private static final int TTL = 600000; // 10min

    private long lastAccess;

    private Map<String, Object> props = new HashMap<>();

    public Session() { lastAccess = System.currentTimeMillis(); }

    private void updateLastAccess() {
        this.lastAccess = System.currentTimeMillis();
    }

    public boolean isEmpired() {
        if (System.currentTimeMillis() > lastAccess + TTL) return true;
        else {
            updateLastAccess();
            return false;
        }
    }

    public void setAttribute(String key, Object value) {
        updateLastAccess();
        props.put(key, value);
    }

    public Object getAttribute(String key) {
        updateLastAccess();
        return props.get(key);
    }

    public String getString(String key) {
        Object ret = getAttribute(key);
        if (ret != null && ret instanceof String) return (String)ret;
        else return null;
    }

    public int getInt(String key) {
        Object ret = getAttribute(key);
        if (ret != null) {
            if (ret instanceof Integer) return (int)ret;
            else throw new RuntimeException("value couldn't be cast to Integer");
        }
        else return 0;
    }
    
}