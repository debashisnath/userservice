package com.dcs.userregistration.exception;

public class UserDoesNotExistException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDoesNotExistException (String s) {
		super(s);
	}
}