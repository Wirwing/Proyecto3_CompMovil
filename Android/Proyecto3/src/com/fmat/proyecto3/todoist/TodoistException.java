package com.fmat.proyecto3.todoist;

public class TodoistException extends RuntimeException {

	public TodoistException() {

	}

	public TodoistException(String message, Throwable cause) {
		super(message, cause);
	}

	public TodoistException(String message) {
		super(message);
	}

	public TodoistException(Throwable cause) {
		super(cause);
	}

}