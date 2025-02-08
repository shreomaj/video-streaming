package com.springboot.videoStreaming.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.videoStreaming.model.Course;
import com.springboot.videoStreaming.repository.CourseRepository;
import com.springboot.videoStreaming.service.CourseService;
@Service
public class CourseServiceImpl implements CourseService{
	@Autowired
	private CourseRepository courseRepo;

	public Course getCourseById(String courseId) {
		
		return courseRepo.findById(courseId).orElse(null);
	}

}
