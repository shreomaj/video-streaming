package com.springboot.videoStreaming.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.videoStreaming.model.Comment;
import com.springboot.videoStreaming.model.Course;
import com.springboot.videoStreaming.model.Video;
import com.springboot.videoStreaming.payload.CustomMessage;
import com.springboot.videoStreaming.repository.CommentRepository;
import com.springboot.videoStreaming.service.CourseService;
import com.springboot.videoStreaming.service.VideoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@CrossOrigin("*")
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CommentRepository commentRepository;
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    //create a new video
    @PostMapping("/save-video")
    private ResponseEntity<?> create(@RequestParam("file") MultipartFile file,
                                     @RequestParam("title") String title,
                                     @RequestParam("description") String description,
                                     @RequestParam("courseId") String courseId) {
    	 Course course = courseService.getCourseById(courseId);
    	    if (course == null) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
    	                .body(new CustomMessage.Builder()
    	                        .message("Course not found with ID: " + courseId)
    	                        .success(false)
    	                        .build());
    	    }
        Video video = new Video();
        video.setDescription(description);
        video.setTitle(title);
        video.setVideoId(UUID.randomUUID().toString());
        video.setCourse(course);

        try {
            // Save video and associated file
            Video savedVideo = videoService.saveVideo(video, file);

            if (savedVideo != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new CustomMessage.Builder()
                        .message("Video uploaded successfully.")
                        .success(true)
                        .data(savedVideo)
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomMessage.Builder()
                        .message("Video not uploaded")
                        .success(false)
                        .build());
            }
        } catch (Exception e) {
            // Catch any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomMessage.Builder()
                    .message("An error occurred: " + e.getMessage())
                    .success(false)
                    .build());
        }
    }
    //get a video by its title
    @GetMapping("/title/{title}")
    public ResponseEntity<?> getByTitle(@PathVariable("title") String title) {
        Video video = videoService.getByTitle(title);
        if (video != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CustomMessage.Builder()
                            .message("Video found.")
                            .success(true)
                            .data(video)
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage.Builder()
                            .message("Video not found with title: " + title)
                            .success(false)
                            .build());
        }
    }

    // Endpoint to get a video by its ID
    @GetMapping("/{videoId}")
    public ResponseEntity<?> getById(@PathVariable("videoId") String videoId) {
        Optional<Video> videoOpt = videoService.get(videoId);
        if (videoOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CustomMessage.Builder()
                            .message("Video found.")
                            .success(true)
                            .data(videoOpt.get())
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage.Builder()
                            .message("Video not found with ID: " + videoId)
                            .success(false)
                            .build());
        }
    }

    // Endpoint to get all videos
    @GetMapping
    public ResponseEntity<?> getAllVideos() {
        List<Video> videos = videoService.getAll();
        if (!videos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CustomMessage.Builder()
                            .message("Videos retrieved successfully.")
                            .success(true)
                            .data(videos)
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage.Builder()
                            .message("No videos found.")
                            .success(false)
                            .build());
        }
    }

    // Endpoint to get videos by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getVideosByCourseId(@PathVariable("courseId") String courseId) {
        List<Video> videos = videoService.getVideosByCourseId(courseId);
        if (!videos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CustomMessage.Builder()
                            .message("Videos for course retrieved successfully.")
                            .success(true)
                            .data(videos)
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage.Builder()
                            .message("No videos found for course ID: " + courseId)
                            .success(false)
                            .build());
        }
    }
    @PutMapping("/update-video/{videoId}")
    public ResponseEntity<?> updateVideo(@PathVariable String videoId,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description
                                         ,@RequestParam("file") MultipartFile file,
                                         @RequestParam("courseId") String courseId) {
        Video video = videoService.get(videoId).orElse(null);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found.");
        }
        Course course = courseService.getCourseById(courseId);
	    if (course == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new CustomMessage.Builder()
	                        .message("Course not found with ID: " + courseId)
	                        .success(false)
	                        .build());
	    }
    video.setDescription(description);
    video.setTitle(title);
    video.setCourse(course);
    Video updatedVideo = videoService.saveVideo(video, file);
    return ResponseEntity.ok(updatedVideo);
    }
    
    
    @DeleteMapping("/delete-video/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable String videoId) {
        boolean isDeleted = videoService.deleteVideo(videoId);
        
        if (isDeleted) {
            return ResponseEntity.ok("Video deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found.");
        }
    }
// Resource is often used to represent the video file as a streamable resource. It helps serve large files efficiently without loading them completely into memory.
 //Spring provides different types of Resource implementations like:

//FileSystemResource – Loads a file from the system.
//UrlResource – Loads a file from a URL.
//InputStreamResource – Wraps an InputStream, useful for streaming.
    
