package org.luncert.view.datasource.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Strain;

@Mapper
public interface StrainMapper {

    @Insert("insert into Strain(value) values(#{value})")
    void addStrain(@Param("value") String value);

    @Select("select * from Strain")
    List<Strain> fetchAll();
    
}