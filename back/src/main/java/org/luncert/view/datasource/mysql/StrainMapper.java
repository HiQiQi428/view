package org.luncert.view.datasource.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Strain;

@Mapper
public interface StrainMapper {

    @Insert("insert into Strain(value) values(#{0})")
    void addStrain(String value);

    @Delete("delete from Strain where id=#{0}")
    void deleteStrain(int id);

    @Select("select * from Strain")
    List<Strain> fetchAll();
    
}