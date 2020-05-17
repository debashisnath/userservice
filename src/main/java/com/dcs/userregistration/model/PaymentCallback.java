/**
 * 
 */
package com.dcs.userregistration.model;

import com.dcs.userregistration.util.PaymentMode;

/**
 * @author DebashisNath
 *
 */
public class PaymentCallback {

	private String txnid;
    private String mihpayid;
    private PaymentMode mode;
    private String status;
    private String hash;
	/**
	 * @return the txnid
	 */
	public String getTxnid() {
		return txnid;
	}
	/**
	 * @param txnid the txnid to set
	 */
	public void setTxnid(String txnid) {
		this.txnid = txnid;
	}
	/**
	 * @return the mihpayid
	 */
	public String getMihpayid() {
		return mihpayid;
	}
	/**
	 * @param mihpayid the mihpayid to set
	 */
	public void setMihpayid(String mihpayid) {
		this.mihpayid = mihpayid;
	}
	/**
	 * @return the mode
	 */
	public PaymentMode getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(PaymentMode mode) {
		this.mode = mode;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
    
    
}
