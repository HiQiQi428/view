package org.luncert.view.service;

import org.luncert.simpleutils.JsonResult;

public interface UserService {
    
    /**
     * @param code: 微信小程序上传的临时code，可用来向微信服务器查询用户唯一标识
     * @return: 返回sid，往后小程序的所有请求都必须带上sid
     */
    boolean validate(String code);

    /**
     * PC 端管理员验证
     */
    boolean validateAdmin(String account, String password);

    /**
     * PC 端管理员注册
     */
    boolean registerAdmin(String account, String password);

    /**
     * 管理员查询所有微信用户
     */
    JsonResult queryAllWxUser();

}