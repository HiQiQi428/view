package org.luncert.view.service.implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.luncert.simpleutils.ContentType;
import org.luncert.simpleutils.IOHelper;
import org.luncert.simpleutils.JsonResult;
import org.luncert.view.pojo.StatusCode;
import org.luncert.view.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${image.storePath}")
    String imageStorePath;

    @Value("${image.format}")
    String imageFormat;

    /**
     * 检查 imageStorePath、imageFormat
     */
    @PostConstruct
    public void init() {
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
	public String save(MultipartFile file) throws IOException {
        return IOHelper.saveImage(file.getInputStream(), imageFormat, imageStorePath);
	}

	@Override
	public boolean delete(String picName) {
        if (picName == null)
            return false;
        File file = new File(imageStorePath, picName);
        if (file.exists()) {
            file.delete();
            return true;
        }
        else
            return false;
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