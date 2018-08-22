package org.luncert.view.service;

public interface UserService {
    
    /**
     * @param code: 微信小程序上传的临时code，可用来向微信服务器查询用户唯一标识
     * @return: 返回sid，往后小程序的所有请求都必须带上sid
     */
    boolean validate(String code);

    /**
     * 用于 PC 端管理员验证
     */
    boolean validateAdmin(String account, String password);

    boolean registerAdmin(String account, String password);

}