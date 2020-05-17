/**
 * 
 */
package com.dcs.userregistration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author DebashisNath
 *
 */
public class JSONUtil {

	/**
	 * @param object
	 * @return
	 */
	public static String convertObjectToJSON(Object object) {
		String jsonString = "";
		ObjectMapper mapperObj = new ObjectMapper();
		mapperObj.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		try {
			jsonString = mapperObj.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			
		}
		return jsonString;
	}
}
