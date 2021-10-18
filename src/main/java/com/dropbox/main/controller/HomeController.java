package com.dropbox.main.controller;

import com.dropbox.main.model.File;
import com.dropbox.main.model.Notification;
import com.dropbox.main.model.OwnerGuest;
import com.dropbox.main.model.User;
import com.dropbox.main.service.FileService;
import com.dropbox.main.service.OwnerGuestService;
import com.dropbox.main.service.StorageService;
import com.dropbox.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final OwnerGuestService ownerGuestService;
    private final StorageService storageService;
    JavaMailSender javaMailSender = getJavaMailSender();
    private int fileId;
    private String url;
    private Set<String> emailsSelected = new HashSet<>();
    private User user;

    @Autowired
    public HomeController(FileService fileService,
                          UserService userService,
                          StorageService storageService,
                          OwnerGuestService ownerGuestService) {
        this.ownerGuestService = ownerGuestService;
        this.userService = userService;
        this.fileService = fileService;
        this.storageService = storageService;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("narasimha8186094547@gmail.com");
        mailSender.setPassword("fubwbpumstgwnxef");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @ModelAttribute
    public void addModelAttributes(Model model) {
        model.addAttribute("edit", "false");
    }

    @GetMapping("/")
    public String getHome(Model model) {
        this.user = userService.getCurrentUser();
        model.addAttribute("files", fileService.getFiles(this.user.getId()));
        return "home";
    }

    @PostMapping("upload")
    public String saveFile(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.save(file);
        return "redirect:/";
    }

    @GetMapping("/download/file{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileId") int id) throws FileNotFoundException {
        File file = fileService.getFile(id);
        String fileName = file.getId() + "_" + file.getName();
        byte[] fileData = storageService.downloadFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(fileData));
    }

    @GetMapping("/view/file{fileId}")
    public ResponseEntity<ByteArrayResource> viewFile(@PathVariable("fileId") int id) throws FileNotFoundException {
        File file = fileService.getFile(id);
        String fileName = file.getId() + "_" + file.getName();
        byte[] fileData = storageService.downloadFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(fileData));
    }

    @GetMapping("/delete/file{fileId}")
    public String deleteFile(@PathVariable int fileId) throws FileNotFoundException {
        File file = fileService.getFile(fileId);
        file.setDeleted(true);
        fileService.saveFile(file);
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
    public String shareFile(@PathVariable int fileId, Model model) {
        String url = MvcUriComponentsBuilder
                .fromMethodName(HomeController.class, "downloadFile", fileId).build().toString();
        this.fileId = fileId;
        this.url = url;
        model.addAttribute("guestUsers", userService.getAllUsers());
        return "sharefile";
    }

    @PostMapping(value = "/share", params = {"add"})
    public String addEmail(@RequestParam("email") String email, Model model) {
        emailsSelected.add(email);
        model.addAttribute("emailsSelected", emailsSelected);
        model.addAttribute("guestUsers", userService.getAllUsers());
        return "sharefile";
    }

    @PostMapping(value = "/share", params = {"removeEmail"})
    public String removeEmail(@RequestParam("removeEmail") String email, Model model) {
        emailsSelected.remove(email);
        model.addAttribute("emailsSelected", emailsSelected);
        model.addAttribute("guestUsers", userService.getAllUsers());
        return "sharefile";
    }

    @PostMapping("/share")
    public String sendFile(@RequestParam("edit") boolean access) throws MessagingException, IOException {
        int userId = this.user.getId();
        int[] guestIds = userService.getIdsByEmail(emailsSelected);
        ownerGuestService.save(userId, fileId, guestIds, access);
        String[] emails = Arrays.copyOf(emailsSelected.toArray(), emailsSelected.size(),
                String[].class);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emails);
        msg.setSubject("Shared File Link");
        msg.setText("click on link to download file : " + url);
        javaMailSender.send(msg);
        emailsSelected.clear();
        return "redirect:/";
    }

    @GetMapping("/notification")
    public String notification(Model model) {
        int loginUserId = this.user.getId();
        List<OwnerGuest> list = ownerGuestService.findByGuestId(loginUserId);
        List<Notification> notificationList = ownerGuestService.getNotificationList(list);
        model.addAttribute("notificationList", notificationList);
        model.addAttribute("fileId", fileId);
        return "notification";
    }

    @GetMapping("/editNotification")
    public String editFile(@RequestParam int fileId, Model model) {
        int loginUserId = this.user.getId();
        List<OwnerGuest> list = ownerGuestService.findByGuestId(loginUserId);
        List<Notification> notificationList = ownerGuestService.getNotificationList(list);
        model.addAttribute("notificationList", notificationList);
        model.addAttribute("edit", true);
        model.addAttribute("fileId", fileId);
        return "notification";
    }
}