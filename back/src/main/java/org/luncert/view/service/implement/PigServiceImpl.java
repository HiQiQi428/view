package org.luncert.view.service.implement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.luncert.simpleutils.JsonResult;
import org.luncert.view.datasource.mysql.EnclosureMapper;
import org.luncert.view.datasource.mysql.RecordMapper;
import org.luncert.view.datasource.mysql.StrainMapper;
import org.luncert.view.datasource.mysql.entity.Enclosure;
import org.luncert.view.datasource.mysql.entity.Strain;
import org.luncert.view.datasource.neo4j.PigRepository;
import org.luncert.view.datasource.neo4j.WxUserRepository;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.luncert.view.datasource.neo4j.entity.Pig.Status;
import org.luncert.view.service.ImageService;
import org.luncert.view.service.PigService;
import org.luncert.view.pojo.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PigServiceImpl implements PigService {

    @Autowired
    StrainMapper strainMapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    EnclosureMapper enclosureMapper;

    @Autowired
    ImageService imageService;

    @Autowired
    WxUserRepository wxUserRepos;

    @Autowired
    PigRepository pigRepos;

    Map<Integer, String> strains;

    private boolean isValidStrainIdentifier(int strain) {
        return strains.containsKey(strain);
    }

    private void updateStrainMap() {
        for (Strain item : strainMapper.fetchAll())
            strains.put(item.getId(), item.getValue());
    }

    /**
     * 初始化 strainMap
     */
    @PostConstruct
    public void init() throws IOException {
        strains = new HashMap<>();
        updateStrainMap();
    }

    @Override
    public JsonResult queryEnclosure(WxUser wxUser) {
        return new JsonResult(StatusCode.OK, null, enclosureMapper.query(wxUser.getUserId()));
    }

    @Override
    public JsonResult addStrain(String value) {
        try {
            strainMapper.addStrain(value); // value 字段有UNIQUE约束,添加重复的值会抛出异常
        } catch (Exception e) {
            return new JsonResult(StatusCode.STRAIN_EXISTS);
        }
        updateStrainMap();
        return new JsonResult(StatusCode.OK, null, strains);
    }

    @Override
    public JsonResult deleteStrain(int id) {
        if (strains.containsKey(id)) {
            strainMapper.deleteStrain(id);
            strains.remove(id);
            return new JsonResult(StatusCode.OK);
        }
        else
            return new JsonResult(StatusCode.INVALID_STRAINID);
    }

    @Override
    public JsonResult getStrainMap() { return new JsonResult(StatusCode.OK, null, strains); }

    @Override
    public JsonResult addPig(WxUser wxUser, String name, int strain, boolean beMale, String enclosure, Status status, String birthdate, MultipartFile file) {
        if (!isValidStrainIdentifier(strain))
            return new JsonResult(StatusCode.INVALID_STRAIN_IDENTIFIER);

        String picName;
		try {
            picName = imageService.save(file);
        }
        catch (Exception e) {
            return new JsonResult(StatusCode.EXCEPTION_OCCUR, "failed to save image", e);
        }

        try {
            Enclosure e = new Enclosure();
            e.setUserId(wxUser.getUserId());
            e.setValue(enclosure);
            enclosureMapper.add(e);
        } catch (Exception ex) {}

        Pig pig = Pig.builder()
            .name(name)
            .strain(strain)
            .beMale(beMale)
            .status(status)
            .birthdate(birthdate)
            .picName(picName)
            .build();
        pigRepos.save(pig);
        wxUserRepos.bindPig(wxUser, pig);

		return new JsonResult(StatusCode.OK, null, pig.toString());
    }

    @Override
    public JsonResult addPig(String userId, String name, int strain, boolean beMale, String enclosure, Status status, String birthdate, MultipartFile file) {
        WxUser wxUser = wxUserRepos.findByUserId(userId);
        if (wxUser != null)
            return addPig(wxUser, name, strain, beMale, enclosure, status, birthdate, file);
        else
            return new JsonResult(StatusCode.INVALID_USERID);
    }
    
    @Override
    public JsonResult updatePig(long pigId, String name, int strain, boolean beMale, String enclosure, Status status, String birthdate) {
        if (!isValidStrainIdentifier(strain))
            return new JsonResult(StatusCode.INVALID_STRAIN_IDENTIFIER);

        Optional<Pig> optional = pigRepos.findById(pigId);
        if (optional.isPresent()) {
            Pig pig = optional.get();
            pig.setName(name);
            pig.setStrain(strain);
            pig.setBeMale(beMale);
            pig.setEnclosure(enclosure);
            pig.setStatus(status);
            pig.setBirthdate(birthdate);
            pigRepos.save(pig);
            return new JsonResult(StatusCode.OK, null, pig.toString());
        }
        else
            return new JsonResult(StatusCode.PIG_NOT_FOUND);
    }

	@Override
	public JsonResult fetchAllPigs(WxUser wxUser) {
        List<Pig> data = wxUserRepos.findPigs(wxUser);
        if (data != null && data.size() > 0)
            return new JsonResult(StatusCode.OK, null, data);
        else
            return new JsonResult(StatusCode.FOUND_NOTHING);
    }

    @Override
    public JsonResult fetchAllPigs(String userId) {
        WxUser wxUser = wxUserRepos.findByUserId(userId);
        if (wxUser != null)
            return fetchAllPigs(wxUser);
        else
            return new JsonResult(StatusCode.INVALID_USERID);
    }

    @Override
    public JsonResult deleteById(WxUser wxUser, long pigId) {
        Optional<Pig> optional = pigRepos.findById(pigId);
        if (optional.isPresent()) {
            Pig pig = optional.get();
            wxUserRepos.unbindPig(wxUser, pig);
            pigRepos.delete(pig);

            imageService.delete(pig.getPicName());
            recordMapper.fetchPicNameByPigId(pigId).forEach((picName) -> {
                imageService.delete(picName);
            });;

            recordMapper.deleteByPigId(pigId);
            return new JsonResult(StatusCode.OK);
        }
        else
            return new JsonResult(StatusCode.PIG_NOT_FOUND);
    }

    @Override
    public JsonResult deleteById(String userId, long pigId) {
        WxUser wxUser = wxUserRepos.findByUserId(userId);
        if (wxUser != null)
            return deleteById(wxUser, pigId);
        else
            return null;
    }

}
// 