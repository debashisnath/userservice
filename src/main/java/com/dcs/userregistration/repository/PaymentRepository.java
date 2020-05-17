/**
 * 
 */
package com.dcs.userregistration.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dcs.userregistration.model.Payment;

/**
 * @author DebashisNath
 *
 */
@Repository
public interface PaymentRepository extends CrudRepository<Payment, String> {

    Payment findByTxnId(String txnId);
	
}
