package org.luncert.view.service;

public interface UserService {
    
    /**
     * @param code: 微信小程序上传的临时code，可用来向微信服务器查询用户唯一标识
     * @return: 返回sid，往后小程序的所有请求都必须带上sid
     */
    public String validate(String code);

    /**
     * @param sid: 服务器生成的用户临时标识
     * @return: 微信api提供的用户唯一标识
     */
    public String getUserId(String sid);

}