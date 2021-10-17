package com.dropbox.main.service;

import com.dropbox.main.model.File;
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

    @Autowired
    public FileService(FileRepository fileRepository, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;

    }

    public void save(MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        File file = new File(fileName, multipartFile.getContentType());
        File savedFile = fileRepository.save(file);
    }

    public void update(int fileId, MultipartFile multipartFile) throws IOException {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isPresent()) {
            File existingFile = optionalFile.get();
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            File file = new File(existingFile.getId(), fileName, multipartFile.getContentType());
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

    public List<File> getFiles() {
        return fileRepository.allFiles();
    }

    public File delete(int fileId) throws FileNotFoundException {
        File file = getFile(fileId);
        fileRepository.deleteById(fileId);
        return file;
    }
}