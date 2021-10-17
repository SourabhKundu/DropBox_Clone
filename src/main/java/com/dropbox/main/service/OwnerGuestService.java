package com.dropbox.main.service;

import com.dropbox.main.model.File;
import com.dropbox.main.model.OwnerGuest;
import com.dropbox.main.model.User;
import com.dropbox.main.repository.FileRepository;
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
    @Autowired
    public OwnerGuestService(OwnerGuestRepository ownerGuestRepository,
                             FileRepository fileRepository,
                             UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.ownerGuestRepository = ownerGuestRepository;
    }

    public void save(int userId, int fileId, int[] guestIds, boolean access) {

        for (int guestId : guestIds) {
            OwnerGuest ownerGuest = new OwnerGuest();
            ownerGuest.setUserId(userId);
            ownerGuest.setGuestId(guestId);
            ownerGuest.setFileId(fileId);
            ownerGuest.setAccess(access);
            ownerGuestRepository.save(ownerGuest);
        }
    }

    public List<OwnerGuest> findByGuestId(int id) {
        return ownerGuestRepository.getByGuestId(id);
    }

}