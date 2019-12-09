package webplay.Network;

import webplay.Security.Identification;

import java.net.HttpURLConnection;

public class BaseRequest {

	private String databaseVersion;
	private String assetVersion;
	private String clientVersion;
	private String server;
	private String accessTokken;
	private String secret;
	private String identifier;
	private String gifts="";
	private String signIn;
	private String signUp;
	private String transferCode;
	private String authS;
	private String osVersion = "android";
	public int requestVersion = 2;
	public String currentAuth;
	public boolean usePrevious = false;


	
	public void setDatabaseVersion(String database){
		this.databaseVersion = database;
	}
	
	public String getDatabaseVersion(){
		return this.databaseVersion;
	}
	
	public void setAssetVersion(String asset){
		this.assetVersion = asset;
	}
	
	public String getAssetVersion(){
		return this.assetVersion;
	}
	
	public void setClientVersion(String clientVer){
		this.clientVersion = clientVer;
	}
	
	public String getClientVersion(){
		return this.clientVersion;
	}
	
	public void setServer(String server){
		this.server = server;
	}
	
	public String getServer(){
		return this.server;
	}
	
	public void setAccessTokken(String accTokken){
		this.accessTokken = accTokken;
	}
	
	public String getAccessTokken(){
		return this.accessTokken;
	}
	
	public void setSecret(String secret){
		this.secret = secret;
	}
	
	public String getSecret(){
		return this.secret;
	}
	
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}
	
	public String getIdentifier(){
		return this.identifier;
	}
	
	public void setGifts(String gifts){
		this.gifts = gifts;
	}
	
	public String getGifts(){
		return this.gifts;
	}
	
	public void setSignUp(String signUp){
		this.signUp = signUp;
	}
	
	public String getSignUp(){
		return this.signUp;
	}
	
	public void setSignIn(String signIn){
		this.signIn = signIn;
	}
	
	public String getSignIn(){
		return signIn;
	}
	
	public void setTransferCode(String transferCode){
		this.transferCode = transferCode;
	}
	
	public String getTransferCode(){
		return this.transferCode;
	}
	
	public void setOsVersion(String osVersion){
		this.osVersion = osVersion;
	}
	
	public String getOsVersion(){
		return this.osVersion;
	}
	
	public String getRequestVersion(){
		String requestVer = Integer.toString(requestVersion);
		requestVersion++;
		return requestVer;
	}
	
	public void getBaseHeaders(HttpURLConnection conn){
		conn.setRequestProperty("Host"," " + server);
		conn.setRequestProperty("Accept-Encoding"," identity");
		conn.setRequestProperty("X-Platform"," " + osVersion);
		conn.setRequestProperty("X-Language"," en");
		conn.setRequestProperty("X-ClientVersion"," "+ clientVersion);
		conn.setRequestProperty("Connection"," Keep-Alive");
		conn.setRequestProperty("Content-Type"," application/json");
		conn.setRequestProperty("User-Agent"," Dalvik/2.1.0 (Linux; U; Android 9; SM-G950F Build/PPR1.180610.011)");
	}
	
	public void additionalHeaders(HttpURLConnection conn, String request, String requestType, Identification auth){
		conn.setRequestProperty("X-AssetVersion"," " + assetVersion);          
	    conn.setRequestProperty("X-DatabaseVersion"," " + databaseVersion);
	    String re = getRequestVersion();
	    conn.setRequestProperty("X-RequestVersion"," " + re);
	    conn.setRequestProperty("Authorization", " " + setAuth(requestType, request,auth));
	}

	public String setAuth(String requestType,String request,Identification auth){
		authS = auth.buildAuth(getAccessTokken(),getSecret(),requestType,request,getServer());
		return authS;
	}
	
	public String getAuth(){
	    return authS;
	}
}
