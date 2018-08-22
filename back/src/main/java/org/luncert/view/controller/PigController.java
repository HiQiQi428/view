package org.luncert.view.controller;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import org.luncert.simpleutils.JsonResult;
import org.luncert.springauth.annotation.AuthRequired;
import org.luncert.springauth.annotation.AuthUser;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
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
    @GetMapping("addStrain")
    public String addStrain(String value) {
        return pigService.addStrain(value).toString();
    }

    @GetMapping("getStrainMap")
    public String getStrainMap() {
        return pigService.getStrainMap().toString();
    }

    @AuthRequired
    @PostMapping("addPig")
    public String addPig(@AuthUser WxUser wxUser, String name, int strain, boolean beMale, String status, String birthdate, @RequestParam(name = "image") MultipartFile file) {
        return pigService.addPig(wxUser, name,strain, beMale, Pig.statusValueOf(status), birthdate, file).toString();
    }

    @AuthRequired
    @PostMapping("updatePig")
    public String updatePig(Long pigId, String name, int strain, boolean beMale, String status, String birthdate) {
        return pigService.updatePig(pigId, name, strain, beMale, Pig.statusValueOf(status), birthdate).toString();
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

    @AuthRequired
    @PostMapping("record/addRecord")
    public String addRecord(Long pigId, float weight, String description, @RequestParam("image") MultipartFile multipartFile) {
        return pigService.addRecord(multipartFile, pigId, weight, description).toString();
    }

    @AuthRequired
	@GetMapping("record/fetchAllRecords")
	public PageInfo<Record> fetchAllRecords(int pageSize, int pageNum, Long pigId) {
        return pigService.fetchAllRecords(pageSize, pageNum, pigId);
    }
    
    @AuthRequired
	@GetMapping("record/fetchLastWeekRecords")
    public PageInfo<Record> fetchLastWeekRecords(int pageSize, int pageNum, Long pigId) {
        return pigService.fetchLastWeekRecords(pageSize, pageNum, pigId);
    }
    
    @AuthRequired
	@GetMapping("record/fetchLast3WeekRecords")
    public PageInfo<Record> fetchLast3WeekRecords(int pageSize, int pageNum, Long pigId) {
        return pigService.fetchLast3WeekRecords(pageSize, pageNum, pigId);
    }

    @AuthRequired
    @GetMapping("loadImage")
    public String loadImage(String picName, final HttpServletResponse response) {
        JsonResult JsonResult = pigService.loadImage(picName, response);
        if (JsonResult != null)
            return JsonResult.toString();
        else
            return null;
    }

}