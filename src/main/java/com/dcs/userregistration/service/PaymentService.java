/**
 * 
 */
package com.dcs.userregistration.service;

import com.dcs.userregistration.exception.UserCacheNotFoundException;
import com.dcs.userregistration.model.PaymentCallback;
import com.dcs.userregistration.model.PaymentDetail;
import com.dcs.userregistration.model.RetrievedUser;

/**
 * @author DebashisNath
 *
 */
public interface PaymentService {

	public PaymentDetail proceedPayment(PaymentDetail paymentDetail) ;
	
	public String payuCallback(PaymentCallback paymentResponse);
	
	/**
	 * @param emailId
	 * @param phoneNumber 
	 * @return
	 * @throws UserCacheNotFoundException
	 */
	public RetrievedUser getPaymentStatus(String phoneNumber, String emailId) throws UserCacheNotFoundException;
}
