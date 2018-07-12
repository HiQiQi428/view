package org.luncert.view.component.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.luncert.view.component.ConfigManager;
import org.luncert.view.util.IOHelper;

import net.sf.json.JSONObject;

public class ConfigManagerImpl implements ConfigManager {

    // private Map<String, Properties> configs;

    private JSONObject configs;

    private void loadConfigFromContent(String content) {
        configs = JSONObject.fromObject(content);
    }

    public ConfigManagerImpl() throws IOException {
        String content = IOHelper.read(ConfigManagerImpl.class.getClassLoader().getResourceAsStream("config.json"));
        loadConfigFromContent(content);
    }

	@Override
	public void setProperty(String path, String value) {
        if (path == null || path.equals(":")) return;

        StringTokenizer tokenizer = new StringTokenizer(path, ":");
        JSONObject tmp = configs;

        while (true) {
            String name = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens()) {
                tmp.put(name, value);
                return;
            }
            else {
                if (tmp.getJSONObject(name) == null) tmp = tmp.getJSONObject(name);
                else {
                    List<String> tokens = new ArrayList<>();
                    while (tokenizer.hasMoreTokens()) tokens.add(tokenizer.nextToken());
                    Object v = value;
                    for (int i = tokens.size() - 1; i >= 0; i--) {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put(tokens.get(i), v);
                        v = jsonObj;
                    }
                    tmp.put(name, v);
                    return;
                }
            }
        }
	}

	@Override
	public JSONObject getConfig(String name) {
		return configs.getJSONObject(name);
	}

	@Override
	public String getProperty(String path) {
        if (path == null || path.equals(":")) return null;

        StringTokenizer tokenizer = new StringTokenizer(path, ":");
        JSONObject tmp = configs;
        while (true) {
            String name = tokenizer.nextToken();
            if (tokenizer.hasMoreTokens()) {
                tmp = tmp.getJSONObject(name);
                if (tmp == null) return null;
            }
            else return tmp.getString(name);
        }
    }

	@Override
	public Properties getProperties(String path) {
        if (path == null || path.equals(":")) return null;

        StringTokenizer tokenizer = new StringTokenizer(path, ":");
        JSONObject tmp = configs;
        while (true) {
            String name = tokenizer.nextToken();
            if (tokenizer.hasMoreTokens()) {
                tmp = tmp.getJSONObject(name);
                if (tmp == null) return null;
            }
            else {
                tmp = tmp.getJSONObject(name);
                if (tmp == null) return null;
                Properties props = new Properties();
                for (Object key : tmp.keySet()) props.put(key, tmp.get(key));
                return props;
            }
        }
	}

	@Override
	public boolean loadConfig(File configFile) {
        try {
            loadConfigFromContent(IOHelper.read(new FileInputStream(configFile)));
            return true;
		} catch (IOException e) {
            e.printStackTrace();
            return false;
		}
	}

}