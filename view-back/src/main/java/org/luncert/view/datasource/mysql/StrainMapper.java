package org.luncert.view.datasource.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Strain;

@Mapper
public interface StrainMapper {

    @Select("select * from Strain")
    List<Strain> fetchAll();
    
}