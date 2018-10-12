package org.luncert.view.controller;

import org.luncert.springauth.annotation.AuthRequired;
import org.luncert.view.pojo.Role;
import org.luncert.view.service.PigService;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * PigController 管理员接口
 */
@RestController
@RequestMapping(value = "pigManage", produces={"application/json;charset=UTF-8"})
public class PigManageController {

    @Autowired
    PigService pigService;

    @AuthRequired(accessLevel = Role.ADMIN)
    @PostMapping("addPig")
    public String addPig(String userId, String name, int strain, boolean beMale, String enclosure, String status, String birthdate, @RequestParam(name = "image") MultipartFile file) {
        return pigService.addPig(userId, name, strain, beMale, enclosure, Pig.statusValueOf(status), birthdate, file).toString();
    }

    @AuthRequired(accessLevel = Role.ADMIN)
    @GetMapping("fetchAllPigs")
    public String fetchAllPigs(String userId) {
        return pigService.fetchAllPigs(userId).toString();
    }

    @AuthRequired(accessLevel = Role.ADMIN)
    @GetMapping("deleteById")
    public String deleteById(String userId, Long pigId) {
        return pigService.deleteById(userId, pigId).toString();
    }

}