package com.dropbox.main.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class StorageService {

    @Value("${application.bucket.name")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    public void uploadFile(MultipartFile multipartFile, String fileName) {
        File convertedFile = convertMultiPartToFile(multipartFile);
        amazonS3.putObject(new PutObjectRequest("dropboxstorage", fileName, convertedFile));
        convertedFile.delete();
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) {
        File newFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            fileOutputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }
}