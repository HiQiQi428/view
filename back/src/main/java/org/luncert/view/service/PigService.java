package org.luncert.view.service;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import org.luncert.simpleutils.JsonResult;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.luncert.view.datasource.neo4j.entity.Pig.Status;
import org.springframework.web.multipart.MultipartFile;

public interface PigService {

    JsonResult addStrain(String value);

    /**
     * 获取品系id与品系描述的对照表
     */
    JsonResult getStrainMap();

    /**
     * 检查 strain 是否合法
     * 保存图片，并生成唯一的 picName
     * 存储 pig 到数据库，并与 wxUser 建立关系
     * @param userId 持有者ID
     * @param name 昵称
     * @param strain 品种
     * @param beMale 是否为雄性
     * @param status 身体状态
     * @param birthdate 出生日期
     * @param file 照片
     */
    JsonResult addPig(WxUser wxUser, String name, int strain, boolean beMale, Status status, String birthdate, MultipartFile file);
    
    /**
     * 检查 strain 是否合法
     * 获取 pig 并更新属性
     */
    JsonResult updatePig(Long pigId, String name, int strain, boolean beMale, Status status, String birthdate);

    /**
     * 通过 WxUserRepository 查询 wxUser 登记的所有猪
     */
	JsonResult fetchAllPigs(WxUser wxUser);

    /**
     * 删除 neo4j 数据（包括 pig 与其登记者间的关系）、所有图片及 mysql 中的生长记录
     * @param wxUser 登记者
     * @param pigId 猪 id
     */
    JsonResult deleteById(WxUser wxUser, Long id);

    JsonResult addRecord(MultipartFile file, Long pigId, float weight, String description);

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
    
    /**
     * 用于直接读图片，操作成功 JsonResult 的 data 为空，否则 为异常信息
     */
    JsonResult loadImage(String picName, HttpServletResponse response);

}