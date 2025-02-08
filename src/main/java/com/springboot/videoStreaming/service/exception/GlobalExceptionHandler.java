package com.springboot.videoStreaming.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.springboot.videoStreaming.payload.CustomMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Handle VideoNotFoundException
    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<CustomMessage> handleVideoNotFoundException(VideoNotFoundException ex, WebRequest request) {
        CustomMessage response = new CustomMessage.Builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
//WebRequest is an interface in Spring Framework that provides access to HTTP request details. It allows you to get information about the request, such as request parameters, headers, and attributes.
    //If a Video is Not Found, Suppose you request:GET /api/v1/videos/title/UnknownTitle  
    //The VideoNotFoundException is thrown.The error response will include the request path:
    //{
//    "message": "Error: Video not found with title: UnknownTitle | Path: uri=/api/v1/videos/title/UnknownTitle",
//    "success": false
//  }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomMessage> handleGlobalException(Exception ex, WebRequest request) {
        CustomMessage response = new CustomMessage.Builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}