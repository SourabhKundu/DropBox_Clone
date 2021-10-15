package com.dropbox.main.service;

import com.dropbox.main.model.User;
import com.dropbox.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public int[] getIdsByEmail(Set<String> emails){
        return userRepository.findIdByEmail(emails);
    }
}
