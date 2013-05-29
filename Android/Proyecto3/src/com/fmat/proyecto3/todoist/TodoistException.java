package com.fmat.proyecto3.todoist;

/**
 * Representa una excepción de Todoist
 * @author Fabián Castillo
 *
 */
public class TodoistException extends RuntimeException {

	/**
	 * Constructor
	 */
	public TodoistException() {

	}

	/**
	 * Constructor
	 * @param message Mensaje de la excepción
	 * @param cause Causa de la excepción
	 */
	public TodoistException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message mensaje de la excepción
	 */
	public TodoistException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param Causa de la excepción
	 */
	public TodoistException(Throwable cause) {
		super(cause);
	}

}