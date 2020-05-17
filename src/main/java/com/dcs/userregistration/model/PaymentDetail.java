/**
 * 
 */
package com.dcs.userregistration.model;

/**
 * @author DebashisNath
 *
 */
public class PaymentDetail {

	private String email;
    private String name;
    private String phone;
    private String productInfo;
    private String amount;
    private String txnId;
    private String hash;
    private String sUrl;
    private String fUrl;
    private String key;
    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaymentDetail [email=" + email + ", name=" + name + ", phone=" + phone + ", productInfo=" + productInfo
				+ ", amount=" + amount + ", txnId=" + txnId + ", hash=" + hash + ", sUrl=" + sUrl + ", fUrl=" + fUrl
				+ ", key=" + key + "]";
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the productInfo
	 */
	public String getProductInfo() {
		return productInfo;
	}
	/**
	 * @param productInfo the productInfo to set
	 */
	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return the txnId
	 */
	public String getTxnId() {
		return txnId;
	}
	/**
	 * @param txnId the txnId to set
	 */
	public void setTxnId(String txnId) {
		this.txnId = txnId;
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
	/**
	 * @return the sUrl
	 */
	public String getsUrl() {
		return sUrl;
	}
	/**
	 * @param sUrl the sUrl to set
	 */
	public void setsUrl(String sUrl) {
		this.sUrl = sUrl;
	}
	/**
	 * @return the fUrl
	 */
	public String getfUrl() {
		return fUrl;
	}
	/**
	 * @param fUrl the fUrl to set
	 */
	public void setfUrl(String fUrl) {
		this.fUrl = fUrl;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
