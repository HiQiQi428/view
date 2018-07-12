package org.luncert.view.service;

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
public class TestUserService {

    Mullog mullog = new Mullog(this);

    @Autowired
    private UserService userService;

    @Test
    public void testValiate() {
        mullog.info(userService.validate("033o8zt320cYmN08k4s32MuVt32o8ztj"));
    }

}