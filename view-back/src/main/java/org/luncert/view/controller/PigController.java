package org.luncert.view.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import org.luncert.simpleutils.Result;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.service.PigService;
import org.luncert.view.service.UserService;
import org.luncert.view.util.StatusCode;
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
    UserService userService;

    @Autowired
    PigService pigService;

    @GetMapping("addStrain")
    public String addStrain(String sid, String value) {
        if (userService.beValidSid(sid)) return pigService.addStrain(value).toString();
        else return new Result(StatusCode.INVALID_SID).toString();
    }

    @GetMapping("getStrainMap")
    public String getStrainMap() {
        return pigService.getStrainMap().toString();
    }

    /**
     * 如果fatherId和motherId值为-1则表示不输入
     * @throws ParseException
     */
    @PostMapping("addPig")
    public String addPig(@RequestParam String sid,
        @RequestParam String name,
        @RequestParam int strain,
        @RequestParam boolean beMale,
        @RequestParam String status,
        @RequestParam String birthdate,
        @RequestParam(name = "image") MultipartFile file) throws ParseException {

        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) {
            result = pigService.addPig(userId, name,strain, beMale, Pig.statusValueOf(status), birthdate, file);
        }
        else result = new Result(StatusCode.INVALID_SID);
        return result.toString();
    }

    @PostMapping("updatePig")
    public String updatePig(@RequestParam String sid,
        @RequestParam Long pigId,
        @RequestParam String name,
        @RequestParam int strain,
        @RequestParam boolean beMale,
        @RequestParam String status,
        @RequestParam String birthdate) {

        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) {
            result = pigService.updatePig(userId, pigId, name, strain, beMale, Pig.statusValueOf(status), birthdate);
        }
        else result = new Result(StatusCode.INVALID_SID);
        return result.toString();
    }

    @GetMapping("fetchAllPigs")
    public String fetchAllPigs(@RequestParam String sid) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.fetchAllPigs(userId);
        else result = new Result(StatusCode.INVALID_SID);
        return result.toString();
    }

    @GetMapping("queryById")
    public String queryById(@RequestParam String sid, @RequestParam Long pigId) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.queryById(userId, pigId);
        else result = new Result(StatusCode.INVALID_SID);
        return result.toString();
    }

    @GetMapping("deleteById")
    public String deleteById(@RequestParam String sid, @RequestParam Long pigId) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.deleteById(userId, pigId);
        else result = new Result(StatusCode.INVALID_SID);
        return result.toString();
    }

    @PostMapping("record/addRecord")
    public String addRecord(@RequestParam("image") MultipartFile multipartFile,
        @RequestParam String sid,
        @RequestParam Long pigId,
        @RequestParam float weight,
        @RequestParam String description) {

        if (userService.beValidSid(sid)) return pigService.addRecord(multipartFile, pigId, weight, description).toString();
        else return new Result(StatusCode.INVALID_SID).toString();
    }

	@GetMapping("record/fetchAllRecords")
	public PageInfo<Record> fetchAllRecords(
		@RequestParam String sid,
		@RequestParam int pageSize,
		@RequestParam int pageNum,
		@RequestParam Long pigId) {
        if (userService.beValidSid(sid)) return pigService.fetchAllRecords(pageSize, pageNum, pigId);
        else return null;
    }
    
	@GetMapping("record/fetchLastWeekRecords")
    public PageInfo<Record> fetchLastWeekRecords(
		@RequestParam String sid,
		@RequestParam int pageSize,
		@RequestParam int pageNum,
		@RequestParam Long pigId) {
        if (userService.beValidSid(sid)) return pigService.fetchLastWeekRecords(pageSize, pageNum, pigId);
        else return null;
    }
    
	@GetMapping("record/fetchLast3WeekRecords")
    public PageInfo<Record> fetchLast3WeekRecords(
		@RequestParam String sid,
		@RequestParam int pageSize,
		@RequestParam int pageNum,
		@RequestParam Long pigId) {
        if (userService.beValidSid(sid)) return pigService.fetchLast3WeekRecords(pageSize, pageNum, pigId);
        else return null;
    }

    @GetMapping("loadImage")
    public String loadImage(String sid, String picName, final HttpServletResponse response) {
        if (userService.beValidSid(sid)) {
            Result result = pigService.loadImage(picName, response);
            if (result != null) return result.toString();
            else return null;
        }
        else return new Result(StatusCode.INVALID_SID).toString();
    }

}