//    @GetMapping("/stream/{videoId}")
//    public ResponseEntity<Resource> streamVideo(@PathVariable String videoId) {
//        Video video = videoService.get(videoId).orElse(null);
//        		//.getVideoById(videoId);
//        
//        if (video == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        
//        File file = new File(video.getFilePath());
//        Resource resource = new FileSystemResource(file);
//        
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(video.getContentType()))
//                .body(resource);
//    }
    @GetMapping("/stream/{fileName}")
    public ResponseEntity<Resource> streamVideo(@PathVariable String fileName) throws IOException {
        Path videoPath = Paths.get("videos/" + fileName); // Path to the video file
        Resource resource = new FileSystemResource(videoPath.toFile()); // Load video as a Resource

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4")) // Set content type
                .body(resource); // Return the video resource
    }

    @PostMapping("/increment-view/{videoId}")
    public ResponseEntity<?> incrementViewCount(@PathVariable String videoId) {
        //Video video = videoService.get(videoId).orElse(null);
    	Optional<Video> optionalVideo = videoService.get(videoId);
        if (optionalVideo.isEmpty() ) {
           // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage.Builder()
                            .message("Video not found.")
                            .success(false)
                            .build());
        }
        Video video = optionalVideo.get();
        video.setViewCount(video.getViewCount() + 1);
        videoService.save(video);
        
        return ResponseEntity.ok(new CustomMessage.Builder()
                .message("View count updated successfully.")
                .success(true)
                .data(video)
                .build());
       // return ResponseEntity.ok("View count updated.");
    }
    
    @PostMapping("/add-comment/{videoId}")
    public ResponseEntity<?> addComment(@PathVariable String videoId, @RequestParam String userId, @RequestParam String text) {
        Comment comment = new Comment();
        comment.setVideo(videoService.get(videoId).orElse(null));
        		//.getVideoById(videoId));
        comment.setUserId(userId);
        comment.setText(text);
        commentRepository.save(comment);    
        return ResponseEntity.ok("Comment added successfully.");
    }
//http://localhost:8182/search?title=manali
    @GetMapping("/search")
    public ResponseEntity<?> searchVideos(@RequestParam("title") String title) {
        List<Video> videos = videoService.findByTitleContaining(title);
        if (!videos.isEmpty()) {
            return ResponseEntity.ok(new CustomMessage.Builder()
                    .message("Videos retrieved successfully.")
                    .success(true)
                    .data(videos)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage.Builder()
                            .message("No videos found for title: " + title)
                            .success(false)
                            .build());
        }
    }
    @GetMapping("/search/both")
    public ResponseEntity<?> searchVideosForboth(@RequestParam("query") String query) {
    	 List<Video> videos = videoService.searchVideos(query);
    	 if (!videos.isEmpty()) {
             return ResponseEntity.ok(new CustomMessage.Builder()
                     .message("Videos retrieved successfully.")
                     .success(true)
                     .data(videos)
                     .build());
         } else {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(new CustomMessage.Builder()
                             .message("No videos found for for query: " + query)
                             .success(false)
                             .build());
         }
    	 
    
    }
    
    //In Spring Data JPA, you don't always need to use @Query when defining methods in a repository
    
//    @GetMapping("/search/paginated")
//    public ResponseEntity<?> searchVideosPaginated(
//            @RequestParam("query") String query,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Page<Video> videos = videoService.searchVideosUsingPagination(query, query, page, size);
//
//        if (videos.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No videos found.");
//        }
//
//        return ResponseEntity.ok(videos);
//    }
    
    //GET http://localhost:8080/search/paginated?title=Java&description=Spring&page=0&size=5&sortBy=title&sortDirection=desc
    //Allows searching for videos by title and description with pagination and sorting.
    //page – Page number (default: 0 → first page).
    //size – Number of videos per page (default: 10).
    //sortBy – The field to sort results by (default: title).
    //sortDirection – Sorting order: ascending (asc) or descending (desc)
    //GET http://localhost:8080/search/paginated?title=Java&page=0&size=5
    @GetMapping("/search/paginated")
    public ResponseEntity<?> searchVideosPaginated(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);//Creating a Pageable Object

        Page<Video> videos = videoService.searchVideosUsingPagination(title, description, pageable);

        if (videos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No videos found.");
        }

        return ResponseEntity.ok(videos);
    }

    @PostMapping("/like/{videoId}")
    public ResponseEntity<?> likeVideo(@PathVariable String videoId) {
        Video video = videoService.get(videoId).orElse(null);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found.");
        }

        video.setLikes(video.getLikes() + 1);
        videoService.save(video);

        return ResponseEntity.ok("Like added successfully.");
    }
    @PostMapping("/dislikes/{videoId}")
    public ResponseEntity<?> dislikeVideo(@PathVariable String videoId) {
        Video video = videoService.get(videoId).orElse(null);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found.");
        }

        video.setDislikes(video.getDislikes()+ 1);
        videoService.save(video);

        return ResponseEntity.ok("DisLikes added successfully.");
    }
    //Prevent multiple likes/dislikes from the same user
    
    @PostMapping("/comment/{videoId}")
    public ResponseEntity<?> addComment(@PathVariable String videoId, @RequestBody Comment comment) {
        Video video = videoService.get(videoId).orElse(null);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found.");
        }

        comment.setVideo(video);
        commentRepository.save(comment);

        return ResponseEntity.ok("Comment added successfully.");
    }







}
