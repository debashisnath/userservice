/**
 * 
 */
package com.dcs.userregistration.exception;

import java.io.Serializable;

/**
 * @author DebashisNath
 *
 */
public class OtpNotMatchedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5930052098215321387L;

	public OtpNotMatchedException(String s) {
		super(s);
	}
}
