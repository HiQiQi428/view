package org.luncert.view.datasource.mysql;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Record;

@Mapper
public interface RecordMapper {

    @Insert("insert into Records(pigId, description, timestamp, picName) values(#{ pigId }, #{ description }, #{ timestamp }, #{ picName })")
    void addRecord(@Param("pigId") Long pigId, @Param("description") String description, @Param("timestamp") Date timestamp, @Param("picName") String picName);

    @Select("select * from Records where pigId=#{ pigId } and date(timestamp) >= date_sub(curdate(), INTERVAL 7 DAY)")
    List<Record> fetchLastWeek(@Param("pigId") Long pigId);

    @Select("select * from Records where pigId=#{ pigId } and date(timestamp) >= date_sub(curdate(), INTERVAL 21 DAY)")
    List<Record> fetchLast3Week(@Param("pigId") Long pigId);

    @Select("select * from Records where pigId=#{ pigId }")
    List<Record> fetchAll(@Param("pigId") Long pigId);
    
}