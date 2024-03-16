package com.vijay.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BlogAPIException extends RuntimeException{
	
	private HttpStatus status;
	private String message;
	public BlogAPIException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
