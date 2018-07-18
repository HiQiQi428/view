package org.luncert.view.service.implement;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.luncert.mullog.Mullog;
import org.luncert.mullog.annotation.BindAppender;
import org.luncert.view.component.ConfigManager;
import org.luncert.view.datasource.mysql.RecordMapper;
import org.luncert.view.datasource.mysql.StrainMapper;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.mysql.entity.Strain;
import org.luncert.view.datasource.neo4j.PigRepository;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.service.PigService;
import org.luncert.view.util.CipherHelper;
import org.luncert.view.util.DateHelper;
import org.luncert.view.util.IOHelper;
import org.luncert.view.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@BindAppender(name = "PigService")
public class PigServiceImpl implements PigService {

    Mullog mullog = new Mullog(this);

    @Autowired
    ConfigManager configManager;
    
    @Autowired
    StrainMapper strainMapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    PigRepository pigRepo;

    Map<Integer, String> strains;

    String imageStorePath;

    String imageFormat;

    private boolean isValidStrainIdentifier(int strain) { return strains.containsKey(strain); }

    private Pig findById(String userId, Long pigId) {
        for (Pig pig : pigRepo.findById(userId, pigId)) {
            if (pig.getId() == pigId) return pig;
        }
        return null;
    }

    /**
     * initialize
     */
    @PostConstruct
    public void init() throws IOException {
        strains = new HashMap<>();
        updateStrainMap();
        // init image store path
        imageStorePath = configManager.getProperty("image:storePath");
        if (imageStorePath == null || imageStorePath.length() == 0) {
            Path path = Paths.get(ResourceUtils.getFile("classpath:").getAbsolutePath(), "static", "images");
            imageStorePath = path.toString();
            configManager.setProperty("image:storePath", imageStorePath);
        }
        File storeDir = new File(imageStorePath);
        if (!storeDir.exists()) storeDir.mkdirs();
        // init image format
        imageFormat = configManager.getProperty("image:format");
        if (imageFormat == null || imageFormat.length() == 0) {
            imageFormat = "jpeg";
            configManager.setProperty("image:format", imageFormat);
        }
    }

    private void updateStrainMap() {
        for (Strain item : strainMapper.fetchAll()) strains.put(item.getId(), item.getValue());
    }

    @Override
    public Result addStrain(String value) {
        strainMapper.addStrain(value);
        updateStrainMap();
        return new Result(Result.OK, null, strains);
    }

    @Override
    public Result getStrainMap() { return new Result(Result.OK, null, strains); }

	@Override
	public Result addPig(String userId, String name, boolean beMale, String birthdate, int strain, String health, String eatingHabits, String appetite, Long fatherId, Long motherId, MultipartFile file) {
        if (!isValidStrainIdentifier(strain)) return new Result(Result.INVALID_STRAIN_IDENTIFIER);
        Pig father = null, mother = null;
        if (fatherId >= 0) {
            father = findById(userId, fatherId);
            if (father == null) return new Result(Result.PIG_NOT_FOUND, "connot found pig's father with ID = " + fatherId);
        }
        if (motherId >= 0) {
            mother = findById(userId, motherId);
            if (mother == null) return new Result(Result.PIG_NOT_FOUND, "connot found pig's mother with ID = " + motherId);
        }

        String picName;
		try {
			picName = saveImage(file);
		} catch (Exception e) {
            e.printStackTrace();
            return new Result(Result.EXCEPTOIN_OCCUR, null, e);
		}

        Pig pig = new Pig()
                .name(name)
                .beMale(beMale)
                .userId(userId)
                .birthdate(birthdate)
                .strain(strain)
                .health(health)
                .eatingHabits(eatingHabits)
                .appetite(appetite)
                .picName(picName)
                .father(father)
                .mother(mother);
        pigRepo.save(pig);
		return new Result(Result.OK, null, pig);
    }
    
