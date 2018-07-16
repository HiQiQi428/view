package org.luncert.view.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public class IOHelper {

    public static String read(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) buffer.append(line).append("\n");
        reader.close();
        return buffer.toString();
    }

    public static String read(File file) throws IOException {
        return read(new FileInputStream(file));
    }

    /**
     * @param multipartFile: 上传的文件
     * @param path: 存储文件的目录
     */
    public static void saveImage(MultipartFile multipartFile, String fileName, String storeDir) throws Exception {
        FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
        String storePath = Paths.get(storeDir, CipherHelper.hashcode(fileName) + ".png").toString();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(storePath));

        int len;
        byte[] bs = new byte[1024];
        while ((len = fileInputStream.read(bs)) != -1) bos.write(bs, 0, len);
        bos.flush();
        bos.close();
    }
    
    /**
     * write string into response
     */
    public static void writeResponse(String contentType, String content, HttpServletResponse response) throws IOException {
        byte[] data = content.getBytes();
        response.reset();
        // cors
        response.setHeader("Access-Control-Allow-Origin", "*");  
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");  
        response.setHeader("Access-Control-Max-Age", "3600");  
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");  
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // content
        response.setContentType(contentType);
        response.setContentLength(data.length);
        // output
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * write response from input stream
     */
    public static void writeResponse(String contentType, InputStream inputStream, HttpServletResponse response) throws IOException {
        writeResponse(contentType, read(inputStream), response);
    }

    /**
     * write file into response
     */
    public static void writeResponse(File file, HttpServletResponse response) throws IOException {
        writeResponse("application/octet-stream;charset=UTF-8", read(file), response);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(file.getName(), "UTF-8")));
    }
    
}
