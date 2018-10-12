package org.luncert.view.datasource.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Enclosure;

@Mapper
public interface EnclosureMapper {

    @Insert("insert into Enclosure(userId, value) values(#{ e.userId }, #{ e.value })")
    void add(@Param("e") Enclosure enclosure);

    @Select("select value from Enclosure where userId = #{ userId }")
    List<Enclosure> query(@Param("userId") String userId);

}