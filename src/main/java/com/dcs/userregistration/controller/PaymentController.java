/**
 * 
 */
package com.dcs.userregistration.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dcs.userregistration.exception.UserCacheNotFoundException;
import com.dcs.userregistration.model.PaymentCallback;
import com.dcs.userregistration.model.PaymentDetail;
import com.dcs.userregistration.model.RetrievedUser;
import com.dcs.userregistration.service.PaymentService;
import com.dcs.userregistration.util.JSONUtil;
import com.dcs.userregistration.util.PaymentMode;

/**
 * @author DebashisNath
 *
 */
@RestController
@CrossOrigin(origins="*")
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
    private PaymentService paymentService;
	
	private static final Logger logger = LogManager.getLogger(PaymentController.class);
	

    /**
     * @param paymentDetail
     * @return
     */
    @PostMapping(path = "/payment-details")
    public PaymentDetail  proceedPayment(@RequestBody PaymentDetail paymentDetail){
    	logger.info("/PaymentOrder Request: "+JSONUtil.convertObjectToJSON(paymentDetail));
    	PaymentDetail processedPaymentDetail =  paymentService.proceedPayment(paymentDetail);
    	logger.info("/PaymentOrder Response: "+JSONUtil.convertObjectToJSON(processedPaymentDetail));
    	return processedPaymentDetail;
    }

    /**
     * @param mihpayid
     * @param status
     * @param mode
     * @param txnid
     * @param hash
     * @return
     */
    @RequestMapping(path = "/payment-response", method = RequestMethod.POST)
    public @ResponseBody String payuCallback(@RequestParam String mihpayid, @RequestParam String status, @RequestParam PaymentMode mode, @RequestParam String txnid, @RequestParam String hash){
        PaymentCallback paymentCallback = new PaymentCallback();
        paymentCallback.setMihpayid(mihpayid);
        paymentCallback.setTxnid(txnid);
        paymentCallback.setMode(mode);
        paymentCallback.setHash(hash);
        paymentCallback.setStatus(status);
        logger.info("/Process Payment Request: "+JSONUtil.convertObjectToJSON(paymentCallback));
        String response = paymentService.payuCallback(paymentCallback);
        logger.info("/Process Payment Response: "+response);
        return response;
    }
    
    @GetMapping("/payment-status/{emailId}/{phoneNumber}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable (value = "emailId") String emailId, @PathVariable (value = "phoneNumber") String phoneNumber ){
    	try {
    		RetrievedUser retrievedUser = paymentService.getPaymentStatus(phoneNumber, emailId);
    		return new ResponseEntity<>(retrievedUser, HttpStatus.OK);
    	}catch(UserCacheNotFoundException e) {
    		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    	}
    }
    
}
