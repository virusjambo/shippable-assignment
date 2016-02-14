package com.heroku.shippable.exception;

import com.heroku.shippable.constants.ErrorMessages;

/**
 * @author Amit
 *  Defines custom exception used across app.
 */
public class ShippableException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ShippableException() {
		// TODO Auto-generated constructor stub
	}
	
	private ErrorMessages errorCode;

	public ShippableException(ErrorMessages errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}
	
	public ErrorMessages getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorMessages errorCode) {
		this.errorCode = errorCode;
	}
}
