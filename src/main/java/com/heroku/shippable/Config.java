package com.heroku.shippable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * @author Amit 
 * Responsible for global level configuration. Sets read and
 *         connection timeout.
 */
@Configuration
@ComponentScan(basePackages = { "com.heroku.shippable" })
public class Config {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(clientHttpRequestFactory());
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(30000);
		factory.setReadTimeout(60000);
		return factory;
	}

	@Bean
	public ObjectMapper jacksonObjectMapper() {
		return new ObjectMapper()
				.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}
}
