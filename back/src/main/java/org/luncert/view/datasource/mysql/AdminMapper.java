package org.luncert.view.datasource.mysql;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Admin;

@Mapper
public interface AdminMapper {

    @Select("SELECT * FROM Admin WHERE account = {#account}")
    public Admin queryByAccount(@Param("account") String account);

    @Insert("INSERT INTO Admin(account, password) VALUES({#admin.account}, {#admin.password})")
    public void addAdmin(@Param("admin") Admin admin);
    
}