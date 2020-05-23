package io.betgeek.authserver.vo;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class HttpResponse {

	private HttpStatus status;
	private Integer statusCode;
	private String message;
	
	public HttpResponse(HttpStatus status, String message) {
		this.status = status;
		this.statusCode = status.value();
		this.message = message;
	}
	
}
