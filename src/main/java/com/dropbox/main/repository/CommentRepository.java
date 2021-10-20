package com.dropbox.main.repository;

import com.dropbox.main.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Integer> {

    @Query(value = "select * from comments c where c.file_id = ?1",nativeQuery = true)
    public List<Comment> findCommentByFileId(int fileId);
}
