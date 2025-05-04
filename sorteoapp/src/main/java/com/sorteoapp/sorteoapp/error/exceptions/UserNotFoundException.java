package com.sorteoapp.sorteoapp.error.exceptions;

public class UserNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long id) {
        super("No se ha encontrado ningún usuario con el ID: " + id);
    }
}
