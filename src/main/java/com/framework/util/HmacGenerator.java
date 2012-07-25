package com.framework.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.google.common.base.Charsets;

public class HmacGenerator {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	/**
	 * Computes RFC 2104-compliant HMAC signature. *
	 * 
	 * @param secretKey
	 * 
	 * @param data
	 *            The data to be signed.
	 * 
	 * @return signature
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 */

	public static String generate(String secretKey, String data) {
		try {
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(
					secretKey.getBytes(Charsets.UTF_8), HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes(Charsets.UTF_8));

			return new String(Base64.encodeBase64(rawHmac), Charsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException();
		} catch (InvalidKeyException e) {
			throw new IllegalStateException();
		}
	}
}
