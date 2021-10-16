package com.dropbox.main.repository;

import com.dropbox.main.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    @Transactional
    @Query(value = "select f.id, f.name from files f", nativeQuery = true)
    List<File> allFiles();
}