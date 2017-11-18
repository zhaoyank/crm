package com.kaishengit.crm.file;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

/**
 * @author zhao
 */
@Component
public class FastDfsFileStore implements FileStore {

    @Value("${filepath}")
    private String filePath;

    /**
     * 真实文件名 组名和文件名的分隔符
     */
    private final String separator = "#";
    /**
     * 上传文件
     * @param fileName    真实文件名
     * @param inputStream
     * @return
     */
    @Override
    public String uploadFile(String fileName, InputStream inputStream) throws IOException {
        String exName = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            StorageClient storageClient = getStorageClient();

            String[] result = storageClient.upload_file(IOUtils.toByteArray(inputStream),exName,null);

            // 拼接文件保存的真实名称
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(result[0])
                    .append(separator)
                    .append(result[1]);

            return stringBuffer.toString();
        } catch (MyException e) {
            e.printStackTrace();
            throw new RuntimeException("文件保存到FastDfs失败");
        }
    }

    /**
     * 下载文件
     * @param fileName
     * @return
     */
    @Override
    public InputStream downloadFile(String fileName) throws IOException {
        String[] array = fileName.split(separator);
        String groupName = array[0];
        String saveName = array[1];
        try {
            StorageClient storageClient = getStorageClient();
            byte[] bytes = storageClient.download_file(groupName,saveName);
            return new ByteArrayInputStream(bytes);
        } catch (MyException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取FastDfs StorageClient
     * @return
     * @throws IOException
     * @throws MyException
     */
    private StorageClient getStorageClient() throws IOException, MyException {
        // 配置tracker
        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.163.100:22122");
        ClientGlobal.initByProperties(properties);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageServer storageServer = null;
        return new StorageClient(trackerServer, storageServer);
    }
}
