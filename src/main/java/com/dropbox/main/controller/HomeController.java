package com.dropbox.main.controller;

import com.dropbox.main.model.File;
import com.dropbox.main.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final FileService fileService;

    @Autowired
    public HomeController(FileService fileService) {
        this.fileService = fileService;
    }

    @ModelAttribute
    public void addModelAttributes(Model model) {
        model.addAttribute("edit", "false");
    }

    @GetMapping("/")
    public String getHome(Model model) {
        List<File> files = fileService.getFiles();
        model.addAttribute("files", files);
        return "home";
    }

    @PostMapping("upload")
    public String saveFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.save(file);
        return "redirect:/";
    }

    @GetMapping("file{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileId") int id) throws FileNotFoundException {
        File file = fileService.getFile(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(new ByteArrayResource((file.getData())));
    }

    @GetMapping("/delete/file{fileId}")
    public String deleteFile(@PathVariable int fileId) {
        fileService.delete(fileId);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam int fileId, Model model) {
        model.addAttribute("edit", "true");
        model.addAttribute("fileId", fileId);
        return getHome(model);
    }

    @PostMapping("/update/file{fileId}")
    public String updateFile(@PathVariable int fileId, @RequestParam("file") MultipartFile file) throws IOException {
        fileService.update(fileId, file);
        return "redirect:/";
    }

    @GetMapping("/share/file{fileId}")
    public String generateUrl(@PathVariable("fileId") int fileId, Model model) {
        String url = MvcUriComponentsBuilder
                .fromMethodName(HomeController.class, "downloadFile", fileId).build().toString();
        model.addAttribute("url", url);
        return "sharefile";
    }
}