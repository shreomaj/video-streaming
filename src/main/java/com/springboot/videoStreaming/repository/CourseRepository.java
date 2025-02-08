package com.springboot.videoStreaming.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.videoStreaming.model.Course;

public interface CourseRepository extends JpaRepository<Course, String>{
	

}
