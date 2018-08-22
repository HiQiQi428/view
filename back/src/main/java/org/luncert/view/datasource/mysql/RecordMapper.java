package org.luncert.view.datasource.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Record;

@Mapper
public interface RecordMapper {

    @Insert("insert into Records(pigId, weight, description, timestamp, picName) values(#{ record.pigId }, #{ record.weight }, #{ record.description }, #{ record.timestamp }, #{ record.picName })")
    void addRecord(@Param("record") Record record);

    @Select("select * from Records where pigId=#{ pigId } and date(timestamp) >= date_sub(curdate(), INTERVAL 7 DAY)")
    List<Record> fetchLastWeek(@Param("pigId") Long pigId);

    @Select("select * from Records where pigId=#{ pigId } and date(timestamp) >= date_sub(curdate(), INTERVAL 21 DAY)")
    List<Record> fetchLast3Week(@Param("pigId") Long pigId);

    @Select("select * from Records where pigId=#{ pigId } order by timestamp desc")
    List<Record> fetchAll(@Param("pigId") Long pigId);

    @Delete("delete from Records where pigId=#{ pigId }")
    void deleteById(@Param("pigId") Long pigId);
    
}