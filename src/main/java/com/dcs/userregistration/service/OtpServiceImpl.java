/**
 * 
 */
package com.dcs.userregistration.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.dcs.userregistration.exception.OtpNotFoundException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author Debashis
 *
 */
@Service
public class OtpServiceImpl implements OtpService {

	private static final Integer EXPIRE_MINS = 5;
	private LoadingCache<String, Integer> otpCache;
	public OtpServiceImpl(){
		 super();
		 otpCache = CacheBuilder.newBuilder().
			     expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
				      public Integer load(String key) {
				             return 0;
				       }
				   });
	 }
	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.OtpService#generateOTP(java.lang.String)
	 */
	@Override
	public int generateOTP(String key) throws Exception {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		otpCache.put(key, otp);
		return otp;
	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.OtpService#getOTP(java.lang.String)
	 */
	@Override
	public int getOTP(String key) throws OtpNotFoundException {
		try {
			return otpCache.get(key);
		}catch (Exception e) {
			return 0;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.dcs.userregistration.service.OtpService#clearOTP(java.lang.String)
	 */
	@Override
	public void clearOTP(String key) throws OtpNotFoundException{
		otpCache.invalidate(key);

	}

}
