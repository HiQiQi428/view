package org.luncert.view.datasource.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.neo4j.entity.Pig;

@Mapper
public interface RecordMapper {

    @Insert("insert into Records(pigId, weight, description, timestamp, picName) values(#{ record.pigId }, #{ record.weight }, #{ record.description }, #{ record.timestamp }, #{ record.picName })")
    void addRecord(@Param("record") Record record);

    @Select("select * from Records where id=#{0}")
    Record queryById(int recordId);

    @Select("select * from Records where pigId=#{0} and date(timestamp) >= date_sub(curdate(), INTERVAL 7 DAY)")
    List<Record> fetchLastWeek(Long pigId);

    @Select("select * from Records where pigId=#{0} and date(timestamp) >= date_sub(curdate(), INTERVAL 21 DAY)")
    List<Record> fetchLast3Week(Long pigId);

    @Select("select * from Records where pigId=#{0} order by timestamp desc")
    List<Record> fetchAll(Long pigId);

    @Select("select picName from Records where pigId=#{0}")
    List<String> fetchPicNameByPigId(long pigId);

    @Select("<script>" +
        "select picName from Records where pigId in " + 
        "<foreach item='pig' index='index' collection='pigs' open='(' separator=',' close=')'>" +
            "#{pig.id}" +
        "</foreach>" +
    "</script>")
    List<String> fetchPicNameByPigs(@Param("pigs") List<Pig> pigs);

    /**
     * 删除所有相关记录
     * @param pigId pig id
     */
    @Delete("delete from Records where pigId=#{0}")
    void deleteByPigId(long pigId);

    @Delete("<script>" +
        "delete from Records where pigId in  " +
        "<foreach item='pig' index='index' collection='pigs' open='(' separator=',' close=')'>" +
            "#{pig.id}" +
        "</foreach>" +
    "</script>")
    void deleteByPigs(@Param("pigs") List<Pig> pigs);

    /**
     * 删除指定记录
     * @param recordId 记录 id
     */
    @Delete("delete from Records where recordId=#{0}")
    void deleteByRecordId(int recordId);
    
}