    @Override
    public Result updatePig(String userId, Long pigId, String name, boolean beMale, String birthdate, int strain, String health, String eatingHabits, String appetite, Long fatherId, Long motherId) {
        Pig pig = findById(userId, pigId);
        if (pig != null) {
            // modify strain
            if (!isValidStrainIdentifier(strain)) return new Result(Result.INVALID_STRAIN_IDENTIFIER);
            else pig.strain(strain);

            // modify father
            if (fatherId >= 0) {
                Pig father = findById(userId, fatherId);
                if (father == null) return new Result(Result.PIG_NOT_FOUND, "connot found pig's father with ID = " + fatherId);
                else pig.father(father);
            }

            // modify mother
            if (motherId >= 0) {
                Pig mother = findById(userId, motherId);
                if (mother == null) return new Result(Result.PIG_NOT_FOUND, "connot found pig's mother with ID = " + motherId);
                else pig.mother(mother);
            }

            // others
            pig.name(name).beMale(beMale).birthdate(birthdate).health(health).eatingHabits(eatingHabits).appetite(appetite);

            // save
            pigRepo.save(pig);

            return new Result(Result.OK, null, pig);
        }
        else return new Result(Result.PIG_NOT_FOUND);
    }

	@Override
	public Result fetchAllPigs(String userId) {
        mullog.debug(userId);
        List<Pig> data = pigRepo.findByUserId(userId);
        if (data != null) return new Result(Result.OK, null, data);
        else return new Result(Result.FOUND_NOTHING);
    }
    
    @Override
    public Result queryById(String userId, Long pigId) {
        Pig pig = findById(userId, pigId);
        if (pig != null) return new Result(Result.OK, null, pig);
        else return new Result(Result.PIG_NOT_FOUND, "there's no pig with ID = " + pigId);
    }

    @Override
    public Result queryByName(String userId, String name) {
        List<Pig> data = pigRepo.findByName(userId, name);
        if (data != null) return new Result(Result.OK, null, data);
        else return new Result(Result.FOUND_NOTHING, "there's no pig with name = " + name);
    }

    @Override
    public Result queryByStrain(String userId, int strain) {
        if (isValidStrainIdentifier(strain)) {
            List<Pig> data = pigRepo.findByStrain(userId, strain);
            if (data != null) return new Result(Result.OK, null, data);
            else return new Result(Result.FOUND_NOTHING, "there's no pig with strain = " + strain);
        } else return new Result(Result.INVALID_STRAIN_IDENTIFIER);
    }

    @Override
    public Result deleteById(String userId, Long pigId) {
        Pig pig = findById(userId, pigId);
        if (pig != null) {
            // 删除neo4j中的数据
            pigRepo.delete(pig);
            // 删除mysql中的数据
            recordMapper.deleteById(pigId);
            return new Result(Result.OK);
        } else return new Result(Result.PIG_NOT_FOUND);
    }

    private String saveImage(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        // save image
        String picName = CipherHelper.hashcode(new Date().toString());
        // 转换图片格式
        BufferedImage source = ImageIO.read(file.getInputStream());
        BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        target.createGraphics().drawImage(source, 0, 0, Color.WHITE, null);
        // 以jpeg图片格式写入磁盘
        ImageIO.write(target, imageFormat, new File(imageStorePath, picName));
        return picName;
    }

	@Override
	public Result addRecord(MultipartFile file, Long pigId, String description) {
        if (file.isEmpty()
            || file.getOriginalFilename() == null
            || description.equals("")) {
            return new Result(Result.FIELD_IS_NULL);
        }

        String picName = null;
        try { picName = saveImage(file); }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(Result.EXCEPTOIN_OCCUR, null, e);
        }

        recordMapper.addRecord(pigId, description, DateHelper.now(), picName);
        return new Result(Result.OK);
    }

	@Override
	public void readImage(String picName, HttpServletResponse response) throws IOException {
        IOHelper.writeResponse("image/" + imageFormat, IOHelper.read(new File(imageStorePath, picName)), response);
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