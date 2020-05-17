/**
 * 
 */
package com.dcs.userregistration.service;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.dcs.userregistration.adapter.request.AuthHeader;
import com.dcs.userregistration.adapter.request.BusinessDetailsRequest;
import com.dcs.userregistration.adapter.request.ProfileDetails;
import com.dcs.userregistration.adapter.response.HttpResponse;
import com.dcs.userregistration.exception.UserCacheNotFoundException;
import com.dcs.userregistration.exception.UserDoesNotExistException;
import com.dcs.userregistration.exception.UserNullException;
import com.dcs.userregistration.model.Payment;
import com.dcs.userregistration.model.PaymentCallback;
import com.dcs.userregistration.model.PaymentDetail;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.repository.PaymentRepository;
import com.dcs.userregistration.util.CommonContants;
import com.dcs.userregistration.util.CommonUtil;
import com.dcs.userregistration.util.JSONUtil;
import com.dcs.userregistration.util.PaymentStatus;
import com.dcs.userregistration.util.PaymentStatusType;
import com.dcs.userregistration.util.PaymentUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author DebashisNath
 *
 */
@Service
public class PaymentServiceImpl implements PaymentService {
	 private static final Logger logger = LogManager.getLogger(PaymentService.class);
	
	 private static final String BUSINESS_USERNAME = "business_impl";
	 private static final String BUSINESS_PASSWORD = "business_impl123";
	 private static final Integer EXPIRE_MINS = 20;
	 private LoadingCache<String, RetrievedUser> userProfileCache;
	 private PaymentRepository paymentRepository;
	 private UserService userService;
	 private RestTemplate restTemplate;
	 private String serviceUrl;
	 /**
	 * @param paymentRepository
	 * @param userService
	 */
	@Autowired
	 PaymentServiceImpl(PaymentRepository paymentRepository, UserService userService, RestTemplate restTemplate ,
			 @Value("${service.url}") String baseUrl){
		 super();
		 this.paymentRepository = paymentRepository;
		 this.userService = userService;
		 this.restTemplate = restTemplate;
		 this.serviceUrl = baseUrl;
		 userProfileCache = CacheBuilder.newBuilder().
			     expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, RetrievedUser>() {
				      public RetrievedUser load(String key) {
						return null;
				              }
				   });
	 }
	 
	    /* (non-Javadoc)
	     * @see com.dcs.userregistration.service.PaymentService#proceedPayment(com.dcs.userregistration.model.PaymentDetail)
	     */
	    @Override
	    public PaymentDetail proceedPayment(PaymentDetail paymentDetail) {
	        paymentDetail = PaymentUtil.populatePaymentDetail(paymentDetail);
	        savePaymentDetail(paymentDetail);
	        try {
	        	RetrievedUser updatedUser = updateUserPaymentInfo(paymentDetail.getEmail(),PaymentStatusType.INITIATED.getCharasteristicType());
	        	cacheUpdatedUser(updatedUser);
	        } catch (UserDoesNotExistException | UserNullException e) {
				e.printStackTrace();
			}
	        return paymentDetail;
	    }

	    /**
	     * @param updatedUser
	     */
	    private void cacheUpdatedUser(RetrievedUser updatedUser) {
			String key = CommonUtil.createKey(CommonContants.KEY_DELIMITER, updatedUser.getPhoneNumber(), updatedUser.getEmailId());
			userProfileCache.put(key, updatedUser);
		}

	    /**
	     * @param email
	     * @param charasteristicType
	     * @return
	     * @throws UserDoesNotExistException
	     * @throws UserNullException
	     */
	    private RetrievedUser updateUserPaymentInfo(String email, String charasteristicType) throws UserDoesNotExistException, UserNullException {
			RetrievedUser retrievedUser = userService.getUserProfile(email);
			retrievedUser.setPaymentStatus(charasteristicType);
			return retrievedUser;
		}

		/* (non-Javadoc)
	     * @see com.dcs.userregistration.service.PaymentService#payuCallback(com.dcs.userregistration.model.PaymentCallback)
	     */
	    @Override
	    public String payuCallback(PaymentCallback paymentResponse) {
	        String msg = "Transaction failed.";
	        Payment payment = paymentRepository.findByTxnId(paymentResponse.getTxnid());
	        if(payment != null) {
	            //TODO validate the hash
	            PaymentStatus paymentStatus = null;
	            if(paymentResponse.getStatus().equals("failure")){
	                paymentStatus = PaymentStatus.Failed;
	                updateUserProfile(payment, PaymentStatusType.FAILED.getCharasteristicType());
	            }else if(paymentResponse.getStatus().equals("success")) {
	                paymentStatus = PaymentStatus.Success;
	                msg = "Transaction success";
	            }
	            payment.setPaymentStatus(paymentStatus);
	            payment.setMihpayId(paymentResponse.getMihpayid());
	            payment.setMode(paymentResponse.getMode());
	            logger.debug("Updated Payment: "+JSONUtil.convertObjectToJSON(payment));
	            paymentRepository.save(payment);
	            if(payment.getPaymentStatus().equals(PaymentStatus.Success)) {
	            	updateUserProfile(payment, PaymentStatusType.SUCCESS.getCharasteristicType());
	            	processOrder(payment);
	            }
	        }
	        return msg;
	    }

	    /**
	     * @param payment
	     */
	    private void processOrder(Payment payment) {
			if(payment.getEmail() != null && payment.getPhone() != null) {
				try {
					BusinessDetailsRequest businessDetailsRequest = new BusinessDetailsRequest();
					RetrievedUser retrievedUser = this.userProfileCache.get(CommonUtil.createKey(CommonContants.KEY_DELIMITER, payment.getPhone(), payment.getEmail()));
					if(retrievedUser != null) {
						mapProfileDetails(businessDetailsRequest, retrievedUser);
					}
					mapAuthHeader(businessDetailsRequest);
					businessDetailsRequest.setPurchaseStatus(PaymentStatusType.COMPLETED.getCharasteristicType());
					businessDetailsRequest.setProduct_Id(payment.getProductInfo());
					handleAdapterRequest(businessDetailsRequest);
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			
		}

		/**
		 * @param businessDetailsRequest
		 */
		private void handleAdapterRequest(BusinessDetailsRequest businessDetailsRequest) {
			logger.debug("ProcessOrder REST Request: "+JSONUtil.convertObjectToJSON(businessDetailsRequest));
			try {
				HttpResponse responseEntity = restTemplate.postForObject(serviceUrl, businessDetailsRequest, HttpResponse.class);
				logger.debug("ProcessOrder REST Response: "+JSONUtil.convertObjectToJSON(responseEntity));
			}catch(HttpClientErrorException e) {
				logger.error("Exception on executing REST API " + e.getResponseBodyAsString());
			}catch(HttpStatusCodeException e) {
				logger.error("Exception on executing REST API " + e.getResponseBodyAsString());
			}catch(RestClientException e) {
				logger.error("Exception on executing REST API " + ((RestClientResponseException) e).getResponseBodyAsString());
			}
			
		
		}

		/**
		 * @param businessDetailsRequest
		 */
		private void mapAuthHeader(BusinessDetailsRequest businessDetailsRequest) {
			AuthHeader authHeader = new AuthHeader();
			authHeader.setUsername(BUSINESS_USERNAME);
			authHeader.setPassword(BUSINESS_PASSWORD);
			businessDetailsRequest.setAuthHeader(authHeader);
		}

		/**
		 * @param businessDetailsRequest
		 * @param retrievedUser
		 */
		private void mapProfileDetails(BusinessDetailsRequest businessDetailsRequest, RetrievedUser retrievedUser) {
			ProfileDetails profileDetails = new ProfileDetails();
			if(retrievedUser.getEmailId() != null) {
				profileDetails.setEmailId(retrievedUser.getEmailId());
			}
			if(retrievedUser.getFirstName() != null) {
				profileDetails.setFirstName(retrievedUser.getFirstName());
			}
			if(retrievedUser.getLastName() != null) {
				profileDetails.setLastName(retrievedUser.getLastName());
			}
			if(retrievedUser.getPaymentStatus() != null) {
				profileDetails.setPaymentStatus(retrievedUser.getPaymentStatus());
			}
			if(retrievedUser.getPhoneNumber() != null) {
				profileDetails.setPhoneNumber(retrievedUser.getPhoneNumber());
			}
			if(retrievedUser.getProfileStatus() != null) {
				profileDetails.setProfileStatus(retrievedUser.getProfileStatus());
			}
			if(retrievedUser.getRegDate() != null) {
				profileDetails.setRegDate(retrievedUser.getRegDate());
			}
			profileDetails.setId(retrievedUser.getId());
			businessDetailsRequest.setProfileDetails(profileDetails);
		}

		/**
	     * @param payment
	     * @param string 
	     */
	    private void updateUserProfile(Payment payment, String string) {
			try {
				RetrievedUser retrievedUser = this.userProfileCache.get(CommonUtil.createKey(CommonContants.KEY_DELIMITER, payment.getPhone(), payment.getEmail()));
				if(retrievedUser != null) {
					retrievedUser.setPaymentStatus(string);
					this.userProfileCache.put(CommonUtil.createKey(CommonContants.KEY_DELIMITER, payment.getPhone(), payment.getEmail()), retrievedUser);
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
		}

		/**
		 * @param paymentDetail
		 */
		private void savePaymentDetail(PaymentDetail paymentDetail) {
	        Payment payment = new Payment();
	        payment.setAmount(Double.parseDouble(paymentDetail.getAmount()));
	        payment.setEmail(paymentDetail.getEmail());
	        payment.setName(paymentDetail.getName());
	        payment.setPaymentDate(new Date());
	        payment.setPaymentStatus(PaymentStatus.Pending);
	        payment.setPhone(paymentDetail.getPhone());
	        payment.setProductInfo(paymentDetail.getProductInfo());
	        payment.setTxnId(paymentDetail.getTxnId());
	        paymentRepository.save(payment);
	    }

		
		/* (non-Javadoc)
		 * @see com.dcs.userregistration.service.PaymentService#getPaymentStatus(java.lang.String, java.lang.String)
		 */
		@Override
		public RetrievedUser getPaymentStatus(String phoneNumber, String emailId) throws UserCacheNotFoundException {
			RetrievedUser retrievedUser = null;
			if(emailId != null) {
				if(validatePaymentCallback(phoneNumber, emailId)) {
					try {
						retrievedUser = this.userProfileCache.get(CommonUtil.createKey(CommonContants.KEY_DELIMITER, phoneNumber, emailId));
					} catch (ExecutionException e) {
						throw new UserCacheNotFoundException("User Cache not found");
					}
				}
			}
			return retrievedUser;
		}

		/**
		 * @param phoneNumber
		 * @param emailId
		 * @return
		 * @throws UserCacheNotFoundException 
		 */
		private boolean validatePaymentCallback(String phoneNumber, String emailId) throws UserCacheNotFoundException {
			RetrievedUser retrievedUser;
			try {
				int retryCount = 0;
				retrievedUser = this.userProfileCache.get(CommonUtil.createKey(CommonContants.KEY_DELIMITER, phoneNumber, emailId));
				while(!isPaymentCallBackSuccess(retrievedUser.getPaymentStatus())) {
					try {
						Thread.sleep(600);
						retrievedUser = this.userProfileCache.get(CommonUtil.createKey(CommonContants.KEY_DELIMITER, phoneNumber, emailId));
					} catch (InterruptedException e) {
						throw new UserCacheNotFoundException("User Cache not found");
					}
					retryCount++;
					if(retryCount >= 500) {
						break;
					}
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			return true;
		}

		/**
		 * @param paymentStatus
		 * @return
		 */
		private boolean isPaymentCallBackSuccess(String paymentStatus) {
			boolean isSuccess = false;
			if(PaymentStatusType.SUCCESS.getCharasteristicType().equalsIgnoreCase(paymentStatus)
					|| PaymentStatusType.FAILED.getCharasteristicType().equalsIgnoreCase(paymentStatus)) {
				isSuccess = true;
			}
			return isSuccess;
		}

}
