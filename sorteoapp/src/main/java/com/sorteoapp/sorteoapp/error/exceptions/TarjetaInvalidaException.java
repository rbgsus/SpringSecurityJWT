package com.sorteoapp.sorteoapp.error.exceptions;

public class TarjetaInvalidaException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TarjetaInvalidaException(String message) {
		super(message);
	}

	public TarjetaInvalidaException() {
		super();
	}

}