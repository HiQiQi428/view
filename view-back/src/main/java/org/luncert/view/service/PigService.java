package org.luncert.view.service;

import com.github.pagehelper.PageInfo;

import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.util.Result;
import org.springframework.web.multipart.MultipartFile;

public interface PigService {

    Result addStrain(String value);

    /**
     * 获取品系id与品系描述的对照表
     */
    Result getStrainMap();

    /**
     * 登记新猪
     */
    Result addPig(String userId, String name, boolean beMale, String birthdate, int strain, String health, String eatingHabits, String appetite, Long fatherId, Long motherId, MultipartFile file);
    
    Result updatePig(String userId, Long pigId, String name, boolean beMale, String birthdate, int strain, String health, String eatingHabits, String appetite, Long fatherId, Long motherId);

    Result fetchAllPigs(String userId);

    Result queryById(String userId, Long pigId);

    @Deprecated
    Result queryByName(String userId, String name);

    @Deprecated
    Result queryByStrain(String userId, int strain);

    /**
     * 删除
     */
    Result deleteById(String userId, Long id);

    Result addRecord(MultipartFile file, Long pigId, String description);

    /**
     * 获得猪的所有生长记录
     */
    PageInfo<Record> fetchAllRecords(int pageSize, int pageNum, Long pigId);

    /**
     * 获得猪的1周内生长记录
     */
    PageInfo<Record> fetchLastWeekRecords(int pageSize, int pageNum, Long pigId);

    /**
     * 获得猪的3周内生长记录
     */
    PageInfo<Record> fetchLast3WeekRecords(int pageSize, int pageNum, Long pigId);
    
}