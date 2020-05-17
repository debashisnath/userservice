/**
 * 
 */
package com.dcs.userregistration.security;

import java.util.Map;

import com.dcs.userregistration.model.User;

/**
 * @author DebashisNath
 *
 */
@FunctionalInterface
public interface SecurityTokenGeneratorUser {
	
	/**
	 * @param user
	 * @return
	 */
	Map<String,String> generateTokenForUser(User user);
}
