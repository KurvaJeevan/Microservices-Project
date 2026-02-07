package com.gc.exception;

public class NotFoundException extends RuntimeException{

	public NotFoundException() {
	}

	public NotFoundException(String msg) {
		super(msg);
	}
	
}
