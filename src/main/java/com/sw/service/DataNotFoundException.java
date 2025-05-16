package com.sw.service;

public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException() {}
	public DataNotFoundException(String message) {
		super(message);
	}
}
