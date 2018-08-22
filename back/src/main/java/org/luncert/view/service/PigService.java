package org.luncert.view.service;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import org.luncert.simpleutils.Result;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.neo4j.entity.Pig.Status;
import org.springframework.web.multipart.MultipartFile;

public interface PigService {

    Result addStrain(String value);

    /**
     * 获取品系id与品系描述的对照表
     */
    Result getStrainMap();

    /**
     * @param userId 持有者ID
     * @param name 昵称
     * @param strain 品种
     * @param beMale 是否为雄性
     * @param status 身体状态
     * @param birthdate 出生日期
     * @param file 照片
     */
    Result addPig(String userId, String name, int strain, boolean beMale, Status status, String birthdate, MultipartFile file);
    
    Result updatePig(String userId, Long pigId, String name, int strain, boolean beMale, Status status, String birthdate);

    /**
     * 查询用户登记的所有猪
     */
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

    Result addRecord(MultipartFile file, Long pigId, float weight, String description);

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
    
    Result loadImage(String picName, HttpServletResponse response);

}