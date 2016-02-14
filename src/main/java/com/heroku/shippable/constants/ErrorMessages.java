package com.heroku.shippable.constants;

public enum ErrorMessages {
	UNREACHEBLE("SHP00001","Git Unreachable"),
	TIME_OUT("SHP00002","Git Request timeout"),
	PROVISION_FAILED("SHP00004","Unable to Connect"),
	INVALID_REPO_URL("SHP00005","Invalid Repository Url"),
	UNEXPECTED_ERROR("SHP00006","Unable to process request.");

	/**
	 * @param errorCode
	 * @param errorMessage
	 */
	private ErrorMessages(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	private String errorCode;
	private String errorMessage;

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

}
