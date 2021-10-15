package com.dropbox.main.service;

import com.dropbox.main.model.OwnerGuest;
import com.dropbox.main.repository.OwnerGuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OwnerGuestService {

    private final OwnerGuestRepository ownerGuestRepository;

    @Autowired
    public OwnerGuestService(OwnerGuestRepository ownerGuestRepository) {
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