package com.dropbox.main.repository;

import com.dropbox.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select u.id from users u where u.email in (?1)", nativeQuery = true)
    public int[] findIdByEmail(Set<String> emails);

    @Query(value = "SELECT u FROM User u WHERE u.email=?1")
    public User findUserByEmail(String email);
}