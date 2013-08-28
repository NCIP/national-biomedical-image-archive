/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

/**
 * @author lethai
 *
 */


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class StringEncrypter {

	private final static String HEX = "0123456789ABCDEF";
	private static final String ALGO = "AES";
   
    public String encryptString(String cleartext) throws Exception {
    	if (StringUtil.isEmptyTrim(cleartext)){
            throw new IllegalArgumentException("cleartext string was null or empty");
        }
        return toHex(encryptString(cleartext.getBytes()));
    }

    public  String decryptString(String encrypted) throws Exception {
    	if (StringUtil.isEmptyTrim(encrypted)){
            throw new IllegalArgumentException("encrypted string was null or empty");
        }
        byte[] enc = toByte(encrypted);
        return new String(decryptString(enc));
    }

    private  byte[] getRawKey() throws Exception {
    	String encryptionKey = NCIAConfig.getEncryptionKey(); 
    	byte[] seed = encryptionKey.getBytes();
        KeyGenerator kgen = KeyGenerator.getInstance(ALGO);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private  byte[] encryptString(byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(), ALGO);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private  byte[] decryptString(byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(), ALGO);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public  String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }
        
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }
    
    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }
  
}