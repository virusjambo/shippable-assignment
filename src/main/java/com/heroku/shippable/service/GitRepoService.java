package com.heroku.shippable.service;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.heroku.shippable.constants.ErrorMessages;
import com.heroku.shippable.exception.ShippableException;
import com.heroku.shippable.model.output.ApiOutput;

/**
 * @author Amit
 *  Handles rest request to Git repository.Returns  JSON Output 
 */
@Service
public class GitRepoService {

	@Autowired
	private RestTemplate restTemplate;

	public Object provisionService(String url, Class<?> T) throws ShippableException {

		try {

			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
			ResponseEntity<?> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, T);
			Object output = responseEntity.getBody();
			return output;

		} catch (RestClientException exc) {
			if (exc.getRootCause() instanceof ConnectException) {
				throw new ShippableException(ErrorMessages.UNREACHEBLE);
			}
			if (exc.getRootCause() instanceof SocketTimeoutException) {
				throw new ShippableException(ErrorMessages.TIME_OUT);

			}
			throw new ShippableException(ErrorMessages.PROVISION_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ShippableException(ErrorMessages.UNEXPECTED_ERROR);

		}

	}

}
