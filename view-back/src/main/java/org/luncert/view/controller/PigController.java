package org.luncert.view.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;

import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.service.PigService;
import org.luncert.view.service.UserService;
import org.luncert.view.util.Result;
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
        if (userService.beValidSid(sid)) return pigService.addStrain(value).toJSONString();
        else return new Result(Result.INVALID_SID).toJSONString();
    }

    @GetMapping("getStrainMap")
    public String getStrainMap() {
        return pigService.getStrainMap().toJSONString();
    }

    /**
     * 如果fatherId和motherId值为-1则表示不输入
     * @throws ParseException
     */
    @PostMapping("addPig")
    public String addPig(@RequestParam String sid,
        @RequestParam String name,
        @RequestParam boolean beMale,
        @RequestParam String birthdate,
        @RequestParam int strain,
        @RequestParam String health,
        @RequestParam String eatingHabits,
        @RequestParam String appetite,
        @RequestParam Long fatherId,
        @RequestParam Long motherId,
        @RequestParam(name = "image") MultipartFile file) throws ParseException {

        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) {
            result = pigService.addPig(userId, name, beMale, birthdate, strain, health, eatingHabits, appetite, fatherId, motherId, file);
        }
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @GetMapping("updatePig")
    public String updatePig(@RequestParam String sid,
        @RequestParam Long pigId,
        @RequestParam String name,
        @RequestParam boolean beMale,
        @RequestParam String birthdate,
        @RequestParam int strain,
        @RequestParam String health,
        @RequestParam String eatingHabits,
        @RequestParam String appetite,
        @RequestParam Long fatherId,
        @RequestParam Long motherId) {

        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) {
            result = pigService.updatePig(userId, pigId, name, beMale, birthdate, strain, health, eatingHabits, appetite, fatherId, motherId);
        }
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @GetMapping("fetchAllPigs")
    public String fetchAllPigs(@RequestParam String sid) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.fetchAllPigs(userId);
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @GetMapping("queryById")
    public String queryById(@RequestParam String sid, @RequestParam Long pigId) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.queryById(userId, pigId);
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @GetMapping("queryByName")
    public String queryByName(@RequestParam String sid, @RequestParam String name) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.queryByName(userId, name);
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @GetMapping("queryByStrain")
    public String queryByStrain(@RequestParam String sid, @RequestParam int strain) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.queryByStrain(userId, strain);
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @GetMapping("deleteById")
    public String deleteById(@RequestParam String sid, @RequestParam Long pigId) {
        Result result;
        String userId = userService.getUserId(sid);
        if (userId != null) result = pigService.deleteById(userId, pigId);
        else result = new Result(Result.INVALID_SID);
        return result.toJSONString();
    }

    @PostMapping("record/addRecord")
    public String addRecord(@RequestParam("image") MultipartFile multipartFile,
        @RequestParam String sid,
        @RequestParam Long pigId,
        @RequestParam String description) {

        if (userService.beValidSid(sid)) return pigService.addRecord(multipartFile, pigId, description).toJSONString();
        else return new Result(Result.INVALID_SID).toJSONString();
    }

    @GetMapping("record/readImage")
    public void readImage(String picName, HttpServletResponse response) throws IOException {
        pigService.readImage(picName, response);
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

}