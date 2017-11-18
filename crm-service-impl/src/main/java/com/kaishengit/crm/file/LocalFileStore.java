package com.kaishengit.crm.file;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

/**
 * 本地文件上传下载
 * @author zhao
 */
@Component
public class LocalFileStore implements FileStore {

    @Value("${filepath}")
    private String filePath;

    /**
     * 上传文件
     * @param fileName    真实文件名
     * @param inputStream
     * @return
     */
    @Override
    public String uploadFile(String fileName, InputStream inputStream) throws IOException {

        String saveName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));

        OutputStream outputStream = new FileOutputStream(new java.io.File(filePath, saveName));
        IOUtils.copy(inputStream,outputStream);

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return saveName;
    }

    /**
     * 下载文件
     * @param fileName
     * @return
     */
    @Override
    public InputStream downloadFile(String fileName) throws IOException {
        return new FileInputStream(new File(filePath, fileName));
    }
}
