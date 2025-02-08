package com.springboot.videoStreaming.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.videoStreaming.model.Video;
import com.springboot.videoStreaming.repository.VideoRepository;
import com.springboot.videoStreaming.service.VideoService;
import com.springboot.videoStreaming.service.exception.VideoNotFoundException;

import jakarta.annotation.PostConstruct;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepo;

    @Value("${files.video}")
    private String DIR;

    // Constructor
    public VideoServiceImpl(VideoRepository videoRepo) {
        this.videoRepo = videoRepo;
    }

    // The @PostConstruct annotation ensures that the directory is created when the bean is initialized.
    @PostConstruct
    public void init() {
        try {
            // Create directories if they don't exist
            Files.createDirectories(Paths.get(DIR));
            System.out.println("Folder is ready for use: " + DIR);
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }

    @Override
    public Video saveVideo(Video video, MultipartFile file) {
        try {
            // Get original file name and clean it
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new RuntimeException("Invalid file: File name is empty.");
            }
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();

            // Clean file name and generate 
            //the file path Cleans the file name using StringUtils.cleanPath() to remove any unwanted characters
            String cleanFile = StringUtils.cleanPath(originalFilename);
            if (cleanFile.contains("..")) {
                throw new RuntimeException("Invalid file path: " + cleanFile);
            }
            Path filePath = Paths.get(DIR, cleanFile);
            // If file already exists, generate a unique filename
            if (Files.exists(filePath)) {
            	cleanFile = UUID.randomUUID().toString() + "_" + cleanFile;
                filePath = Paths.get(DIR, cleanFile);
               // cleanFile = System.currentTimeMillis() + "_" + cleanFile; // Adding timestamp to avoid collision
                filePath = Paths.get(DIR, cleanFile);
            }

            // Copy file to the target directory
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set video metadata (file path, content type)
            video.setContentType(contentType);
            video.setFilePath(filePath.toString());

            // Save video metadata in the database
            return videoRepo.save(video);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store video file: " + e.getMessage(), e);
        }
    }

    @Override
    public Video getByTitle(String title) {
        Video video = videoRepo.findByTitle(title);
        if (video == null) {
            throw new VideoNotFoundException("Video with title " + title + " not found");
        }
        return video;
    }

    @Override
    public Optional<Video> get(String videoId) {
        return videoRepo.findById(videoId); // Optional returned by JpaRepository
    }

    @Override
    public List<Video> getAll() {
        return videoRepo.findAll();
    }

    @Override
    public List<Video> getVideosByCourseId(String courseId) {
        return videoRepo.findByCourseId(courseId);
    }

	@Override
	public boolean deleteVideo(String videoId) {
		if (videoRepo.existsById(videoId)) { // Check if video exists
	        videoRepo.deleteById(videoId);
	        return true; // Return true if deletion was successful
	    }
		return false;
	}

	@Override
	public List<Video> findByTitleContaining(String title) {
		return videoRepo.findByTitleContainingIgnoreCase(title);
	}


	public List<Video> searchVideos(String query) {
		return videoRepo.searchByTitleOrDescription(query);
	}

	@Override
	public Video save(Video video) {
		// TODO Auto-generated method stub
		return videoRepo.save(video);
	}
	@Override
	public Page<Video> searchVideosUsingPagination(String title, String description, Pageable pageable) {
	    return videoRepo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title, description, pageable);
	}
//or
//	@Override
//	public Page<Video> searchVideosUsingPagination(String title, String description, int page, int size) {
//	    Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
//	    return videoRepo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title, description, pageable);
//	}
	
//	@Override
//	 @Transactional
//	public boolean likeVideo(String videoId) {
//		        int updatedRows = videoRepo.incrementLikes(videoId);
//		        return updatedRows>0; // Returns true if the update was successful
//		    }
		


}
