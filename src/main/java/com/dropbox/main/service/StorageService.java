package com.dropbox.main.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class StorageService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        this.bucketName = "dropboxstorage";
    }

    public void uploadFile(MultipartFile multipartFile, String fileName) {
        File convertedFile = convertMultiPartToFile(multipartFile);
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
        convertedFile.delete();
    }

    public byte[] downloadFile(String fileName) {
        S3Object awsFile = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = awsFile.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
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