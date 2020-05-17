/**
 * 
 */
package com.dcs.userregistration.controller;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dcs.userregistration.exception.OtpNotFoundException;
import com.dcs.userregistration.exception.OtpNotMatchedException;
import com.dcs.userregistration.exception.ProfileAlreadyExistsException;
import com.dcs.userregistration.exception.UserDoesNotExistException;
import com.dcs.userregistration.exception.UserIdAndPasswordMismatchException;
import com.dcs.userregistration.exception.UserNullException;
import com.dcs.userregistration.exception.UserProfileDoesNotApprovedException;
import com.dcs.userregistration.exception.WrongEmailIdException;
import com.dcs.userregistration.model.CustomHttpResponse;
import com.dcs.userregistration.model.LogInCredentials;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.model.User;
import com.dcs.userregistration.model.ViewProfileRequest;
import com.dcs.userregistration.service.UserService;
import com.dcs.userregistration.util.CommonContants;
import com.dcs.userregistration.util.CommonUtil;

/**
 * @author Debashis
 *
 */
@CrossOrigin(origins = "*")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	/**
	 * @param userService
	 */
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * @param user
	 * @return
	 * @throws ProfileAlreadyExistsException
	 */
	@PostMapping("/api/user/register")
	public ResponseEntity<?> saveUserProfile(@RequestBody User user){

		try {
			String message = userService.saveUser(user);
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse(message, HttpStatus.OK), HttpStatus.OK);
		}
		catch(ProfileAlreadyExistsException e){
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse(CommonContants.PROFILE_EXISTS, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
		}
		catch(WrongEmailIdException e) {
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse(CommonContants.WRONG_EMAILID, HttpStatus.NOT_ACCEPTABLE), HttpStatus.CONFLICT);
		}
	}
	
	/**
	 * @param otpValue
	 * @param emailId
	 * @return
	 */
	@PatchMapping("/api/user/{emailId}/{otpValue}")
	public ResponseEntity<?> updateProfileStatus(@PathVariable("otpValue") String otpValue,
			@PathVariable("emailId") String emailId){
		int otp = Integer.parseInt(otpValue);
		try {
			String message = userService.updateProfileStatus(emailId, otp);
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse(message, HttpStatus.OK), HttpStatus.OK);
		}
		catch(UserDoesNotExistException e) {
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse("Profile Doesn't Exist", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
		} catch (OtpNotFoundException | OtpNotMatchedException e) {
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	/**
	 * @param logInCredentials
	 * @return
	 */
	@PostMapping("/api/user/logIn")
	public ResponseEntity<?> logInUser(@RequestBody LogInCredentials logInCredentials) {
		try {
			Map<String, String> map = userService.authenticateUser(logInCredentials);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (UserDoesNotExistException | 
				UserProfileDoesNotApprovedException exception) {
			String message = "{ \"message\":\"" + exception.getMessage() + "\"}";
			return new ResponseEntity<>(CommonUtil.createHttpResponse(message, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
		}catch(UserIdAndPasswordMismatchException exception) {
			String message = "{ \"message\":\"" + exception.getMessage() + "\"}";
			return new ResponseEntity<>(CommonUtil.createHttpResponse(message, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		

	}
	
	/**
	 * @param emailId
	 * @return
	 */
	@PatchMapping("/api/user/forgotpwd/{emailId}")
	public ResponseEntity<?> forgotPassword(@PathVariable("emailId") String emailId){
		try {
			userService.forgetPassword(emailId);
			return new ResponseEntity<CustomHttpResponse>(CommonUtil.createHttpResponse("Password has been sent to registered emailId", HttpStatus.OK), HttpStatus.OK);
		}catch(MessagingException | UserDoesNotExistException 
				| UserProfileDoesNotApprovedException | UserNullException exception){
			String message = "{ \"message\":\"" + exception.getMessage() + "\"}";
			return new ResponseEntity<>(CommonUtil.createHttpResponse(message, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
	}
	
	/**
	 * @param emailId
	 * @return
	 */
	@PostMapping("/api/user/view")
	public ResponseEntity<?> getUserProfile(@RequestBody ViewProfileRequest viewProfileRequest){
		try {
			RetrievedUser retrievedUser = userService.getUserProfile(viewProfileRequest.getEmailId());
			return new ResponseEntity<>(retrievedUser, HttpStatus.OK);
		}catch(UserDoesNotExistException | UserNullException exception) {
			String message = "{ \"message\":\"" + exception.getMessage() + "\"}";
			return new ResponseEntity<>(CommonUtil.createHttpResponse(message, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
	}
}
