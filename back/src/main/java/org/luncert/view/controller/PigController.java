package org.luncert.view.controller;

import org.luncert.springauth.annotation.AuthRequired;
import org.luncert.springauth.annotation.AuthUser;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.luncert.view.pojo.Role;
import org.luncert.view.service.PigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "pig", produces={"application/json;charset=UTF-8"})
public class PigController {

    @Autowired
    PigService pigService;

    @AuthRequired
    @GetMapping("queryEnclosure")
    public String queryEnclosure(@AuthUser WxUser wxUser) {
        return pigService.queryEnclosure(wxUser).toString();
    }

    @AuthRequired
    @GetMapping("addStrain")
    public String addStrain(String value) {
        return pigService.addStrain(value).toString();
    }

    @AuthRequired(accessLevel = Role.ADMIN)
    @GetMapping("deleteStrain")
    public String deleteStrain(int id) {
        return pigService.deleteStrain(id).toString();
    }

    @AuthRequired
    @GetMapping("getStrainMap")
    public String getStrainMap() {
        return pigService.getStrainMap().toString();
    }

    @AuthRequired
    @PostMapping("addPig")
    public String addPig(@AuthUser WxUser wxUser, String name, int strain, boolean beMale, String enclosure, String status, String birthdate, @RequestParam(name = "image") MultipartFile file) {
        return pigService.addPig(wxUser, name,strain, beMale, enclosure, Pig.statusValueOf(status), birthdate, file).toString();
    }

    @AuthRequired
    @PostMapping("updatePig")
    public String updatePig(Long pigId, String name, int strain, boolean beMale, String enclosure, String status, String birthdate) {
        return pigService.updatePig(pigId, name, strain, beMale, enclosure, Pig.statusValueOf(status), birthdate).toString();
    }

    @AuthRequired
    @GetMapping("fetchAllPigs")
    public String fetchAllPigs(@AuthUser WxUser wxUser) {
        return pigService.fetchAllPigs(wxUser).toString();
    }

    @AuthRequired
    @GetMapping("deleteById")
    public String deleteById(@AuthUser WxUser wxUser, Long pigId) {
        return pigService.deleteById(wxUser, pigId).toString();
    }

}