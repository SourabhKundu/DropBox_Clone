package com.dropbox.main.controller;

import com.dropbox.main.model.File;
import com.dropbox.main.model.User;
import com.dropbox.main.service.FileService;
import com.dropbox.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/")
public class BinController {

    private final FileService fileService;
    private final UserService userService;
    private User user;

    @Autowired
    public BinController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

}