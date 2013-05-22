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


import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringEncrypter {
    private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private static final String DES_ENCRYPTION_SCHEME = "DES";
    private KeySpec keySpec;
    private SecretKeyFactory keyFactory;
    private Cipher cipher;
    private static final String UNICODE_FORMAT = "UTF8";

    private static String getKey(){
        return "123CSM34567890ENCRYPTIONC3PR4KEY5678901234567890";
    }

    public StringEncrypter() throws EncryptionException {
        this(DESEDE_ENCRYPTION_SCHEME, getKey());
    }

    public StringEncrypter(String encryptionScheme) throws EncryptionException {
        this(encryptionScheme, getKey());
    }

    public StringEncrypter(String encryptionScheme, String encryptionKey) throws EncryptionException {

        if (encryptionKey == null){
            throw new IllegalArgumentException("encryption key was null");
        }
        if (encryptionKey.trim().length() < 24){
            throw new IllegalArgumentException("encryption key was less than 24 characters");
        }

        try {
            byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);

            if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
                keySpec = new DESedeKeySpec(keyAsBytes);
            } else{
                if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {
                    keySpec = new DESKeySpec(keyAsBytes);
                } else {
                    throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
                }
            }

            keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
            cipher = Cipher.getInstance(encryptionScheme);

        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public  String encrypt(String unencryptedString) throws EncryptionException {
        if (StringUtil.isEmptyTrim(unencryptedString)){
            throw new IllegalArgumentException("unencrypted string was null or empty");
        }
        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] ciphertext = cipher.doFinal(cleartext);

            BASE64Encoder base64encoder = new BASE64Encoder();
            return base64encoder.encode(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public  String decrypt(String encryptedString) throws EncryptionException {
        if (StringUtil.isEmptyTrim(encryptedString)){
            throw new IllegalArgumentException("encrypted string was null or empty");
        }

        try {
            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
            byte[] ciphertext = cipher.doFinal(cleartext);

            return bytes2String(ciphertext);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

    public static class EncryptionException extends Exception {
		private static final long serialVersionUID = 1L;

		public EncryptionException(Throwable t) {
            super(t);
        }
    }
}