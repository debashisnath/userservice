/**
 * 
 */
package com.dcs.userregistration.adapter.request;

import java.io.Serializable;



/**
 * @author DebashisNath
 *
 */
public class BusinessDetailsRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7204809992414937795L;
	private AuthHeader authHeader;
	private ProfileDetails profileDetails;
	private String purchaseStatus;
	private String product_Id;
	/**
	 * @return the authHeader
	 */
	public AuthHeader getAuthHeader() {
		return authHeader;
	}
	/**
	 * @param authHeader the authHeader to set
	 */
	public void setAuthHeader(AuthHeader authHeader) {
		this.authHeader = authHeader;
	}
	/**
	 * @return the profileDetails
	 */
	public ProfileDetails getProfileDetails() {
		return profileDetails;
	}
	/**
	 * @param profileDetails the profileDetails to set
	 */
	public void setProfileDetails(ProfileDetails profileDetails) {
		this.profileDetails = profileDetails;
	}
	/**
	 * @return the purchaseStatus
	 */
	public String getPurchaseStatus() {
		return purchaseStatus;
	}
	/**
	 * @param purchaseStatus the purchaseStatus to set
	 */
	public void setPurchaseStatus(String purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}
	/**
	 * @return the product_Id
	 */
	public String getProduct_Id() {
		return product_Id;
	}
	/**
	 * @param product_Id the product_Id to set
	 */
	public void setProduct_Id(String product_Id) {
		this.product_Id = product_Id;
	}
	
	
}
