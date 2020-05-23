package io.betgeek.authserver.exception;

public class RedirecException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6288664331826723090L;
	private final String errorCode;
	private final String errorMessage;

	/**
	 * @param errorCode
	 * @param errorMessage
	 */
	public RedirecException(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public RedirecException(String errorMessage) {
		super();
		this.errorCode = "";
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
