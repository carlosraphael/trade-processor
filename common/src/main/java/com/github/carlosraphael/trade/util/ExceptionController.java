package com.github.carlosraphael.trade.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Simple and generic exception handler. It can be improved by defining custom exceptions in order to send back
 * clear error messages, error code and accurate http status.
 *  
 * @author carlos
 */
@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler({Exception.class, RuntimeException.class})
	public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
		log.error(e.getMessage(), e);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return handleExceptionInternal(e, e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
