package com.fmat.proyecto3.todoist;

/**
 * Representa una excepci�n de Todoist
 * @author Fabi�n Castillo
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
	 * @param message Mensaje de la excepci�n
	 * @param cause Causa de la excepci�n
	 */
	public TodoistException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor
	 * @param message mensaje de la excepci�n
	 */
	public TodoistException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * @param Causa de la excepci�n
	 */
	public TodoistException(Throwable cause) {
		super(cause);
	}

}