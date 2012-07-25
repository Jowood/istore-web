package com.framework.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import com.google.common.io.LimitInputStream;

public class MD5Util {
	public static String md5(final String filepath, final long start,
			final long size) throws NoSuchAlgorithmException, IOException {
		byte[] digest = ByteStreams.getDigest(new InputSupplier<InputStream>() {
			@Override
			public InputStream getInput() throws IOException {
				FileInputStream fileInputStream = new FileInputStream(new File(
						filepath));
				fileInputStream.skip(start);

				return new LimitInputStream(fileInputStream, size);
			}

		}, MessageDigest.getInstance("MD5"));

		return Base64.encodeBase64String(digest);
	}
	
    public static String md5file(File file) throws Exception {  
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));  
        byte[] buf = new byte[1024 * 100];  
        int p = 0;  
        while ((p = in.read(buf)) != -1) {  
            messageDigest.update(buf, 0, p);  
        }  
        in.close();  
        byte[] digest = messageDigest.digest();  
  
        //BASE64Encoder encoder = new BASE64Encoder();  
        return Base64.encodeBase64String(digest);  
    } 
}
