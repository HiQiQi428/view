package org.luncert.view.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.luncert.simpleutils.JsonResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String save(MultipartFile file) throws IOException;
    
    boolean delete(String picName);
    
    /**
     * 用于直接读图片，操作成功 JsonResult 的 data 为空，否则为异常信息
     */
    JsonResult loadImage(String picName, HttpServletResponse response);
    
}