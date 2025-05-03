package com.sorteoapp.sorteoapp.error.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
	public UsernameAlreadyExistsException(String message) {
		super(message);
	}
}
