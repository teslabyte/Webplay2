package webplay.Network;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import webplay.ResponseClasses.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import java.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import webplay.ResponseClasses.Cards.Card;
import webplay.ResponseClasses.Cards.CardsRootObject;
import webplay.ResponseClasses.Gashas.Gasha;
import webplay.ResponseClasses.Gashas.GashasRootObject;
import webplay.ResponseClasses.Missions.MissionsRootObject;
import webplay.ResponseClasses.Quests.StartRootObject;
import webplay.ResponseClasses.Quests.StartSign;
import webplay.ResponseClasses.Quests.Supporters.Supporter;
import webplay.ResponseClasses.Quests.Supporters.SupportersRootObject;
import webplay.ResponseClasses.UserAreas.UserAreasRootObject;
import webplay.Security.Decryptor;
import webplay.Security.Identification;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Connect{
		public static Type PROXY_TYPE = Proxy.Type.SOCKS;
		public static String PROXY_URL = "localhost";
		public int PROXY_PORT = 8888;

    	boolean toReset = false;

		public String eventkagi = "";
		public String customAuth = "a";
		public String punchingMachine = "";
		Object obj;  
		JSONObject jsonObject; 

		public int dupeSuccess = 0;
		String customAuthBasic = "t";
		public String responseMessage;
		public String fodder = "";
		public String toDupe = "";
		public String toDupeCardId = "";
		String reqResponse;
		int ind1,ind2;
		long questFinishedAt;
		int srv;
		String help;
		String url;
		int urlCount = 1;
		int ezaStageNum = 1;
		int teamSelected = 1;
		String captchaKey;
		List <String> mission = new ArrayList<String>();
		public List <String> areaEvent = new ArrayList<String>();
		public int wantedEvent = 0;
		long transferedAccTs;
		String savedResponse;
		String chosenServer = ""; //can be jp/glb

		public Identification auth = new Identification();
		public BaseRequest baseRequest = new BaseRequest();
		Decryptor secure = new Decryptor();
		public ResponseHandler resp = new ResponseHandler();
		DataOutputStream wr;
		private ObjectMapper objectMapper = new ObjectMapper();

		public String accountsToFile = "";
		private AuthSignIn authSignIn;
		private User user;
		private UserInfo userInfo;


		public void setResponse(String resp){
		    responseMessage = resp;
		}
		
		
		private HttpURLConnection openConnection(URL url) throws IOException{
		    Proxy proxy = new Proxy(PROXY_TYPE, new InetSocketAddress(PROXY_URL, PROXY_PORT));
		    return (HttpURLConnection) url.openConnection(proxy);
		}

		//replace baseRequest with ServerSettings eventually
		public void setServerSettings(String chosenServer) throws Exception{

			if(chosenServer.equals("jp")){
                baseRequest.setServer("ishin-production.aktsk.jp");
                baseRequest.setClientVersion("4.7.0-891e1b641cebdf28bcfbb965d0c8e19cc5a15db485d145508c207ab315b6f972");
                baseRequest.setAssetVersion("////");
                baseRequest.setDatabaseVersion("////");
			}
			else{
                baseRequest.setServer("ishin-global.aktsk.com");
                baseRequest.setClientVersion("4.6.1-ea9aa08c2b2c2d99ec0ee817f7b73309e53060b2ceca7d3323bd0a97c31ad6ee");
                baseRequest.setAssetVersion("////");
                baseRequest.setDatabaseVersion("////");
            }
			setResponse("SetData done");
		}

		public String authBasic(){
			if(customAuthBasic.length() > 5) return customAuthBasic;
			String basic,partOne,partTwo;
			basic = new String(Base64.getDecoder().decode(baseRequest.getIdentifier().getBytes()));
			partTwo = basic.substring(0,88);
			partOne = basic.substring(89);
			basic = partOne + ":" + partTwo;
			basic = Base64.getEncoder().encodeToString(basic.getBytes());
			return basic;
		}

		public void setOsVersion(String osVersion){				//osVersion = android || ios
			resp.setSignUp(resp.getSignUp().replace("android",osVersion));
			resp.setTransferCode(resp.getTransferCode().replace("android",osVersion));
			setResponse("OS Version: " + osVersion);
			baseRequest.setOsVersion(osVersion);
		}

		public String readResponse(HttpURLConnection conn,String request,String requestType) throws Exception{    
			String line,info="";
			int responseCode = conn.getResponseCode();
			boolean isError = false;
		    BufferedReader reader;
		    	if (200 <= responseCode && responseCode <= 299) {
		    	    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    	} else {
		    	    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		    	    isError = true;
		    	}
		    while ((line = reader.readLine()) != null) {
		    	info += line;
		    }
			reader.close();

		    if(isError) {
		    	System.out.println("readResponse() : " + request + " " + responseCode + ":" + info);
		    }
			return info + "\n";
		}
		
		public void errorHandler(String errorCode,String request,String requestType,int responseCode) throws Exception{
			if(responseCode == 429) {
				toReset = true;
				return;
			}
			if(errorCode.contains("act_")){
				System.out.println("Recovering act");
				request("/user/recover_act_with_stone", "PUT");
				request(request, requestType);
				return;
			}
			if(errorCode.contains("the_number")){
				System.out.println("Selling cards");
				request("/cards","GET");
	    		request("/cards/sell", "POST");
				request(request, requestType);
				return;
			}
			/*if(errorCode.contains("token")){
				System.out.println("Sign-in again");
				request("/auth/sign_in","POST");
				request(request, requestType);
				return;
			}*/
			if(errorCode.contains("not_exist_supporter")){
				System.out.println(savedResponse);
				return;
			}
		}
		
		public String readFile(String filename) throws Exception{  
			FileReader fileReader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fileReader);
			String currentLine,finalLine="";
			while((currentLine=reader.readLine())!=null) finalLine+=currentLine;
			reader.close();
			return finalLine;
		}
		
		public void writeFile(String filename,String infoToWrite,boolean toAppend) throws Exception{
			BufferedWriter writer = null;
			
			writer = new BufferedWriter(new FileWriter(filename,toAppend));
	        writer.write(infoToWrite);
	        writer.close();
		}

		public String getGifts(Object obj,JSONObject jsonObject){
			String gifts = "";
			jsonObject = (JSONObject) obj;

			JSONArray jsonArray = (JSONArray) jsonObject.get("gifts");

			Iterator i = jsonArray.iterator();

	        while (i.hasNext()) {
	            jsonObject = (JSONObject) i.next();
	            gifts += String.valueOf(jsonObject.get("id")) + ",";
	        }

			if(gifts.length() < 2){
			    return "";
			}
			System.out.println(gifts);
			return gifts.substring(0, gifts.length()-1);
		}
		
		public String stepCounter(String decResponse){
			String helpSteps = "";
			int index1,index2;
			while(decResponse.length()>400){
				index1 = decResponse.indexOf(",\"")+1;
				index2 = decResponse.indexOf("\":", index1 + 1);
				if(StringUtils.isNumeric(decResponse.substring(index1+1, index2))) helpSteps +=decResponse.substring(index1+1, index2) + ",";
				decResponse = decResponse.substring(index2+2);
			}
			return "0," + helpSteps.substring(0, helpSteps.length()-1);
		}
		
		public String sellCards(){
			jsonObject = (JSONObject) obj;
			String createdAt,id;
			String toSell = "";
			int numUnitsToSell = 0;
			JSONArray jsonArray = (JSONArray) jsonObject.get("cards");
			Iterator i = jsonArray.iterator();

			while (i.hasNext()) {
				jsonObject = (JSONObject) i.next();
				createdAt = String.valueOf(jsonObject.get("created_at"));
				id = String.valueOf(jsonObject.get("id"));

				if(Integer.valueOf(createdAt) > transferedAccTs) {
					toSell += id + ",";
					numUnitsToSell++;
				}
				if(numUnitsToSell > 98) break;
			}

			System.out.println("Selling " + numUnitsToSell + " cards.");
			return toSell.substring(0,toSell.length()-1);
		}
		
		public void acceptMission() throws Exception{
			int count = mission.size();
			for(int i = 0;i<count;i++){
				request("/missions/" + mission.get(i) + "/accept","POST");
			}
			mission.clear();
		}
		
		public void setAreaIds(Object obj,JSONObject jsonObject){
			areaEvent.clear();
			JSONArray jsonArray = (JSONArray) jsonObject.get("user_areas");
			Iterator i = jsonArray.iterator();
			String areaId;
			
	        while (i.hasNext()) {
	        	jsonObject = (JSONObject) i.next();
	        	areaId = String.valueOf(jsonObject.get("area_id"));
	        	if(areaId.equals(String.valueOf(wantedEvent))){
	        		JSONArray jsonArray2 = (JSONArray) jsonObject.get("user_sugoroku_maps");
	        		Iterator i2 = jsonArray2.iterator();
	        		 while (i2.hasNext()) {
	     	        	jsonObject = (JSONObject) i2.next();
	     	        	String mapId = String.valueOf(jsonObject.get("sugoroku_map_id"));
	     	        	areaEvent.add(mapId);
	        		 }
	        	}
	        }
		}

		public void setMissions(Object obj,JSONObject jsonObject){
			jsonObject = (JSONObject) obj;
			String completedAt;
			String acceptedAt;
			String id;
			JSONArray jsonArray = (JSONArray) jsonObject.get("missions");
			Iterator i = jsonArray.iterator();

	        while (i.hasNext()) {
	            jsonObject = (JSONObject) i.next();
	            completedAt = String.valueOf(jsonObject.get("completed_at"));
	            acceptedAt = String.valueOf(jsonObject.get("accepted_reward_at"));
	            if(completedAt != "null" && acceptedAt == "null"){
	            	id = String.valueOf(jsonObject.get("id"));
	            	mission.add(id);
	            }
	        }
		
		}
		
		public String cpuSupporters(JSONObject jsonObject){
			
			
			switch(resp.getDifficulty()){
			case "2":jsonObject = (JSONObject) jsonObject.get("very_hard");break;
			case "3":jsonObject = (JSONObject) jsonObject.get("super_hard1");break;
			case "4":jsonObject = (JSONObject) jsonObject.get("super_hard2");break;
			case "5":jsonObject = (JSONObject) jsonObject.get("super_hard3");break;
			case "1":jsonObject = (JSONObject) jsonObject.get("hard");break;
			default :jsonObject = (JSONObject) jsonObject.get("normal");break;
			}
			
			if(String.valueOf(jsonObject).equals("[]")) return null;
			
			JSONArray jsonArray = (JSONArray) jsonObject.get("cpu_friends");
			
			if (String.valueOf(jsonArray).equals("[]")) return null;
			
			Iterator i = jsonArray.iterator();

	        while (i.hasNext()) {
	            jsonObject = (JSONObject) i.next();
	            return String.valueOf(jsonObject.get("id"));
	        }
	        
			return String.valueOf(jsonObject = (JSONObject) jsonObject.get("id"));
		}

		public String supporters(JSONObject jsonObject){
			JSONArray jsonArray = (JSONArray) jsonObject.get("supporters");
			Iterator i = jsonArray.iterator();

			while (i.hasNext()) {
				jsonObject = (JSONObject) i.next();
				return String.valueOf(jsonObject.get("id"));
			}
			return "E";
		}

		public void cardDupes(JSONObject jsonObject){
			JSONArray jsonArray = (JSONArray) jsonObject.get("cards");
			String card_id;
			Iterator i = jsonArray.iterator();
			while (i.hasNext()) {
				jsonObject = (JSONObject) i.next();
				card_id = String.valueOf(jsonObject.get("card_id"));
				if(card_id.equals(toDupeCardId)) toDupe = String.valueOf(jsonObject.get("id"));
				if(card_id.endsWith("0") && card_id.startsWith(toDupeCardId.substring(0,toDupeCardId.length()-1))) fodder = String.valueOf(jsonObject.get("id"));
			}
			System.out.println(toDupe + " with  " + fodder);
		}

		public void reqOutput(HttpURLConnection conn,String request) throws Exception{
			wr = new DataOutputStream(conn.getOutputStream());
			String output="e";
			ind1 = request.indexOf("/",3);
			if(ind1 < 0) ind1 = request.length();
			
			switch(request.substring(0, ind1)){
			case "/auth":{
				switch(request.substring(ind1)){
				case "/sign_up": {
					if(urlCount == 1){
						wr.writeBytes( "{" + resp.getSignUp());
					}
					else{
						wr.writeBytes("{" + "\"captcha_session_key\":\"" + captchaKey + "\","+ resp.getSignUp());
						urlCount = 1;
					}
				};break;
				case "/sign_in": wr.writeBytes(resp.getSignIn()); break;
				default: if (request.contains("val")) wr.writeBytes("{\"eternal\":true,\"user_account\":{\"platform\":\"" + baseRequest.getOsVersion() + "\",\"user_id\":" + resp.getUserId() +"}}");
				else wr.writeBytes(resp.getTransferCode());
				}
			};break;
			case "/tutorial": if(ind1 == request.length()) wr.writeBytes("{\"" + "progress" + "\":999}");break;
			case "/gifts": wr.writeBytes(output = "{\"gift_ids\":[" + baseRequest.getGifts() + "]}");break;
			case "/cards": {
				if(request.contains("train")) wr.writeBytes("{\"card_ids\": [" + fodder +"],\"training_field_id\": 1,\"training_items\": []}");
				else wr.writeBytes("{\"card_ids\":[" + sellCards() + "]}");
			};break;
			case "/quests":{
				ind2 = request.indexOf("/",ind1 + 10);
				switch(request.substring(ind2)){
				case "/start": {
					if(resp.getFriendId().length()>5 || resp.getFriendId().equals("1"))wr.writeBytes("{\"sign\":\"" + secure.encrypt("{\"difficulty\":" + resp.getDifficulty() + eventkagi +",\"friend_id\":" + resp.getFriendId() + ",\"is_playing_script\":false,\"selected_team_num\":"+teamSelected+"}")+"\"}");
					else wr.writeBytes("{\"sign\":\"" + secure.encrypt("{\"cpu_friend_id\":" + resp.getFriendId() + eventkagi +",\"difficulty\":" + resp.getDifficulty() + ",\"is_playing_script\":false,\"selected_team_num\":"+teamSelected+"}")+"\"}");
				};break;
				case "/finish": {
					questFinishedAt = System.currentTimeMillis();
					wr.writeBytes("{\"sign\":\"" +secure.encrypt("{\"actual_steps\":" + resp.getSteps() + ",\"difficulty\":"+ resp.getDifficulty() + punchingMachine + ",\"elapsed_time\":1200000,\"is_cheat_user\":false,\"is_cleared\":true,\"quest_finished_at_ms\":" + questFinishedAt + ",\"quest_started_at_ms\":" + (questFinishedAt - 1200000) + ",\"steps\":" + resp.getSteps() + ",\"token\":\""+ resp.getToken() +"\"}")+"\"}");
				};break;
				}
			};break;
			case "/shops":wr.writeBytes("{\"bought_num\":1}");break;
			case "/user_cards":{
				wr.writeBytes("{\"consume_user_card_id\":" + fodder + "}");
			}break;
			case "/z_battles":{
				if(request.endsWith("start")){
					String enc = secure.encrypt("{\"friend_id\":" + resp.getFriendId() + ",\"level\":" + ezaStageNum +",\"selected_team_num\":1}");				
					wr.writeBytes("{\"sign\":\"" + enc +"\"}");
					
				}
				else if(request.endsWith("/finish")){
					int enemyAttack = ThreadLocalRandom.current().nextInt(225000, 300000);
					int enemyAttackCount = ThreadLocalRandom.current().nextInt(3, 8);
					int enemyMaxAttack = ThreadLocalRandom.current().nextInt(60000, 110000);
					int enemyMinAttack = ThreadLocalRandom.current().nextInt(25000, 45000);
					int playerAttackCounts = ThreadLocalRandom.current().nextInt(9, 15);
					int playerAttacks = ThreadLocalRandom.current().nextInt(5000000, 8000000);
					int playerMaxAttacks = ThreadLocalRandom.current().nextInt(1000000, 3000000);
					int playerMinAttacks = ThreadLocalRandom.current().nextInt(500000, 1000000);
					String t = "{\"summary\": {\"enemy_attack\": " + enemyAttack
							+ ",\"enemy_attack_count\": " + enemyAttackCount + ",\"enemy_heal_counts\":" 
							+"[0],\"enemy_heals\": [0],\"enemy_max_attack\": " + enemyMaxAttack 
							+",\"enemy_min_attack\": " + enemyMinAttack + ",\"player_attack_counts\": [" + 
							playerAttackCounts + "],\"player_attacks\": [" + playerAttacks 
							+ "],\"player_heal\": 0,\"player_heal_count\": 0,\"player_max_attacks\": [" + 
					playerMaxAttacks +"],\"player_min_attacks\": [" + playerMinAttacks +"],\"type\": \"summary\"}}";
					t = new String(Base64.getEncoder().encodeToString(t.getBytes()));
					int elapsedTime = ThreadLocalRandom.current().nextInt(100000, 200000);
					long finishedAt = System.currentTimeMillis();
					String toOut = "{\"elapsed_time\":" + elapsedTime 
							+ ",\"is_cleared\":true,\"level\":" 
							+ ezaStageNum + ",\"t\":\"" + t + "\"," + 
							"\"token\":\"" + resp.getToken() + "\"," +
							"\"used_items\":[],\"z_battle_finished_at_ms\":" 
							+ finishedAt + "," + "\"z_battle_started_at_ms\":" 
							+ (finishedAt-elapsedTime) + "}";
					wr.writeBytes(toOut);
				}
			};break;
			default:wr.writeBytes(output="null");break;
			}
			wr.flush();
		}
		
		
		public void addHeaders(HttpURLConnection conn,String request,String requestType){
			if(request.contains("validate")||(request.contains("/auth/link_codes")&&request.length()>16))return;
			
			switch(request){
			case "/client_assets/database":{
				conn.setRequestProperty("X-AssetVersion"," 0"); 
			    conn.setRequestProperty("X-RequestVersion"," 5");
			    conn.setRequestProperty("Authorization", " " + baseRequest.setAuth(requestType, request,auth));
			};break;
			case "/auth/sign_in":{
				conn.setRequestProperty("Authorization", " Basic " + authBasic());  
			    conn.setRequestProperty("X-UserCountry"," RS");
			    conn.setRequestProperty("X-UserCurrency"," RSD");
			};break;
			case "/auth/sign_up":break;
			case "/ping":break;
			default:baseRequest.additionalHeaders(conn,request,requestType,auth);break;
			}
		}
		
		public void reqResponse (String response,String request) {
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if(!request.equals("/gifts/accept")){
				obj = JSONValue.parse(response);
				jsonObject = (JSONObject) obj;
			}
			
			if(request.endsWith("/0/supporters")){
				int ind3 = response.indexOf("\"id\":");
				resp.setFriendId(response.substring(ind3 + 5, response.indexOf(",", ind3)));
				return;
			}
			
			ind1 = request.indexOf("/",3);
			if(ind1 == -1) ind1 = request.length();
			try{
				switch(request.substring(0,ind1)){
				case "/auth":{
					ind2 = request.indexOf("/", ind1 + 1);
					if(ind2 == -1) ind2 = request.length();
					switch(request.substring(ind1,ind2)){
					case "/sign_up":{
						if(urlCount == 1){
					    url = String.valueOf(jsonObject.get("captcha_url"));
					    captchaKey = String.valueOf(jsonObject.get("captcha_session_key"));
					    url = url.replaceAll("\\u0026", "&");
					    if (Desktop.isDesktopSupported() && url.length() > 5) {
					    	System.out.println("Opening url");
			                Desktop.getDesktop().browse(new URI(url));
					    }
					    urlCount++;
						}
						baseRequest.setIdentifier(String.valueOf(jsonObject.get("identifier")));
						jsonObject = (JSONObject) jsonObject.get("user");
					    resp.setId(String.valueOf(jsonObject.get("id")));
					};break;
					case "/sign_in":{                					//NEW MAPPING IS IN TESTING HERE
						authSignIn = objectMapper.readValue(String.valueOf(jsonObject),AuthSignIn.class);
						baseRequest.setAccessTokken(String.valueOf(jsonObject.get("access_token")));            
					    baseRequest.setSecret(String.valueOf(jsonObject.get("secret")));
					    transferedAccTs = System.currentTimeMillis()/1000;
					};break;
					case "/link_codes":{
						if (request.length() > 16) {
							response = response.replaceAll("\\\\n", "");
						    baseRequest.setIdentifier(response.substring(response.indexOf("identifiers:") + 17, response.length()-3));
						}
						else {
						    resp.setLinkCode(String.valueOf(jsonObject.get("link_code")));
                        }
					};break;
					};
				};break;
				case "/gifts": baseRequest.setGifts(getGifts(obj,jsonObject)); break;
				case "/client_assets":{
				    if (request.length() == ind1) {
					    baseRequest.setAssetVersion(String.valueOf(jsonObject.get("latest_version")));  //client_assets
                    }
					else baseRequest.setDatabaseVersion(String.valueOf(jsonObject.get("version")));     //client_assets/database
				};break;
				case "/missions": {
					MissionsRootObject missionRootObject = new MissionsRootObject();
					missionRootObject = objectMapper.readValue(String.valueOf(jsonObject), MissionsRootObject.class);
					if(ind1 == request.length()) setMissions(obj,jsonObject);
				}break;
				case "/user_areas": {
					setAreaIds(obj,jsonObject);
					UserAreasRootObject rootObject = new UserAreasRootObject();
					rootObject = objectMapper.readValue(String.valueOf(jsonObject), UserAreasRootObject.class);
				}break;
				case "/quests":{
					ind2 = request.indexOf("/", ind1 + 1);
					switch(request.substring(ind2)){
					case "/supporters":{
						/*SupportersRootObject supportersRootObject = objectMapper.readValue(String.valueOf(jsonObject),SupportersRootObject.class);
						for(Supporter a : supportersRootObject.getSupporters()){
							System.out.println(a.getName());
						}*/
						savedResponse = response;
						ind1 = response.indexOf("\"id\":");
						String sup = supporters(jsonObject);
						jsonObject = (JSONObject) jsonObject.get("cpu_supporters");
						
						if((help = cpuSupporters(jsonObject))!=null){
							resp.setFriendId(help);
						}
						else {
						    resp.setFriendId(sup);
						}
						
					};break;
					case "/sugoroku_maps/start" :{
						StartSign startSign = objectMapper.readValue(String.valueOf(jsonObject),StartSign.class);
						StartRootObject startRootObject = objectMapper.readValue(secure.decrypt(startSign.getSign()),StartRootObject.class);
						resp.setToken(startRootObject.getToken());
						help = secure.decrypt(startSign.getSign());
						resp.setSteps("[" + stepCounter(help) + "]");
					};break;
					}
				}
				case "/z_battles":{
					if(request.endsWith("/start")){
						help = secure.decrypt(response.substring(9,response.length()-3));
						resp.setToken(help.substring(help.indexOf("\"token\":")+9,help.indexOf("\",")));
					}
				};break;
				case "/cards":{
					CardsRootObject cardsRootObject = objectMapper.readValue(String.valueOf(jsonObject),CardsRootObject.class);

					for(Card a : cardsRootObject.getCards()){
						System.out.println(a.getCard_id());
					}
					cardDupes(jsonObject);
				};break;
				case "/user":{   									//MAP TESTING !!!
					userInfo = objectMapper.readValue(String.valueOf(jsonObject),UserInfo.class);
					user = userInfo.getUser();
					System.out.println(user.getCard_capacity());
				};break;
				case "/gashas":{
					GashasRootObject gashasRootObject = objectMapper.readValue(String.valueOf(jsonObject),GashasRootObject.class);
					for(Gasha a : gashasRootObject.getGashas()){
						System.out.println(a.getName());
					}
				} break;
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}

		public boolean whichServer(){
		    return baseRequest.getServer().equals("ishin-production.aktsk.jp");  //true if JP,false if Global
        }

		
		public String Request(String request, String requestType) throws Exception{
			long startTime = System.currentTimeMillis();
			if(request.equals("/auth/sign_in")){
				baseRequest.requestVersion = 3;
			}
			URL url = new URL("https://" + baseRequest.getServer() + request);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//if(request.equals("/auth/sign_up")) conn = openConnection(url);
			
		    if(!requestType.equals("POST"))conn.setDoOutput(true);
		    
	
		    conn.setRequestMethod(requestType); 

		    baseRequest.getBaseHeaders(conn);

		    addHeaders(conn,request,requestType);
		    if(requestType.equals("GET"))conn.connect();
		    if(requestType.equals("POST"))conn.setDoOutput(true);
		    if(!requestType.equals("GET"))reqOutput(conn,request);
		    if(requestType.equals("POST"))conn.connect();
		    return readResponse(conn,request,requestType);
		}

		public void request(String url, String requestType) throws Exception{
			reqResponse = Request(url, requestType);

			if(reqResponse.startsWith("{\"error\"")){
			    System.out.println("starts with error : " + reqResponse);
				errorHandler(reqResponse,url,requestType,400);
			}
			else{
				reqResponse(reqResponse, url);
			}

			setResponse(reqResponse);
		}
}
