/**
 * 
 */
package com.dcs.userregistration.model;

import org.springframework.http.HttpStatus;

/**
 * @author DebashisNath
 *
 */
public class CustomHttpResponse {
	
	private String message;
	private HttpStatus status;
	
	
	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return
	 */
	public HttpStatus getStatus() {
		return status;
	}
	/**
	 * @param ok
	 */
	public void setStatus(HttpStatus ok) {
		this.status = ok;
	}
	
	
}
