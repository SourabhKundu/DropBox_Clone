package com.dropbox.main.service;

import com.dropbox.main.model.File;
import com.dropbox.main.model.Notification;
import com.dropbox.main.model.OwnerGuest;
import com.dropbox.main.model.User;
import com.dropbox.main.repository.FileRepository;
import com.dropbox.main.repository.NotificationRepository;
import com.dropbox.main.repository.OwnerGuestRepository;
import com.dropbox.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OwnerGuestService {

    private final OwnerGuestRepository ownerGuestRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public OwnerGuestService(OwnerGuestRepository ownerGuestRepository,
                             FileRepository fileRepository,
                             UserRepository userRepository,
                             NotificationRepository notificationRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.ownerGuestRepository = ownerGuestRepository;
        this.notificationRepository = notificationRepository
    }

    public void save(int userId, int fileId, int[] guestIds, boolean access) {

        for (int guestId : guestIds) {
            Optional<File> optionalFile = fileRepository.findById(fileId);
            Optional<User> optionalUser = userRepository.findById(guestId);
            File file = optionalFile.get();
            User user = optionalUser.get();
            OwnerGuest ownerGuest = new OwnerGuest();
            ownerGuest.setUserId(userId);
            ownerGuest.setGuestId(guestId);
            ownerGuest.setFileId(fileId);
            ownerGuest.setAccess(access);
            Notification notification = new Notification(fileId,guestId, file.getName(), user.getName(), access);
            notificationRepository.save(notification);
            ownerGuestRepository.save(ownerGuest);
        }
    }

    public List<OwnerGuest> findByGuestId(int id) {
        return ownerGuestRepository.getByGuestId(id);
    }

    public List<OwnerGuest> findByUserId(int id) {
        return ownerGuestRepository.getByUserId(id);
    }

    public List<Notification> getNotificationList(int userId) {
        return notificationRepository.getNotificationByUserId(userId);
    }

    public void updateNotification(int fileId){
        Optional<File> optionalFile = fileRepository.findById(fileId);
        File file = optionalFile.get();
        List<Notification> notifications = notificationRepository.getNotificationByFileId(fileId);
        for(Notification notification : notifications){
            notification.setFileName(file.getName());
            notificationRepository.save(notification);
        }
    }
}