/**
 * 
 */
package com.dcs.userregistration.service;

import java.util.Map;

import javax.mail.MessagingException;

import com.dcs.userregistration.exception.OtpNotFoundException;
import com.dcs.userregistration.exception.OtpNotMatchedException;
import com.dcs.userregistration.exception.ProfileAlreadyExistsException;
import com.dcs.userregistration.exception.UserDoesNotExistException;
import com.dcs.userregistration.exception.UserIdAndPasswordMismatchException;
import com.dcs.userregistration.exception.UserNullException;
import com.dcs.userregistration.exception.UserProfileDoesNotApprovedException;
import com.dcs.userregistration.exception.WrongEmailIdException;
import com.dcs.userregistration.model.LogInCredentials;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.model.User;

/**
 * @author Debashis
 *
 */
public interface UserService {

	/**
	 * @param user
	 * @return
	 * @throws ProfileAlreadyExistsException
	 * @throws WrongEmailIdException 
	 */
	public String saveUser(User user) throws ProfileAlreadyExistsException, WrongEmailIdException;
	
	/**
	 * @param emailId
	 * @return
	 * @throws UserDoesNotExistException
	 * @throws UserNullException
	 */
	public RetrievedUser getUserProfile(String emailId) throws UserDoesNotExistException, UserNullException;
	
	/**
	 * @param logInCredentials
	 * @return
	 * @throws UserDoesNotExistException
	 * @throws UserIdAndPasswordMismatchException
	 * @throws UserProfileDoesNotApprovedException
	 */
	public Map<String, String> authenticateUser(LogInCredentials logInCredentials) throws UserDoesNotExistException, UserIdAndPasswordMismatchException, UserProfileDoesNotApprovedException;
	
	/**
	 * @param emailId
	 * @throws MessagingException
	 * @throws UserDoesNotExistException
	 * @throws UserNullException 
	 */
	public void forgetPassword(String emailId) throws MessagingException, UserDoesNotExistException, UserProfileDoesNotApprovedException, UserNullException;
	
	/**
	 * @param emailId
	 * @param otp
	 * @return
	 * @throws UserDoesNotExistException
	 * @throws OtpNotFoundException 
	 * @throws OtpNotMatchedException 
	 */
	public String updateProfileStatus(String emailId, int otp) throws UserDoesNotExistException, OtpNotFoundException, OtpNotMatchedException;
}
