/**
 * 
 */
package com.dcs.userregistration.util;

/**
 * @author DebashisNath
 *
 */
public enum PaymentStatusType {

	NOT_COMPLETED("Not_Completed"),
	INITIATED("Initiated"),
	PENDING("Pending"),
	SUCCESS("Success"),
	FAILED("Failed"),
	EXPIRED("Expired"),
	COMPLETED("Completed");
	
	private String statusType;

	private PaymentStatusType(String characteristic) {
		this.statusType = characteristic;
	}

	public String getCharasteristicType() {
		return statusType;
	}
}
