package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zhao
 */
public interface FileService {

    /**
     * 根据pId查找文件列表
     * @param pId
     * @return
     */
    List<File> findFileListByPId(Integer pId);

    /**
     * 保存文件夹
     * @param file
     */
    void saveNewDir(File file);

    /**
     * 文件上传
     * @param inputStream
     * @param pId
     * @param accountId
     * @param fileName
     * @param fileSize
     */
    void uploadFile(InputStream inputStream, Integer pId, Integer accountId, String fileName, Long fileSize);

    /**
     * 根据id查询对应的文件
     * @param id
     * @return
     */
    File findById(Integer id);

    /**
     * 文件下载
     * @param id
     * @return
     */
    InputStream downloadFile(Integer id) throws FileNotFoundException, IOException;

    /**
     * 在数据库中保存上传文件
     * @param pId
     * @param accountId
     * @param saveName
     * @param fileName
     * @param fileSize
     * @param password
     */
    void uploadFileToDB(Integer pId, Integer accountId, String saveName, String fileName, Long fileSize, String password);

    /**
     * 验证文件密码
     * @param id
     * @param password
     */
    void validatePassword(Integer id, String password);

    /**
     * 更新
     * @param file
     */
    void updateFileById(File file);

    /**
     * 根据Id删除文件
     * @param id
     */
    void deleteById(Integer id);
}
