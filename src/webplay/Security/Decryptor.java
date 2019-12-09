package webplay.Security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

public class Decryptor {
	private static final Charset ASCII = Charset.forName("ASCII");

    private static final int SALT_LENGTH = 8; 
    private static final int ITERATIONS = 1;
    private static final int KEY_SIZE_BITS = 256;

    private static final int INDEX_KEY = 0;
    private static final int INDEX_IV = 1;
    
    public static final String password = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzJ9JaHioVi6rr0TAfr6j";

    
    public static byte[][] EVP_BytesToKey(int key_len, int iv_len, MessageDigest md,
            byte[] salt, byte[] data, int count) {
        byte[][] both = new byte[2][];
        byte[] key = new byte[key_len];
        int key_ix = 0;
        byte[] iv = new byte[iv_len];
        int iv_ix = 0;
        both[0] = key;
        both[1] = iv;
        byte[] md_buf = null;
        int nkey = key_len;
        int niv = iv_len;
        int i = 0;
        if (data == null) {
            return both;
        }
        int addmd = 0;
        for (;;) {
            md.reset();
            if (addmd++ > 0) {
                md.update(md_buf);
            }
            md.update(data);
            if (null != salt) {
                md.update(salt, 0, 8);
            }
            md_buf = md.digest();
            for (i = 1; i < count; i++) {
                md.reset();
                md.update(md_buf);
                md_buf = md.digest();
            }
            i = 0;
            if (nkey > 0) {
                for (;;) {
                    if (nkey == 0)
                        break;
                    if (i == md_buf.length)
                        break;
                    key[key_ix++] = md_buf[i];
                    nkey--;
                    i++;
                }
            }
            if (niv > 0 && i != md_buf.length) {
                for (;;) {
                    if (niv == 0)
                        break;
                    if (i == md_buf.length)
                        break;
                    iv[iv_ix++] = md_buf[i];
                    niv--;
                    i++;
                }
            }
            if (nkey == 0 && niv == 0) {
                break;
            }
        }
        for (i = 0; i < md_buf.length; i++) {
            md_buf[i] = 0;
        }
        return both;
    }

    public String decrypt (String input) /*throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException*/{
    	String answer = "";
    	try{
    	byte[] inputBytes = Base64.decode(input);
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(inputBytes, 0, salt, 0, SALT_LENGTH);
        byte[] encrypted = new byte[inputBytes.length - SALT_LENGTH];
        System.arraycopy(inputBytes, SALT_LENGTH, encrypted, 0, encrypted.length);

        

        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        
        final byte[][] keyAndIV = EVP_BytesToKey(KEY_SIZE_BITS / Byte.SIZE,aesCBC.getBlockSize(),md5,salt,password.getBytes(ASCII),ITERATIONS);
        SecretKeySpec key = new SecretKeySpec(keyAndIV[INDEX_KEY], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[INDEX_IV]);

        

        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = aesCBC.doFinal(encrypted);

        answer = new String(decrypted, ASCII);
        
    	}catch(Exception e){
    		//nothing
    	}
    	return answer;
    }      
    
    public String encrypt(String input) /*throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException*/{
    	byte[] result = new byte[0];
    	try{
    	byte[] inputBytes = input.getBytes();
    	
    	SecureRandom sr = new SecureRandom();
    	byte[] salt = new byte[8];
    	sr.nextBytes(salt);
    	
    	Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		final byte[][] keyAndIV = EVP_BytesToKey(KEY_SIZE_BITS / Byte.SIZE, aesCBC.getBlockSize(), md5, salt, password.getBytes(), ITERATIONS);
		SecretKeySpec key = new SecretKeySpec(keyAndIV[INDEX_KEY], "AES");
		IvParameterSpec iv = new IvParameterSpec(keyAndIV[INDEX_IV]);

		aesCBC.init(Cipher.ENCRYPT_MODE, key, iv);
		
		byte[] enc = aesCBC.doFinal(inputBytes);
		result = new byte[salt.length + enc.length];
		System.arraycopy(salt, 0, result, 0, salt.length);
		System.arraycopy(enc, 0, result, salt.length, enc.length);
    	}catch(Exception e){
    		//nothing
    	}
		return new String(Base64.encode(result));
    }
}
