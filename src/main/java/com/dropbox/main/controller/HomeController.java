package com.dropbox.main.controller;

import com.dropbox.main.model.File;
import com.dropbox.main.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private FileService fileService;

    @Autowired
    public HomeController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        return "main";
    }
}
