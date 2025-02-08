package com.springboot.videoStreaming.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String title;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Course(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	public Course() {
		super();
	}
	
//	@OneToMany(mappedBy="course")
//	private List<Video> list=new ArrayList<>();
//Use cascade = CascadeType.ALL and orphanRemoval = true for proper lifecycle management.
//If you delete a Course, its associated Video entities will remain in the database, causing orphaned records.
//If you modify the videos list (e.g., adding or removing videos), those changes won't be automatically updated in the database.-> cascade.ALL
//Cascade ALL → Automatically persists, updates, and deletes associated Video records.
//Orphan Removal → Ensures that when a Video is removed from the list, it gets deleted from the database.
//If you need to reuse the Video objects for multiple Course entities (e.g., shared videos), If you don’t want automatic deletion of videos when removed from the list.then avoid orphan=true
	 @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Video> videos = new ArrayList<>();
	

	
	
}
