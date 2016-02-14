package com.heroku.shippable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heroku.shippable.constants.ErrorMessages;
import com.heroku.shippable.exception.ShippableException;
import com.heroku.shippable.model.output.ApiOutput;

/**
 * ShippableException exception handler
 * 
 */
@ControllerAdvice(basePackages = { "com.heroku.shippable" })
public class ShippableExceptionHandler {

	@ExceptionHandler(ShippableException.class)
	// @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ApiOutput handleException(HttpServletRequest request, ShippableException ex) {
		ApiOutput apiOutput = new ApiOutput();
		apiOutput.setSuccess(false);
		ErrorMessages errorCode = ex.getErrorCode();
		if (errorCode != null) {
			apiOutput.setErrorCode(errorCode.getErrorCode());
			apiOutput.setErrorMessage(errorCode.getErrorMessage());
		}
		return apiOutput;
	}
}
