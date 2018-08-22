package org.luncert.view.service.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.luncert.mullog.Mullog;
import org.luncert.simpleutils.ContentType;
import org.luncert.simpleutils.DateHelper;
import org.luncert.simpleutils.IOHelper;
import org.luncert.simpleutils.JsonResult;
import org.luncert.view.datasource.mysql.RecordMapper;
import org.luncert.view.datasource.mysql.StrainMapper;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.mysql.entity.Strain;
import org.luncert.view.datasource.neo4j.PigRepository;
import org.luncert.view.datasource.neo4j.WxUserRepository;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
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
    WxUserRepository wxUserRepos;

    @Autowired
    PigRepository pigRepo;

    Map<Integer, String> strains;

    @Value("${image.storePath}")
    String imageStorePath;

    @Value("${image.format}")
    String imageFormat;

    private boolean isValidStrainIdentifier(int strain) {
        return strains.containsKey(strain);
    }

    private void updateStrainMap() {
        for (Strain item : strainMapper.fetchAll())
            strains.put(item.getId(), item.getValue());
    }

    private void deleteImage(String picName) {
        File file = new File(imageStorePath, picName);
        if (file.exists())
            file.delete();
    }

    /**
     * 初始化 strainMap，检查 imageStorePath、imageFormat
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
    public JsonResult addStrain(String value) {
        strainMapper.addStrain(value);
        updateStrainMap();
        return new JsonResult(StatusCode.OK, null, strains);
    }

    @Override
    public JsonResult getStrainMap() { return new JsonResult(StatusCode.OK, null, strains); }

    @Override
    public JsonResult addPig(WxUser wxUser, String name, int strain, boolean beMale, Status status, String birthdate, MultipartFile file) {
        if (!isValidStrainIdentifier(strain))
            return new JsonResult(StatusCode.INVALID_STRAIN_IDENTIFIER);

        String picName;
		try {
            picName = IOHelper.saveImage(file.getInputStream(), imageFormat, imageStorePath);
        }
        catch (Exception e) {
            return new JsonResult(StatusCode.EXCEPTION_OCCUR, "failed to save image", e);
        }

        Pig pig = Pig.builder()
            .name(name)
            .strain(strain)
            .beMale(beMale)
            .status(status)
            .birthdate(birthdate)
            .picName(picName)
            .build();
        pigRepo.save(pig);
        wxUserRepos.addPig(wxUser, pig);

		return new JsonResult(StatusCode.OK, null, pig.toString());
    }
    
    @Override
    public JsonResult updatePig(Long pigId, String name, int strain, boolean beMale, Status status, String birthdate) {
        if (!isValidStrainIdentifier(strain))
            return new JsonResult(StatusCode.INVALID_STRAIN_IDENTIFIER);

        Optional<Pig> optional = pigRepo.findById(pigId);
        if (optional.isPresent()) {
            Pig pig = optional.get();
            pig.setName(name);
            pig.setStrain(strain);
            pig.setBeMale(beMale);
            pig.setStatus(status);
            pig.setBirthdate(birthdate);
            pigRepo.save(pig);
            return new JsonResult(StatusCode.OK, null, pig.toString());
        }
        else
            return new JsonResult(StatusCode.PIG_NOT_FOUND);
    }

	@Override
	public JsonResult fetchAllPigs(WxUser wxUser) {
        List<Pig> data = wxUserRepos.findPigs(wxUser);
        if (data != null)
            return new JsonResult(StatusCode.OK, null, data);
        else
            return new JsonResult(StatusCode.FOUND_NOTHING);
    }

    @Override
    public JsonResult deleteById(WxUser wxUser, Long pigId) {
        Optional<Pig> optional = pigRepo.findById(pigId);
        if (optional.isPresent()) {
            Pig pig = optional.get();
            wxUserRepos.removePig(wxUser, pig);
            pigRepo.delete(pig);

            deleteImage(pig.getPicName());
            List<Record> records = recordMapper.fetchAll(pigId);
            for (Record record : records) {
                deleteImage(record.getPicName());
            }

            recordMapper.deleteById(pigId);
            return new JsonResult(StatusCode.OK);
        }
        else
            return new JsonResult(StatusCode.PIG_NOT_FOUND);
    }

	@Override
	public JsonResult addRecord(MultipartFile file, Long pigId, float weight, String description) {
        if (file.isEmpty()
            || file.getOriginalFilename() == null
            || description.equals("")) {
            return new JsonResult(StatusCode.FIELD_IS_NULL);
        }

        String picName = null;
        try {
            picName = IOHelper.saveImage(file.getInputStream(), imageFormat, imageStorePath);
        }
        catch (Exception e) {
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
    public JsonResult loadImage(String picName, HttpServletResponse response) {
        File file = new File(imageStorePath, picName);
        if (!file.exists()) return new JsonResult(StatusCode.PICTURE_NOT_FOUND);
		try {
			FileInputStream inputStream = new FileInputStream(file);
            IOHelper.writeResponse(ContentType.CONTENT_TYPE_JPEG, inputStream, response, true);
            return null; // ok
		} catch (IOException e) {
            return new JsonResult(StatusCode.EXCEPTION_OCCUR, null, e);
		}
    }

}