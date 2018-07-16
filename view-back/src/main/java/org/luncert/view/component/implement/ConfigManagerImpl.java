package org.luncert.view.component.implement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.luncert.view.component.ConfigManager;
import org.luncert.view.util.IOHelper;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

@Component
public class ConfigManagerImpl implements ConfigManager {

    private String configPath;

    private JSONObject configs;

    public ConfigManagerImpl() throws IOException {
        File file = Paths.get(System.getProperty("user.dir"), "config.json").toFile();
        if (!file.exists()) {
            file.createNewFile();
            configs = new JSONObject();
        }
        else {
            configs = JSONObject.fromObject(IOHelper.read(file));
        }
        configPath = file.getAbsolutePath();
    }

    // write the configuration back to the file
    private void saveChanges() {
        try {
            PrintWriter pw = new PrintWriter(configPath);
            pw.append(configs.toString()).flush();
            pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
                saveChanges();
                return;
            }
            else {
                if (tmp.has(name)) tmp = tmp.getJSONObject(name);
                else {
                    // 由于JSONObject不能存放空的JSONObject，所以只能倒着来
                    List<String> tokens = new ArrayList<>();
                    while (tokenizer.hasMoreTokens()) tokens.add(tokenizer.nextToken());
                    Object v = value;
                    for (int i = tokens.size() - 1; i >= 0; i--) {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put(tokens.get(i), v);
                        v = jsonObj;
                    }
                    tmp.put(name, v);
                    saveChanges();
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
    
}