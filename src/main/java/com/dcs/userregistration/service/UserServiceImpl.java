/**
 * 
 */
package com.dcs.userregistration.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;

import com.dcs.userregistration.exception.OtpNotFoundException;
import com.dcs.userregistration.exception.OtpNotMatchedException;
import com.dcs.userregistration.exception.ProfileAlreadyExistsException;
import com.dcs.userregistration.exception.UserDoesNotExistException;
import com.dcs.userregistration.exception.UserIdAndPasswordMismatchException;
import com.dcs.userregistration.exception.UserNullException;
import com.dcs.userregistration.exception.UserProfileDoesNotApprovedException;
import com.dcs.userregistration.exception.WrongEmailIdException;
import com.dcs.userregistration.model.LogInCredentials;
import com.dcs.userregistration.model.Mail;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.model.User;
import com.dcs.userregistration.repository.UserRepository;
import com.dcs.userregistration.security.SecurityTokenGeneratorUser;
import com.dcs.userregistration.util.CommonContants;
import com.dcs.userregistration.util.PaymentStatusType;
import com.dcs.userregistration.util.ProfileStatus;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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

	/**
	 * @param userRepository
	 * @param myEmailService
	 * @param otpService
	 */
	public UserServiceImpl(UserRepository userRepository, MyEmailService myEmailService, OtpService otpService) {
		this.userRepo = userRepository;
		this.otpService = otpService;
		this.myEmailService = myEmailService;
	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.UserService#saveUser(com.dcs.userregistration.model.User)
	 */
	@Override
	public String saveUser(User user) throws ProfileAlreadyExistsException, WrongEmailIdException {
		if (isProfileCanRegister(user.getEmailId())) {
			user.setProfileStatus(ProfileStatus.Pending);
			user.setPaymentStatus(PaymentStatusType.NOT_COMPLETED.getCharasteristicType());
			try {
				if(isValidEmailId(user.getEmailId()))
					sendOtpToEmail(user);
				else
					throw new WrongEmailIdException(CommonContants.WRONG_EMAILID);
			} catch (Exception e) {
				return CommonContants.FAILED_EMAIL;
			}
			user.setRegDate(new Date());
			userProfile = userRepo.save(user);
			return CommonContants.PROFILE_SAVED;
		} else

			throw new ProfileAlreadyExistsException(CommonContants.PROFILE_EXISTS);
	}

	/**
	 * @param emailId
	 * @return
	 */
	private boolean isValidEmailId(String emailId) {
		boolean isValid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(emailId);
			internetAddress.validate();
			isValid = true;
		}
		catch(AddressException e) {
			return isValid;
		}
		return isValid;
	}

	/**
	 * @param emailId
	 * @return
	 */
	private boolean isProfileCanRegister(String emailId) {
		User user = userRepo.findByEmailId(emailId);
		if(user == null) {
			return true;
		}else if(isProfileSavedWithPendingStatus(user)){
			return true;
		}else
			return false;
	}

	/**
	 * @param user
	 * @return
	 */
	private boolean isProfileSavedWithPendingStatus(User user) {
		return user != null && user.getProfileStatus().toString().equalsIgnoreCase(ProfileStatus.Pending.toString());
	}

	/**
	 * @param user
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void sendOtpToEmail(User user) throws Exception {
		int otpValue = otpService.generateOTP(user.getEmailId());
		Mail mail = new Mail();
		setEmailInformation(user, mail, CommonContants.OTP_SUBJECT);
		Map model = new HashMap<>();
		model.put("name", user.getFirstName());
		model.put("otp", otpValue);
		model.put("location", "Dharmanagar");
		model.put("signature", "https://lifeguard.com");
		mail.setModel(model);
		myEmailService.sendOtpMessage(mail);
	}

	/**
	 * @param user
	 * @param mail
	 * @param otpSubject
	 */
	private void setEmailInformation(User user, Mail mail, String otpSubject) {
		mail.setFrom("lifeguard.accinfo@gmail.com");
		mail.setTo(user.getEmailId());
		mail.setSubject(otpSubject);
	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.UserService#getUserProfile(java.lang.String)
	 */
	@Override
	public RetrievedUser getUserProfile(String emailId) throws UserDoesNotExistException, UserNullException {
		RetrievedUser retrievedUser = null;
		if (emailId != null) {
			userProfile = userRepo.findByEmailId(emailId);
			if (userProfile != null) {
				retrievedUser = new RetrievedUser();
				retrievedUser.setId(userProfile.getId());
				retrievedUser.setEmailId(userProfile.getEmailId());
				retrievedUser.setFirstName(userProfile.getFirstName());
				retrievedUser.setLastName(userProfile.getLastName());
				retrievedUser.setPaymentStatus(userProfile.getPaymentStatus());
				retrievedUser.setPhoneNumber(userProfile.getPhoneNumber());
				retrievedUser.setProfileStatus(userProfile.getProfileStatus().toString());
				retrievedUser.setRegDate(userProfile.getRegDate());
			} else
				throw new UserDoesNotExistException(CommonContants.PROFILE_NOTFOUND);
		} else
			throw new UserNullException(CommonContants.EMAIL_NULL);
		return retrievedUser;

	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.UserService#authenticateUser(com.dcs.userregistration.model.LogInCredentials)
	 */
	@Override
	public Map<String, String> authenticateUser(LogInCredentials logInCredentials)
			throws UserDoesNotExistException, UserIdAndPasswordMismatchException, UserProfileDoesNotApprovedException {
		Map<String, String> authenticatedMap = new HashMap<>();
		User retrivedUserProfile = userRepo.findByEmailId(logInCredentials.getEmailId());
		if (retrivedUserProfile == null) {
			throw new UserDoesNotExistException("User not found");
		}
		if (!logInCredentials.getPassword().equals(retrivedUserProfile.getPassword())) {
			throw new UserIdAndPasswordMismatchException("Invalid Credential, Please check userId and password");
		}
		if(!retrivedUserProfile.getProfileStatus().equals(ProfileStatus.Approved)) {
			throw new UserProfileDoesNotApprovedException("Profile does not approved yet");
		}
		SecurityTokenGeneratorUser securityTokenGeneratorUser = (User userProfile) -> {
			String jwtToken = "";
			jwtToken = Jwts.builder().setId(userProfile.getEmailId()).claim("name", userProfile.getFirstName())
					.setSubject(userProfile.getPhoneNumber()).setIssuedAt(new Date()).setIssuedAt(new Date())
					.signWith(SignatureAlgorithm.HS256, "secretKey").compact();
			Map<String, String> map1 = new HashMap<>();
			map1.put("token", jwtToken);
			map1.put("message", "User Successfully Logged In");
			return map1;
		};

		authenticatedMap = securityTokenGeneratorUser.generateTokenForUser(retrivedUserProfile);
		return authenticatedMap;
	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.UserService#forgetPassword(java.lang.String)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void forgetPassword(String emailId) throws MessagingException, 
					UserDoesNotExistException, UserProfileDoesNotApprovedException, UserNullException {
		if(emailId.isEmpty() ||  emailId == null) {
			userProfile = userRepo.findByEmailId(emailId);
		}else
			throw new UserNullException("EmailId can't be empty");
		if(userProfile != null) {
			Mail mail = new Mail();
			setEmailInformation(userProfile, mail, CommonContants.FORGOT_PWD);
			Map model = new HashMap<>();
			model.put("name", userProfile.getFirstName());
			model.put("pwd", userProfile.getPassword());
			model.put("location", "Dharmanagar");
			model.put("signature", "https://lifeguard.com");
			mail.setModel(model);
			myEmailService.sendOtpMessage(mail);
		}else
			throw new UserDoesNotExistException("User doesn't exist");
	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.UserService#updateProfileStatus(java.lang.String, int)
	 */
	@Override
	public String updateProfileStatus(String emailId, int otp) throws UserDoesNotExistException, OtpNotFoundException, OtpNotMatchedException {
		int retrievedOtp = 0;
		if(emailId != null) {
			try {
				retrievedOtp = otpService.getOTP(emailId);
			} catch (Exception  e ) {
				throw new OtpNotFoundException(CommonContants.OTP_NOT_FOUND);
			}
			if(retrievedOtp == otp) {
				userProfile = userRepo.findByEmailId(emailId);
				if(userProfile!=null) {
					userProfile.setProfileStatus(ProfileStatus.Approved);
					userRepo.save(userProfile);
				}
				return CommonContants.PROFILE_UPDATED;
			}else {
				throw new OtpNotMatchedException(CommonContants.OTP_MISMATCH) ;
			}
			
		}
		return CommonContants.EMAIL_NULL;
	}

}
