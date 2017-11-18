package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.File;
import com.kaishengit.crm.service.FileService;
import com.kaishengit.crm.service.exception.ServiceException;
import com.kaishengit.crm.web.exception.NotFoundException;
import com.kaishengit.util.JsonResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/disk")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public String fileList(Model model,
                           @RequestParam(required = false,defaultValue = "0",name = "_") Integer pId) {
        List<File> fileList = fileService.findFileListByPId(pId);
        if(pId != 0) {
            File file = fileService.findById(pId);
            model.addAttribute("file",file);
        }
        model.addAttribute("fileList",fileList);
        return "disk/list";
    }

    @GetMapping("/list.json")
    @ResponseBody
    public JsonResult list(@RequestParam(name = "_",required = false,defaultValue = "0") Integer pId) {
        List<File> fileList = fileService.findFileListByPId(pId);
        return JsonResult.success(fileList);
    }

    @PostMapping("/new/dir")
    @ResponseBody
    public JsonResult createNewDir(File file) {
        try {
            fileService.saveNewDir(file);
            List<File> fileList = fileService.findFileListByPId(file.getpId());
            return JsonResult.success(fileList);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return JsonResult.error(ex.getMessage());
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public JsonResult fileUpload(Integer pId, Integer accountId, MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            return JsonResult.error("文件不能为空");
        }

        InputStream inputStream = file.getInputStream();
        Long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();

        try {
            fileService.uploadFile(inputStream, pId, accountId, fileName, fileSize);
            List<File> fileList = fileService.findFileListByPId(pId);
            return JsonResult.success(fileList);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return JsonResult.error(ex.getMessage());
        }
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam(name = "_") Integer id,
                             @RequestParam(required = false,defaultValue = "") String fileName,
                             HttpServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = fileService.downloadFile(id);

            //如果url中没有fileName,则在线预览
            if(StringUtils.isNotEmpty(fileName)) {
                //下载,设置MIME类型
                response.setContentType("application/octet-stream");
                //设置下载对话框
                fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
                //设置对话框下载文件名
                response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
            }
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException | ServiceException ex) {
            ex.printStackTrace();
            throw new NotFoundException();
        }
    }

}
