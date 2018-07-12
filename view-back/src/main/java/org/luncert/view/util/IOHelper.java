package org.luncert.view.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

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
}
