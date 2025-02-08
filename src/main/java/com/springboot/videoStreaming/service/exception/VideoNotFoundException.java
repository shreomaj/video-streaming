package com.springboot.videoStreaming.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springboot.videoStreaming.payload.CustomMessage;

public class VideoNotFoundException extends RuntimeException {
    public VideoNotFoundException(String message) {
        super(message);
    }
//    @ExceptionHandler(VideoNotFoundException.class)
//    public ResponseEntity<?> handleVideoNotFoundException(VideoNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomMessage( false,ex.getMessage()));
//    }

}