package org.luncert.view.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.mullog.Mullog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserService {

    Mullog mullog = new Mullog("Test");

    @Autowired
    private UserService userService;

    @Test
    public void testValiate() {
        mullog.info(userService.validate("033wIcYe0F0g5A1YY8We0Eq1Ye0wIcYQ"));
    }

}