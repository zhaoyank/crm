package com.kaishengit.crm.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
public interface FileStore {

    /**
     * 上传文件
     * @param fileName 真实文件名
     * @param inputStream
     * @return
     */
    String uploadFile(String fileName, InputStream inputStream) throws IOException;

    /**
     * 下载文件
     * @param fileName
     * @return
     */
    InputStream downloadFile(String fileName) throws IOException;

}
