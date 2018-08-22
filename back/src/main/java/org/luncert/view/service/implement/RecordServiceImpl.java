package org.luncert.view.service.implement;

import org.luncert.simpleutils.JsonResult;
import org.luncert.simpleutils.DateHelper;
import org.luncert.view.datasource.mysql.RecordMapper;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.pojo.StatusCode;
import org.luncert.view.service.ImageService;
import org.luncert.view.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    ImageService imageService;

	@Override
	public JsonResult addRecord(MultipartFile file, Long pigId, float weight, String description) {
        if (file.isEmpty()
            || file.getOriginalFilename() == null
            || description.equals("")) {
            return new JsonResult(StatusCode.FIELD_IS_NULL);
        }

        String picName = null;
        try {
			picName = imageService.save(file);
		} catch (IOException e) {
            return new JsonResult(StatusCode.EXCEPTION_OCCUR, null, e);
        }

        recordMapper.addRecord(Record.builder()
            .pigId(pigId)
            .weight(weight)
            .description(description)
            .timestamp(DateHelper.now())
            .picName(picName)
            .build());
            
        return new JsonResult(StatusCode.OK);
    }

    @Override
    public JsonResult deleteById(int recordId) {
        Record record = recordMapper.queryById(recordId);
        if (record != null) {
            recordMapper.deleteByRecordId(recordId);
            imageService.delete(record.getPicName());
            return new JsonResult(StatusCode.OK);
        }
        else
            return new JsonResult(StatusCode.INVALID_RECORDID);
    }

    @Override
    public PageInfo<Record> fetchAllRecords(int pageSize, int pageNum, Long pigId) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<Record>(recordMapper.fetchAll(pigId));
    }

    @Override
    public PageInfo<Record> fetchLastWeekRecords(int pageSize, int pageNum, Long pigId) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<Record>(recordMapper.fetchLastWeek(pigId));
    }

    @Override
    public PageInfo<Record> fetchLast3WeekRecords(int pageSize, int pageNum, Long pigId) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<Record>(recordMapper.fetchLast3Week(pigId));
    }

}