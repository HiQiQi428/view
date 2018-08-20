package org.luncert.view.service.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.luncert.mullog.Mullog;
import org.luncert.simpleutils.ContentType;
import org.luncert.simpleutils.DateHelper;
import org.luncert.simpleutils.IOHelper;
import org.luncert.simpleutils.Result;
import org.luncert.view.datasource.mysql.RecordMapper;
import org.luncert.view.datasource.mysql.StrainMapper;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.mysql.entity.Strain;
import org.luncert.view.datasource.neo4j.PigRepository;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.Pig.Status;
import org.luncert.view.service.PigService;
import org.luncert.view.util.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PigServiceImpl implements PigService {

    Mullog mullog = new Mullog("PigService");

    @Autowired
    StrainMapper strainMapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    PigRepository pigRepo;

    Map<Integer, String> strains;

    @Value("${image.storePath}")
    String imageStorePath;

    @Value("${image.format}")
    String imageFormat;

    private boolean isValidStrainIdentifier(int strain) { return strains.containsKey(strain); }

    private Pig findById(String userId, Long pigId) {
        for (Pig pig : pigRepo.findById(userId, pigId)) {
            if (pig.getId() == pigId) return pig;
        }
        return null;
    }

    private void updateStrainMap() {
        for (Strain item : strainMapper.fetchAll()) strains.put(item.getId(), item.getValue());
    }

    private void deleteImage(String picName) {
        File file = new File(imageStorePath, picName);
        if (file.exists()) file.delete();
    }

    /**
     * initialize
     */
    @PostConstruct
    public void init() throws IOException {
        strains = new HashMap<>();
        updateStrainMap();
        // init image store path
        if (imageStorePath == null || imageStorePath.length() == 0) {
            throw new NullPointerException("image store path");
        } else {
            File storeDir = new File(imageStorePath);
            if (!storeDir.exists())
                storeDir.mkdirs();
        }
        // check image format
        if (imageFormat == null || imageFormat.length() == 0) {
            throw new NullPointerException("image format");
        }
    }

    @Override
    public Result addStrain(String value) {
        strainMapper.addStrain(value);
        updateStrainMap();
        return new Result(StatusCode.OK, null, strains);
    }

    @Override
    public Result getStrainMap() { return new Result(StatusCode.OK, null, strains); }

    @Override
    public Result addPig(String userId, String name, int strain, boolean beMale, Status status, String birthdate, MultipartFile file) {
        // 检查 strain 是否合法
        if (!isValidStrainIdentifier(strain)) return new Result(StatusCode.INVALID_STRAIN_IDENTIFIER);
        // 保存图片，并获取生成的 picName
        String picName;
		try {
            picName = IOHelper.saveImage(file.getInputStream(), imageFormat, imageStorePath);
        }
        catch (Exception e) {
            return new Result(StatusCode.EXCEPTION_OCCUR, "failed to save image", e);
        }
        // 存储到数据库
        Pig pig = Pig.builder()
            .userId(userId)
            .name(name)
            .strain(strain)
            .beMale(beMale)
            .status(status)
            .birthdate(birthdate)
            .picName(picName)
            .build();
        pigRepo.save(pig);
		return new Result(StatusCode.OK, null, pig.toString());
    }
    
    @Override
    public Result updatePig(String userId, Long pigId, String name, int strain, boolean beMale, Status status, String birthdate) {
        if (!isValidStrainIdentifier(strain)) return new Result(StatusCode.INVALID_STRAIN_IDENTIFIER);
        // 获取猪
        Pig pig = findById(userId, pigId);
        if (pig != null) {
            // 更新数据
            pig.setName(name);
            pig.setStrain(strain);
            pig.setBeMale(beMale);
            pig.setStatus(status);
            pig.setBirthdate(birthdate);
            // save
            pigRepo.save(pig);
            return new Result(StatusCode.OK, null, pig.toString());
        }
        else return new Result(StatusCode.PIG_NOT_FOUND);
    }

	@Override
	public Result fetchAllPigs(String userId) {
        mullog.debug(userId);
        List<Pig> data = pigRepo.findByUserId(userId);
        if (data != null) return new Result(StatusCode.OK, null, data);
        else return new Result(StatusCode.FOUND_NOTHING);
    }
    
    @Override
    public Result queryById(String userId, Long pigId) {
        Pig pig = findById(userId, pigId);
        if (pig != null) return new Result(StatusCode.OK, null, pig.toString());
        else return new Result(StatusCode.PIG_NOT_FOUND, "there's no pig with ID = " + pigId);
    }

    @Deprecated
    @Override
    public Result queryByName(String userId, String name) {
        List<Pig> data = pigRepo.findByName(userId, name);
        if (data != null) return new Result(StatusCode.OK, null, data);
        else return new Result(StatusCode.FOUND_NOTHING, "there's no pig with name = " + name);
    }

    @Deprecated
    @Override
    public Result queryByStrain(String userId, int strain) {
        if (isValidStrainIdentifier(strain)) {
            List<Pig> data = pigRepo.findByStrain(userId, strain);
            if (data != null) return new Result(StatusCode.OK, null, data);
            else return new Result(StatusCode.FOUND_NOTHING, "there's no pig with strain = " + strain);
        } else return new Result(StatusCode.INVALID_STRAIN_IDENTIFIER);
    }

    @Override
    public Result deleteById(String userId, Long pigId) {
        Pig pig = findById(userId, pigId);
        if (pig != null) {
            // 删除neo4j中的数据与图片
            deleteImage(pig.getPicName());
            pigRepo.delete(pig);
            // 删除mysql中的数据与图片
            List<Record> records = recordMapper.fetchAll(pigId);
            for (Record record : records) deleteImage(record.getPicName());
            recordMapper.deleteById(pigId);
            return new Result(StatusCode.OK);
        } else return new Result(StatusCode.PIG_NOT_FOUND);
    }

	@Override
	public Result addRecord(MultipartFile file, Long pigId, float weight, String description) {
        if (file.isEmpty()
            || file.getOriginalFilename() == null
            || description.equals("")) {
            return new Result(StatusCode.FIELD_IS_NULL);
        }

        String picName = null;
        try {
            picName = IOHelper.saveImage(file.getInputStream(), imageFormat, imageStorePath);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(StatusCode.EXCEPTION_OCCUR, null, e);
        }

        recordMapper.addRecord(Record.builder()
            .pigId(pigId)
            .weight(weight)
            .description(description)
            .timestamp(DateHelper.now())
            .picName(picName)
            .build());
        return new Result(StatusCode.OK);
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

    @Override
    public Result loadImage(String picName, HttpServletResponse response) {
        File file = new File(imageStorePath, picName);
        if (!file.exists()) return new Result(StatusCode.PICTURE_NOT_FOUND);
		try {
			FileInputStream inputStream = new FileInputStream(file);
            IOHelper.writeResponse(ContentType.CONTENT_TYPE_JPEG, inputStream, response, true);
            return null; // ok
		} catch (IOException e) {
            e.printStackTrace();
            return new Result(StatusCode.EXCEPTION_OCCUR, null, e);
		}
    }

}