package org.luncert.view.service;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.util.Result;
import org.springframework.web.multipart.MultipartFile;

public interface PigService {

    /**
     * 获取品系id与品系描述的对照表
     */
    Result getStrainMap();

    /**
     * 登记新猪
     */
    Result addPig(String userId, String name, boolean beMale, Date birthdate, int strain, String health, String eatingHabits, String appetite, Long fatherId, Long motherId);

    Result fetchAllPigs(String userId);

    Result queryById(String userId, Long pigId);

    Result queryByName(String userId, String name);

    Result queryByStrain(String userId, int strain);

    /**
     * 删除
     */
    Result deleteById(String userId, Long id);

    Result addRecord(MultipartFile file, Long pigId, String description);

    void readImage(String picName, HttpServletResponse response) throws IOException;

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