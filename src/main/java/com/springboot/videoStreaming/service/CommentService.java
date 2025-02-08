package com.springboot.videoStreaming.service;

import java.util.List;

import com.springboot.videoStreaming.model.Comment;

public interface CommentService {
	//Retrieve all comments for a video.
	List<Comment> findCommentByVideoId(String videoId);
	//Implement comment deletion.
	void deletebyCommentIdofVedio(String CommentId, String videId);

}
