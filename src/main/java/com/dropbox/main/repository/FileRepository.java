package com.dropbox.main.repository;

import com.dropbox.main.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    @Query(value = "select * from files order by id", nativeQuery = true)
    List<File> findAll();
}