package com.blog.exception;

public class BlogAPIException extends RuntimeException {
    public BlogAPIException(String message) {
        super(message);
    }
}
