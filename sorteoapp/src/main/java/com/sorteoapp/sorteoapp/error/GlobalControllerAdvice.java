package com.sorteoapp.sorteoapp.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sorteoapp.sorteoapp.error.exceptions.NewUserWithDifferentPasswordsException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

	// Manejo de la excepción para cuando un usuario no tiene las credenciales
	// correctas
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationCredentialsNotFoundException ex) {
		return buildErrorResponseEntity(HttpStatus.UNAUTHORIZED, "Credenciales de autenticación no encontradas.");
	}

	// Manejo de la excepción para cuando el usuario no tiene permiso para acceder a
	// un recurso
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
		return buildErrorResponseEntity(HttpStatus.FORBIDDEN, "Acceso denegado.");
	}

	// Manejo de una excepción personalizada para usuarios con contraseñas
	// diferentes
	@ExceptionHandler(NewUserWithDifferentPasswordsException.class)
	public ResponseEntity<ApiError> handleNewUserErrors(NewUserWithDifferentPasswordsException ex) {
		return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	// Manejo de la excepción personalizada cuando el nombre de usuario ya está en
	// uso
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
		return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	// Sobrescribe el método para manejar excepciones internas
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {

		HttpStatus status = HttpStatus.resolve(statusCode.value());
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		ApiError apiError = ApiError.builder().estado(status).mensaje(ex.getMessage()).fecha(LocalDateTime.now())
				.build();

		return ResponseEntity.status(status).headers(headers).body(apiError);
	}

	// Método para construir la respuesta de error
	private ResponseEntity<ApiError> buildErrorResponseEntity(HttpStatus status, String message) {
		return ResponseEntity.status(status)
				.body(ApiError.builder().estado(status).mensaje(message).fecha(LocalDateTime.now()).build());
	}
}
