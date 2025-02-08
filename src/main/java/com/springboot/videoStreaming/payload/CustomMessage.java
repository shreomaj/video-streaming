package com.springboot.videoStreaming.payload;

public class CustomMessage {
    private String message;
    private boolean success;
    private Object data;

    // Private constructor to enforce the use of builder
    private CustomMessage(Builder builder) {
        this.message = builder.message;
        this.success = builder.success;
        this.data = builder.data;
    }
//In the Builder Pattern, we often use a private constructor to ensure that object creation happens only through the builder class.
//This enforces controlled instantiation and improves immutability.
//When we use a private constructor, it prevents direct instantiation of the class from outside. This ensures that an object can only be created through the Builder, which improves code consistency and readability.

    

	// Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // Static builder class
    public static class Builder {
        private String message;
        private boolean success;
        private Object data;

        // Builder setter methods
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        // Build method to return the final CustomMessage object
        public CustomMessage build() {
            return new CustomMessage(this);
        }
    }
}
