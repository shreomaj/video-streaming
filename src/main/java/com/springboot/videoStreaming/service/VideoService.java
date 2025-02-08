package com.springboot.videoStreaming.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.videoStreaming.model.Video;

public interface VideoService {

    // Save video along with file upload
    Video saveVideo(Video video, MultipartFile file);

    // Get video by title
    Video getByTitle(String title);

    // Get video by ID
    Optional<Video> get(String videoId);

    // Get all videos
    List<Video> getAll();

    // Get videos by Course ID
    List<Video> getVideosByCourseId(String courseId);

	boolean deleteVideo(String videoId);

	List<Video> findByTitleContaining(String title);
	
	List<Video> searchVideos(String query);

	Video save(Video video);
	//Page<Video> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
	//	    String title, String description, Pageable pageable);

	Page<Video> searchVideosUsingPagination(String title, String description, Pageable pageable);

	//boolean  likeVideo(String videoId);

	
}
