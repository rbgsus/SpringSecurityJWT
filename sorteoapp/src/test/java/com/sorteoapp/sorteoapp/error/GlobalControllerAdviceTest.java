package com.sorteoapp.sorteoapp.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.sorteoapp.sorteoapp.error.exceptions.EmailAlreadyExistsException;
import com.sorteoapp.sorteoapp.error.exceptions.NewUserWithDifferentPasswordsException;
import com.sorteoapp.sorteoapp.error.exceptions.UserNotFoundException;
import com.sorteoapp.sorteoapp.error.exceptions.UsernameAlreadyExistsException;

class GlobalControllerAdviceTest {

	private GlobalControllerAdvice advice;

	@BeforeEach
	void setup() {
		advice = new GlobalControllerAdvice();
	}

	@Test
	void testHandleEmailAlreadyExistsException() {
		EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Correo en uso");
		ResponseEntity<ApiError> res = advice.handleEmailAlreadyExistsException(ex);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals("Correo en uso", res.getBody().getMensaje());
	}

	@Test
	void testHandleUsernameAlreadyExistsException() {
		UsernameAlreadyExistsException ex = new UsernameAlreadyExistsException("Nombre en uso");
		ResponseEntity<ApiError> res = advice.handleUsernameAlreadyExistsException(ex);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals("Nombre en uso", res.getBody().getMensaje());
	}

	@Test
	void testHandleNewUserWithDifferentPasswordsException() {
		NewUserWithDifferentPasswordsException ex = new NewUserWithDifferentPasswordsException("Contraseñas distintas");
		ResponseEntity<ApiError> res = advice.handleNewUserErrors(ex);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertEquals("Contraseñas distintas", res.getBody().getMensaje());
	}

	@Test
	void testHandleUserNotFoundException() {
		UserNotFoundException ex = new UserNotFoundException(1L);
		ResponseEntity<ApiError> res = advice.handleUserNotFoundException(ex);
		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
		assertEquals(ex.getMessage(), res.getBody().getMensaje());
	}

	@Test
	void testHandleAuthenticationException() {
		ResponseEntity<ApiError> res = advice.handleAuthenticationException(
				new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException(""));
		assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
	}

	@Test
	void testHandleAccessDeniedException() {
		ResponseEntity<ApiError> res = advice
				.handleAccessDeniedException(new org.springframework.security.access.AccessDeniedException(""));
		assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
	}

	@Test
	void testHandleValidationException() {
		BindingResult bindingResult = mock(BindingResult.class);
		FieldError fieldError = new FieldError("obj", "campo", "Error de validación");
		when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		when(ex.getBindingResult()).thenReturn(bindingResult);

		ResponseEntity<ApiError> res = advice.handleValidationException(ex);
		assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
		assertTrue(res.getBody().getMensaje().contains("Error de validación"));
	}
}
