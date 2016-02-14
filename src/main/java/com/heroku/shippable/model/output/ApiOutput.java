package com.heroku.shippable.model.output;

import com.heroku.shippable.constants.ErrorMessages;

public class ApiOutput {
	private boolean success = true;
	private String errorCode;
	private String errorMessage;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ApiOutput(ErrorMessages errorMessage) {
		this.success = false;
		this.errorCode = errorMessage.getErrorCode();
		this.errorMessage = errorMessage.getErrorMessage();

	}
	
	public ApiOutput(){};

}
