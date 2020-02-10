/**
 * 
 */
package com.dcs.userregistration.service;

/**
 * @author Debashis
 *
 */
public interface OtpService {

	public int generateOTP(String key) throws Exception;
	
	public int getOTP(String key) throws Exception;
	
	public void clearOTP(String key) throws Exception;
}
