package com.funnel.keep.serverManager;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class BaseManager {
	protected static RestTemplate _manager;

	public BaseManager() {
		_manager = new RestTemplate();
		SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) _manager
				.getRequestFactory();
		rf.setReadTimeout(60 * 1000);
		rf.setConnectTimeout(60 * 1000);
		_manager.getMessageConverters().add(
				new MappingJacksonHttpMessageConverter());

	}

	protected HttpHeaders getRequestHeaders() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(new MediaType("application", "json"));
		MediaType[] medias = { MediaType.APPLICATION_JSON };
		List<MediaType> acceptableMediaTypes = Arrays.asList(medias);
		requestHeaders.setAccept(acceptableMediaTypes);

		return requestHeaders;
	}

}
