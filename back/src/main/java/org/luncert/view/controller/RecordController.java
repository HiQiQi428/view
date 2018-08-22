package org.luncert.view.controller;

import com.github.pagehelper.PageInfo;

import org.luncert.springauth.annotation.AuthRequired;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.pojo.Role;
import org.luncert.view.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "record", produces={"application/json;charset=UTF-8"})
public class RecordController {

    @Autowired
    RecordService recordService;

    @AuthRequired
    @PostMapping("addRecord")
    public String addRecord(Long pigId, float weight, String description, @RequestParam("image") MultipartFile multipartFile) {
        return recordService.addRecord(multipartFile, pigId, weight, description).toString();
    }

    @AuthRequired(accessLevel = Role.ADMIN)
    @GetMapping("deleteById")
    public String deleteById(int recordId) {
        return recordService.deleteById(recordId).toString();
    }

    @AuthRequired
	@GetMapping("fetchAllRecords")
	public PageInfo<Record> fetchAllRecords(int pageSize, int pageNum, Long pigId) {
        return recordService.fetchAllRecords(pageSize, pageNum, pigId);
    }
    
    @AuthRequired
	@GetMapping("fetchLastWeekRecords")
    public PageInfo<Record> fetchLastWeekRecords(int pageSize, int pageNum, Long pigId) {
        return recordService.fetchLastWeekRecords(pageSize, pageNum, pigId);
    }
    
    @AuthRequired
	@GetMapping("fetchLast3WeekRecords")
    public PageInfo<Record> fetchLast3WeekRecords(int pageSize, int pageNum, Long pigId) {
        return recordService.fetchLast3WeekRecords(pageSize, pageNum, pigId);
    }

}