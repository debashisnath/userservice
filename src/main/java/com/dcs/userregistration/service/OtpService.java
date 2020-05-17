/**
 * 
 */
package com.dcs.userregistration.service;

/**
 * @author Debashis
 *
 */
public interface OtpService {

	/**
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public int generateOTP(String key) throws Exception;
	
	/**
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public int getOTP(String key) throws Exception;
	
	/**
	 * @param key
	 * @throws Exception
	 */
	public void clearOTP(String key) throws Exception;
}
