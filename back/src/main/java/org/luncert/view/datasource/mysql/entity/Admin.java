package org.luncert.view.datasource.mysql.entity;

import lombok.Data;

@Data
public class Admin {

    int id;
    
    String account;

    /**
     * 密码 hash 值
     */
    String password;

    public Admin() {}

    public Admin(String account, String password) {
        this.account = account;
        this.password = password;
    }

}