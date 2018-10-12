package org.luncert.view.service;

import org.luncert.simpleutils.JsonResult;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.luncert.view.datasource.neo4j.entity.Pig.Status;
import org.springframework.web.multipart.MultipartFile;

public interface PigService {

    JsonResult queryEnclosure(WxUser wxUser);

    JsonResult addStrain(String value);

    /**
     * 根据 id 删除指定的种类
     */
    JsonResult deleteStrain(int id);

    /**
     * 获取品系id与品系描述的对照表
     */
    JsonResult getStrainMap();

    /**
     * 检查 strain 是否合法
     * 保存图片，并生成唯一的 picName
     * 存储 pig 到数据库，并与 wxUser 建立关系
     * @param wxUser 登记者
     * @param name 昵称
     * @param strain 品种
     * @param beMale 是否为雄性
     * @param status 身体状态
     * @param birthdate 出生日期
     * @param file 照片
     */
    JsonResult addPig(WxUser wxUser, String name, int strain, boolean beMale, String enclosure, Status status, String birthdate, MultipartFile file);

    /**
     * 管理员接口
     */
    JsonResult addPig(String userId, String name, int strain, boolean beMale, String enclosure, Status status, String birthdate, MultipartFile file);
    
    /**
     * 检查 strain 是否合法
     * 获取 pig 并更新属性
     */
    JsonResult updatePig(long pigId, String name, int strain, boolean beMale, String enclosure, Status status, String birthdate);

    /**
     * 通过 WxUserRepository 查询 wxUser 登记的所有猪
     */
    JsonResult fetchAllPigs(WxUser wxUser);
    
    /**
     * 管理员接口
     */
    JsonResult fetchAllPigs(String userId);

    /**
     * 删除 neo4j 数据（包括 pig 与其登记者间的关系）、所有图片及 mysql 中的生长记录
     * @param wxUser 登记者
     * @param pigId 猪 id
     */
    JsonResult deleteById(WxUser wxUser, long pigId);

    /**
     * 管理员接口
     */
    JsonResult deleteById(String userId, long pigId);
    
}