/**
 * 
 */
package com.dcs.userregistration.adapter.request;

import java.io.Serializable;

/**
 * @author DebashisNath
 *
 */
public class AuthHeader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -835148584261694866L;
	private String username;
	private String password;
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
