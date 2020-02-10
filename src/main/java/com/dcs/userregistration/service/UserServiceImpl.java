/**
 * 
 */
package com.dcs.userregistration.service;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dcs.userregistration.exception.ProfileAlreadyExistsException;
import com.dcs.userregistration.exception.UserDoesNotExistException;
import com.dcs.userregistration.exception.UserIdAndPasswordMismatchException;
import com.dcs.userregistration.exception.UserProfileDoesNotApprovedException;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.model.User;
import com.dcs.userregistration.repository.UserRepository;
import com.dcs.userregistration.util.EmailTemplate;
import com.dcs.userregistration.util.ProfileStatus;

/**
 * @author Debashis
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepo;
	private OtpService otpService;
	private MyEmailService myEmailService;
	private User userProfile = null;
	
	public UserServiceImpl(UserRepository userRepository, MyEmailService myEmailService, OtpService otpService) {
		this.userRepo = userRepository;
		this.otpService = otpService;
		this.myEmailService = myEmailService;
	}

	@Override
	public String saveUser(User user) throws ProfileAlreadyExistsException {
		if(userRepo.findByEmailId(user.getEmailId()) == null) {
			user.setProfileStatus(ProfileStatus.Pending);
			try {
				sendOtpToEmail(user.getEmailId());
			}
			catch (Exception e) {
				return e.getMessage();
			}
			userProfile = userRepo.save(user);
			return "Profile Successfully Saved";
		}
		else
			
		   throw  new ProfileAlreadyExistsException("Profile Already Exists");
	}

	private void sendOtpToEmail(String emailId) throws Exception {
		int otp = otpService.generateOTP(emailId);
		EmailTemplate emailTemplate = new EmailTemplate("SendOtp.html");
		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("user", emailId);
		replacements.put("otpnum", String.valueOf(otp));
		 
		String message = emailTemplate.getTemplate(replacements);
		myEmailService.sendOtpMessage(emailId, "OTP message", message);
	}

	@Override
	public RetrievedUser getUserProfile(String emailId) throws UserDoesNotExistException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> findByEmailIdAndPassword(String emailId, String password)
			throws UserDoesNotExistException, UserIdAndPasswordMismatchException, UserProfileDoesNotApprovedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forgetPassword(String emailId) throws MessagingException, UserDoesNotExistException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String updateProfileStatus(String emailId, int otp) throws UserDoesNotExistException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
