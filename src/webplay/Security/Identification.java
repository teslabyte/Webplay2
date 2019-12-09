package webplay.Security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Identification {
	
	public String ts = "";     //timestamp
	public String nonce = ""; 
	public String mac = "";
	public String aesIdentifier = "fe397b1f53008cccf8e8636aad967fbf";
	public String randomS = "a";

	
	public String buildAuth(String accessTokken,String secret,String requestType,String request,String server){  //adds everything together to create valid Authorization for requests --- Works correctly tested multiple times
		timeStamp();
		generateNonce();	
		String data = macData(requestType,request,server);
		mac = generateMac(secret,data);
		String test = "MAC id=" + "\"" + accessTokken + "\", nonce=\"" + nonce + "\", ts=\"" + ts + "\", mac=\"" + mac + "\"";
		return test;
	}
	
	public String generateMac(String accessKey,String data) {                   
		try { 
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(accessKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			return new String(Base64.encodeBase64((sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)))));
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				}
		return null;
	}
	
	public String macData(String requestType,String request,String server){    
		return ts+"\n"+nonce+"\n"+requestType+"\n"+request+"\n"+server +"\n443\n\n";
		//return "1570650340\n" + "1570650340:8276bb6c703cb99a91f8515c8dd8b043\n"+"GET\n"+"/user\n"+"ishin-production.aktsk.jp"+"\n3001\n\n";
	}
	
	
	public String randomString(){                               
		String candidateChars = "abcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < 32; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
	    }

	    return sb.toString();
	}
	
	public void timeStamp(){                                    
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ts = String.valueOf(timestamp.getTime()).substring(0,10);		
	}
	
	public long timeStampMS(){
		return new Timestamp(System.currentTimeMillis()).getTime();
	}
	
	public void generateNonce(){
		if(randomS.length() < 5) nonce =  ts + ":" + randomString();
		else nonce = ts + ":" + randomS;
	}
	
	public void encryptAes(String text) throws Exception{
		byte[] myKey = aesIdentifier.getBytes(StandardCharsets.UTF_8);
		SecretKeySpec secretKey = new SecretKeySpec(myKey, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		
		String encryption = Base64.encodeBase64String(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
		System.out.println(encryption);
		
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		System.out.println(new String(cipher.doFinal(Base64.decodeBase64(encryption))));
	}

	public String decryptAes(String text){
		try{
			byte[] myKey = aesIdentifier.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKey = new SecretKeySpec(myKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.decodeBase64(text)));
		}catch (Exception e){

		}
		return "";
	}
}
