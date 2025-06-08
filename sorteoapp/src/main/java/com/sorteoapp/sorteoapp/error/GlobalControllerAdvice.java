package com.sorteoapp.sorteoapp.error;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sorteoapp.sorteoapp.error.exceptions.EmailAlreadyExistsException;
import com.sorteoapp.sorteoapp.error.exceptions.NewUserWithDifferentPasswordsException;
import com.sorteoapp.sorteoapp.error.exceptions.UserNotFoundException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;

@RestControllerAdvice
public class GlobalControllerAdvice {

	// Manejo de excepción cuando ya existe un usuario con el mismo email
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
		return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

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

	// Manejo de la excepción personalizada cuando el usuario no es encontrado
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
		return buildErrorResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	// Manejo de errores de validación de campos (Bean Validation)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
		String mensaje = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining("; ")); // Unir todos los mensajes si hay varios errores

		return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, mensaje);
	}

	// Método auxiliar para construir la respuesta de error
	private ResponseEntity<ApiError> buildErrorResponseEntity(HttpStatus status, String message) {
		ApiError apiError = ApiError.builder().estado(status).mensaje(message).fecha(LocalDateTime.now()).build();

		return ResponseEntity.status(status).body(apiError);
	}
}
