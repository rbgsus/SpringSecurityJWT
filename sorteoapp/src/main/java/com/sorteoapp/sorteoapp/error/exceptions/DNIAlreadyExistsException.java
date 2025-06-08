package com.sorteoapp.sorteoapp.error.exceptions;

public class DNIAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DNIAlreadyExistsException() {
		super("El email ya existe");
	}

	public DNIAlreadyExistsException(String mensaje) {
		super(mensaje);
	}
}
