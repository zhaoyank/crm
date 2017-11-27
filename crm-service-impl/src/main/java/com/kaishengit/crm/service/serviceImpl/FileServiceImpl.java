package com.kaishengit.crm.service.serviceImpl;

import com.kaishengit.crm.entity.File;
import com.kaishengit.crm.entity.FileExample;
import com.kaishengit.crm.file.FileStore;
import com.kaishengit.crm.mapper.FileMapper;
import com.kaishengit.crm.service.FileService;
import com.kaishengit.crm.service.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @author zhao
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    /**
     * 上传和下载文件的接口
     */
    @Autowired
    @Qualifier("qiNiuCloudFileStore")
    private FileStore fileStore;

    @Value("${filepath}")
    private String filePath;

    /**
     * 根据pId查找文件列表
     * @param pId
     * @return
     */
    @Override
    public List<File> findFileListByPId(Integer pId) {
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andPIdEqualTo(pId);
        fileExample.setOrderByClause("type asc");
        return fileMapper.selectByExample(fileExample);
    }

    /**
     * 保存文件夹
     * @param file
     */
    @Override
    public void saveNewDir(File file) {
        FileExample fileExample = new FileExample();
        fileExample.createCriteria().andPIdEqualTo(file.getpId())
                .andFileNameEqualTo(file.getFileName());
        List<File> fileList = fileMapper.selectByExample(fileExample);
        if (!fileList.isEmpty()) {
            throw new ServiceException("同一目录下文件夹名不能重复");
        }

        file.setDownloadCount(0);
        file.setType(File.FILE_TYPE_DIR);
        file.setUpdateTime(new Date());
        fileMapper.insertSelective(file);
    }

    /**
     * 文件上传
     * @param inputStream
     * @param pId
     * @param accountId
     * @param fileName
     * @param fileSize
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void uploadFile(InputStream inputStream, Integer pId, Integer accountId, String fileName, Long fileSize) {
        File file = new File();
        file.setType(File.FILE_TYPE_FILE);
        file.setDownloadCount(0);
        file.setAccountId(accountId);
        file.setpId(pId);
        file.setUpdateTime(new Date());
        file.setFileName(fileName);
        file.setFileSize(FileUtils.byteCountToDisplaySize(fileSize));

        String saveName = "";

        try {
            saveName = fileStore.uploadFile(fileName, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }

        file.setSaveName(saveName);
        fileMapper.insertSelective(file);
    }

    /**
     * 根据id查询对应的文件
     * @param id
     * @return
     */
    @Override
    public File findById(Integer id) {
        return fileMapper.selectByPrimaryKey(id);
    }

    /**
     * 文件下载
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public InputStream downloadFile(Integer id) throws IOException,ServiceException {
        File file = fileMapper.selectByPrimaryKey(id);
        if (file == null || file.getType().equals(File.FILE_TYPE_DIR)) {
            throw new ServiceException("该文件不存在或已被删除");
        }

        file.setDownloadCount(file.getDownloadCount() + 1);
        fileMapper.updateByPrimaryKeySelective(file);

        return fileStore.downloadFile(file.getSaveName());
    }

    /**
     * 在数据库中保存上传文件
     * @param pId
     * @param accountId
     * @param saveName
     * @param fileName
     * @param fileSize
     * @param password
     */
    @Override
    public void uploadFileToDB(Integer pId, Integer accountId, String saveName, String fileName, Long fileSize, String password) {
        File file = new File();
        file.setType(File.FILE_TYPE_FILE);
        file.setDownloadCount(0);
        file.setAccountId(accountId);
        file.setpId(pId);
        file.setUpdateTime(new Date());
        file.setFileName(fileName);
        file.setFileSize(FileUtils.byteCountToDisplaySize(fileSize));
        file.setSaveName(saveName);
        file.setPassword(password);

        fileMapper.insertSelective(file);
    }

    /**
     * 验证文件密码
     * @param id
     * @param password
     */
    @Override
    public void validatePassword(Integer id, String password) {
        password = DigestUtils.md5Hex(password);
        File file = fileMapper.selectByPrimaryKey(id);

        if(file == null)  {
            throw new ServiceException("文件不存在或已被删除");
        }
        if (!password.equals(file.getPassword())) {
            throw new ServiceException("密码错误");
        }
    }

    /**
     * 更新
     *
     * @param file
     */
    @Override
    public void updateFileById(File file) {
        FileExample fileExample = new FileExample();
        fileExample.createCriteria()
                .andFileNameEqualTo(file.getFileName())
                .andPIdEqualTo(file.getpId());
        List<File> fileList = fileMapper.selectByExample(fileExample);

        if (fileList.isEmpty()) {
            fileMapper.updateByPrimaryKeySelective(file);
        } else {
            throw new ServiceException("文件名重复");
        }

    }

    /**
     * 根据Id删除文件
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        List<File> fileList = findFileListByPId(id);
        if (fileList != null && !fileList.isEmpty()) {
            throw new ServiceException("请删除文件夹中的文件后在删除文件夹");
        }
        fileMapper.deleteByPrimaryKey(id);
    }
}