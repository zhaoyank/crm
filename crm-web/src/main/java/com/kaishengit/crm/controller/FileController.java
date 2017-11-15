package com.kaishengit.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhao
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @GetMapping
    public String fileList() {
        return "file/list";
    }

}
