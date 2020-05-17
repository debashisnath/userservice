/**
 * 
 */
package com.dcs.userregistration.adapter.response;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

/**
 * @author DebashisNath
 *
 */
public class HttpResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1758062795803482544L;
	private String message;
	private HttpStatus status;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HttpResponse [message=" + message + ", status=" + status + "]";
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the status
	 */
	public HttpStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
}
