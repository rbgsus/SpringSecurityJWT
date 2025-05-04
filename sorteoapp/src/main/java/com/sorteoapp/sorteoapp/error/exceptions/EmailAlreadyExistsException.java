package com.sorteoapp.sorteoapp.error.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
		super("El email ya existe");
	}

	public EmailAlreadyExistsException(String mensaje) {
		super(mensaje);
	}
}
