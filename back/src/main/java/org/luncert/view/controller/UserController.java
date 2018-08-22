package org.luncert.view.controller;

import org.luncert.simpleutils.JsonResult;
import org.luncert.view.service.UserService;
import org.luncert.view.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    // public String 

    @GetMapping("validate")
    public String validate(String code) {
        if (userService.validate(code))
            return JsonResult.build(StatusCode.OK);
        else
            return JsonResult.build(StatusCode.VALIDATE_FAILED);
    }

    @GetMapping("validateAdmin")
    public String validateAdmin(String account, String password) {
        if (userService.validateAdmin(account, password))
            return JsonResult.build(StatusCode.OK);
        else
            return JsonResult.build(StatusCode.VALIDATE_FAILED);
    }

    @GetMapping("registerAdmin")
    public String registerAdmin(String account, String password) {
        if (userService.registerAdmin(account, password))
            return JsonResult.build(StatusCode.OK);
        else
            return JsonResult.build(StatusCode.REGISTER_FAILED);
    }
    
}