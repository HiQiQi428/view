package org.luncert.view.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.mullog.Mullog;
import org.luncert.mullog.annotation.BindAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@BindAppender(name = "Test")
public class TestConfigManager {

    Mullog mullog = new Mullog(this);

    @Autowired
    ConfigManager configManager;

    @Test
    public void testSetProperty() {
        configManager.setProperty("x1:x2:name", "hi");
        mullog.info(configManager.getProperty("x1:x2:name"));
    }

}