/**
 * 
 */
package com.dcs.userregistration.service;

import java.util.Map;

import javax.mail.MessagingException;

import com.dcs.userregistration.exception.ProfileAlreadyExistsException;
import com.dcs.userregistration.exception.UserDoesNotExistException;
import com.dcs.userregistration.exception.UserIdAndPasswordMismatchException;
import com.dcs.userregistration.exception.UserProfileDoesNotApprovedException;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.model.User;

/**
 * @author Debashis
 *
 */
public interface UserService {

	public String saveUser(User user) throws ProfileAlreadyExistsException;
	
	public RetrievedUser getUserProfile(String emailId) throws UserDoesNotExistException;
	
	public Map<String, String> findByEmailIdAndPassword(String emailId, String password) throws UserDoesNotExistException, UserIdAndPasswordMismatchException, UserProfileDoesNotApprovedException;
	
	public void forgetPassword(String emailId) throws MessagingException, UserDoesNotExistException;
	
	public String updateProfileStatus(String emailId, int otp) throws UserDoesNotExistException;
}
