package com.springboot.videoStreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.videoStreaming.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

}
