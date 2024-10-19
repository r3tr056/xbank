package com.xfinance.xbank.infrastructure.auth.otp;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import java.util.concurrent.TimeUnit;
import java.util.Random;

@Description(value="Service for generating and validating OTP in a secure way.")
@Service
public class OtpGenerator {

	
	private static final Integer EXPIRE_MIN = 5;
	private LoadingCache<String, Integer> otpCache;

	public OtpGenerator() throws Exception {
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
			@Override
			public Integer load(String s) throws Exception {
				return 0;
			}
		});
	}

	public  generateOtp(String key) {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		int otp = 1000000 + secureRandom.nextInt(9000000);
		this.otpCache.put(key, otp);
		return otp;
	}

	public Integer getOtpByKey(String key) {
		return otpCache.getIfPresent(key);
	}

	public void clearOtpFromCache(String key) {
		otpCache.invalidate(key);
	}

}