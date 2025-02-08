package com.springboot.videoStreaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Video {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    @Column(name = "video_id", updatable = false, nullable = false)
	private String videoId;
	private String description;
	private String title;
	private String contentType;
	private String filePath;
	private int viewCount;
	private int likes;
	private int dislikes;
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;
	
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public Video(String videoId, String description, String title, String contentType, String filePath, Course course, int viewCount) {
		super();
		this.videoId = videoId;
		this.description = description;
		this.title = title;
		this.contentType = contentType;
		this.filePath = filePath;
		this.course = course;
		this.viewCount=viewCount;
	}
	public Video() {
		super();
	}
	@Override
	public String toString() {
		return "Video [videoId=" + videoId + ", description=" + description + ", title=" + title + ", contentType="
				+ contentType + ", filePath=" + filePath + "]";
	}
	
	
	
	

}
