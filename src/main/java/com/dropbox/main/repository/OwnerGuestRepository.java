package com.dropbox.main.repository;

import com.dropbox.main.model.OwnerGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerGuestRepository extends JpaRepository<OwnerGuest, Integer> {

    @Query(value = "select * from owner_guest o where o.guestid = ?1",nativeQuery = true)
    public List<OwnerGuest> getByGuestId(int id);
}