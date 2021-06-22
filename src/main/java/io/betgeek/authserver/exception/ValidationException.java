package io.betgeek.authserver.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -6224301859755055858L;

	public ValidationException(String errorMessage) {
		super(errorMessage);
	}

}
