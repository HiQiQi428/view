package org.luncert.view.component;

import java.util.Properties;

import net.sf.json.JSONObject;

public interface ConfigManager {

    /**
     * @param path: path should split with ':' and connot be null or just ':'
     */
    public void setProperty(String path, String value);

    public JSONObject getConfig(String name);

    public String getProperty(String path);

    public Properties getProperties(String path);

}