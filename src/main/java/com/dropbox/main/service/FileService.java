package com.dropbox.main.service;

import com.dropbox.main.model.File;
import com.dropbox.main.model.User;
import com.dropbox.main.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final StorageService storageService;
    private final UserService userService;

    @Autowired
    public FileService(FileRepository fileRepository, StorageService storageService, UserService userService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
        this.userService = userService;

    }

    public void save(MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        User user = userService.getCurrentUser();
        File file = new File(fileName, multipartFile.getContentType());
        file.setUser(user);
        File savedFile = fileRepository.save(file);
        String awsFileName = savedFile.getId() + "_" + fileName;
        this.storageService.uploadFile(multipartFile, awsFileName);
    }

    public void update(int fileId, MultipartFile multipartFile) throws IOException {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isPresent()) {
            File existingFile = optionalFile.get();
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            File file = new File(existingFile.getId(), fileName, multipartFile.getContentType());
            storageService.deleteFile(existingFile.getId() + "_" + existingFile.getName());
            storageService.uploadFile(multipartFile, existingFile.getId() + "_" + fileName);
            fileRepository.save(file);
        } else {
            throw new FileNotFoundException("file not found");
        }
    }

    public File getFile(int id) throws FileNotFoundException {
        Optional<File> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            return optionalFile.get();
        } else {
            throw new FileNotFoundException("file not found");
        }
    }

    public List<File> getFiles(int userId) {
        return fileRepository.allFiles(userId);
    }

    public File delete(int fileId) throws FileNotFoundException {
        File file = getFile(fileId);
        fileRepository.deleteById(fileId);
        return file;
    }
}