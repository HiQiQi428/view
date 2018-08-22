package org.luncert.view.service;

import com.github.pagehelper.PageInfo;

import org.luncert.simpleutils.JsonResult;
import org.luncert.view.datasource.mysql.entity.Record;
import org.springframework.web.multipart.MultipartFile;

public interface RecordService {

    /**
     * @param file 图片
     * @param pigId 猪 id
     * @param weight 重量，单位kg
     * @param description 描述
     */
    JsonResult addRecord(MultipartFile file, Long pigId, float weight, String description);

    /**
     * 删除指定记录（包括图片）
     * @param recordId
     */
    JsonResult deleteById(int recordId);

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