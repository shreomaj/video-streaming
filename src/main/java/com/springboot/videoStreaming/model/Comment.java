package com.springboot.videoStreaming.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String commentId;
    
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
    
    private String userId;
    private String text;
    
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public Video getVideo() {
		return video;
	}
	public void setVideo(Video video) {
		this.video = video;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Comment(String commentId, Video video, String userId, String text) {
		super();
		this.commentId = commentId;
		this.video = video;
		this.userId = userId;
		this.text = text;
	}
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", video=" + video + ", userId=" + userId + ", text=" + text + "]";
	}
    
    
}
