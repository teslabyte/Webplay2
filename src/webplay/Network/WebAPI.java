package webplay.Network;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.atmosphere.config.service.Get;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.AtmosphereResourceSession;
import org.atmosphere.cpr.AtmosphereResourceSessionFactory;
import org.atmosphere.cpr.DefaultAtmosphereResourceSessionFactory;

import java.util.*;
import java.util.logging.*;

import webplay.Files.AuthBasicFile;
import webplay.UsageSpecific.DupeSuperAttack;
import webplay.UsageSpecific.DupeUnits;
import webplay.UsageSpecific.FarmSession;

import java.io.IOException;

@ManagedService(path = "/webplay")
public class WebAPI {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger(WebAPI.class.getName());
    private final AtmosphereResourceSessionFactory sessionFactory = new DefaultAtmosphereResourceSessionFactory();
    
    
    class Session {
        public Connect connection;
        public BaseRequest baseRequest;
        public String statusMessage;
        public StateEnum state = StateEnum.SELECTING_SERVER;
        public String uid;
        public Session(){
            System.out.println("-----Session created------");
        }
    }
    
    private enum StateEnum { 
        SELECTING_SERVER,  
        PROCESING_COMMAND,
    };
    
    public WebAPI() throws Exception {

    }
    
    @Get
    public void onOpen(final AtmosphereResource r) {

        r.addEventListener(new AtmosphereResourceEventListenerAdapter() {
            @Override
            public void onSuspend(AtmosphereResourceEvent event) {
                logger.info("User " + r.uuid()+ " connected.");

                Session session = new Session();
                session.connection = new Connect();
                session.baseRequest = new BaseRequest();
                session.uid = r.uuid();

                session.statusMessage = "Jp or Glb?";
                
                try {
                    String ad_id = String.valueOf(UUID.randomUUID());
                    String unique_id = UUID.randomUUID() + ":";
                    unique_id += String.valueOf(UUID.randomUUID()).substring(0,16);
                    session.connection.resp.setSignUpWithId(ad_id,unique_id);
                    session.connection.resp.setSignIn(session.connection.readFile("signIn.txt"));
                    session.connection.resp.setTransferCode(session.connection.readFile("transferCode.txt"));
                    ObjectMapper ob = new ObjectMapper();
                    String settingsFromFile = session.connection.readFile("resources/settings.json");
                } catch (Exception e) {
                    
                    session.statusMessage = "Error reading signup/signin.txt";
                }
                
                setSessionValue(r, "session", session);
            }

            @Override
            public void onDisconnect(AtmosphereResourceEvent event) {
                if (event.isCancelled()) {
                    logger.info("User " + r.uuid() + " unexpectedly disconnected");
                } else if (event.isClosedByClient()) {
                    logger.info("User " + r.uuid() + " closed the connection");
                }
            }
        });
    }

    @Message
    public String onMessage(AtmosphereResource r, String message) throws IOException {
        logger.info("MSG: " + message);
        ChatMessage cmd = mapper.readValue(message, ChatMessage.class);    

        ChatMessage reply;
        try {
            Session session = getSessionValue(r, "session", Session.class);
            processCommand(session, cmd, session.connection);
            reply = new ChatMessage("WebPlay", session.statusMessage);
        } catch (Exception e) {
            reply = new ChatMessage("WebPlay ERROR", e.getMessage());
        }

        return mapper.writeValueAsString(reply);
    }

     String ezaVer = "0";

    public void processCommand(Session session, ChatMessage cmd, Connect connection) throws Exception {
        String choose = cmd.getMessage();
        int ind = choose.length();
        if(choose.contains(" ")) ind = choose.indexOf(" ");
        if(session.state == StateEnum.SELECTING_SERVER) {
            session.statusMessage = "Choose what you want to do:";
            session.state = StateEnum.PROCESING_COMMAND;
        }
        
        if(session.state == StateEnum.PROCESING_COMMAND) {
            boolean wrongCmd = false;
            switch(choose.substring(0,ind)){
                case "ping":connection.request("/ping","GET");break;
                case "signin":connection.request("/auth/sign_in","POST");break;
                case "signup":connection.request("/auth/sign_up","POST");break;
                case "user":connection.request("/user","GET");break;
                case "gifts":connection.request("/gifts","GET");break;
                case "accept":connection.request("/gifts/accept","POST");break;
                case "apologies":connection.request("/apologies/accept","PUT");break;
                case "database":getDatabase(connection);break;
                case "tutorial":finishTutorial(connection);break;    
                case "login":connection.request("/login_bonuses/accept","POST");break;
                case "cards":{
                        connection.request("/cards","GET");
                        //connection.sellCards();
                }   break;
                case "teams":connection.request("/teams","GET");break;
                case "friendships":connection.request("/friendships","GET");break;
                case "areas":connection.request("/user_areas","GET");break;
                case "dragonballs":connection.request("/dragonball_sets","GET");break;
                case "missions":connection.request("/missions","GET");break;
                case "budokai":connection.request("/budokai","GET");break;
                case "gashas":connection.request("/gashas","GET");break;
                case "supportItems":connection.request("/support_items","GET");break;
                case "shopItems":connection.request("/shops/treasure/items","GET");break;
                case "treasureItems":connection.request("/treasure_items","GET");break;
                //case "events":connection.request("/events","GET");break;
                case "comeback":connection.request("/comeback_campaigns/entry","POST");break;
                case "zeniShop":connection.request("/shops/zeni/items", "GET");break;
                case "reroll":reroll(connection,false);break;
                case "bossreroll":reroll(connection, true);break;
                case "asset":connection.request("/client_assets", "GET");break;
                case "set":{
                        String[] splitString = choose.split(" ");
                        connection.chosenServer = splitString[1];
                        connection.setServerSettings(splitString[1]);
                };break;
                case "createTc":connection.request("/auth/link_codes","POST");break;
                case "fre":connection.request("/quests/701001/supporters", "GET");break;
                case "start":connection.request("/quests/701001/sugoroku_maps/start", "POST");break;
                case "finish":connection.request("/quests/701001/sugoroku_maps/finish", "POST");break;
                case "boss":bossRush(connection);break;
                case "act":connection.request("/user/recover_act", "PUT");break;
                case "char":connection.request("/user/capacity/card", "POST");break;
                case "story":finishStory(connection,0,session.uid);break;
                case "medals":connection.request("/cards/sell", "POST");break;
                case "sell":connection.request("/cards/sell", "POST");break;
                case "setIos":connection.setOsVersion("ios");break;
                case "createTcTest": {
                    for(int i = 0;i<100;i++){
                        connection.request("/auth/link_codes","POST");
                        System.out.println(connection.resp.getLinkCode());
                    }
                }break;
                case "transfer":{
                    transferCode(connection,choose);
                };break;
                case "server": connection.baseRequest.setServer("192.168.1.4:7650");break;
                case "trainingItems":connection.request("/training_items", "GET");break;
                case "awakeningItems":connection.request("/awakening_items", "GET");break;
                case "potentialItems":connection.request("/potential_items", "GET");break;
                case "specialItems":connection.request("/special_items", "GET");break;
                case "dragonballSets":connection.request("/dragonball_sets", "GET");break;
                case "stones":connection.request("/stones","GET");break;
                case "ch":for(int i=0;i<50;i++)connection.request("/user/capacity/card", "POST");break;
                case "sum":for(int i=0;i<100;i++)connection.request("/gashas/579/courses/2/draw", "POST");break;
                case "friends":connection.request("/quests/" + 710001 + "/supporters", "GET");break;
                case "setEza":{
                	ezaVer = choose.substring(choose.indexOf(" ")+1);
                	connection.setResponse("EZA set to :" + ezaVer);
                };break;
                case "area":{
                	String area = choose.substring(choose.indexOf(" ")+1);
                	finishStory(connection,Integer.valueOf(area),session.uid);
                };break;
                case "eza":{
                	String start,last;
                	int ind1,ind2;
                	ind1 = choose.indexOf(" ");
                	ind2 = choose.indexOf(" ",ind1+1);
                	start = choose.substring(ind1+1,ind2);
                	last = choose.substring(ind2+1);
                	extremeZAwakening(connection,Integer.valueOf(start),Integer.valueOf(last));
                };break;
                case "event":{
                	String event = choose.substring(choose.indexOf(" ")+1);
                	event(connection, event);
                };break;
                case "eventk":{
                    connection.eventkagi = ",\"eventkagi_item_id\":1";
                    String event = choose.substring(choose.indexOf(" ")+1);
                    event(connection, event);
                    connection.eventkagi = "";
                }break;
                case "events":{
                    String[] wantedEvents = choose.split(" ");
                    for(int i = 1;i<wantedEvents.length; i++){
                        System.out.println("Doing event: " + wantedEvents[i]);
                        event(connection,wantedEvents[i]);
                    }
                    System.out.println("DONE");
                };break;
                case "custfarm":{
                	long startTime ;
                	int ind2 = choose.indexOf(" ",ind+1);
                	String stageId = choose.substring(ind+1,ind2);
                	int ind3 = choose.indexOf(" ",ind2+1);
                	connection.resp.setDifficulty(choose.substring(ind2+1,ind3));
                	int timesToFarm = Integer.valueOf(choose.substring(ind3+1));
                	System.out.println("Doing event id:" + stageId + ",on difficulty " + connection.resp.getDifficulty() + ", " + timesToFarm + " time(s)");
                	for(int i=0;i<timesToFarm;i++){
                	startTime = System.currentTimeMillis();
                	connection.request("/quests/" + stageId + "/supporters", "GET");
                	connection.request("/quests/" + stageId + "/sugoroku_maps/start", "POST");
                	connection.request("/quests/" + stageId + "/sugoroku_maps/finish", "POST");
                	System.out.println("Event done = " + (System.currentTimeMillis()-startTime));
                	System.out.println("Progress:" + (i+1) + "/" + timesToFarm);
                	}
                };break;
                case "custfarmk":{
                    long startTime;
                    connection.eventkagi = ",\"eventkagi_item_id\":3";
                    String[] options = choose.split(" ");
                    String stageId = options[1];
                    int diff = Integer.valueOf(options[2]);
                    int timesToFarm = Integer.valueOf(options[3]);
                    System.out.println("Doing event id:" + stageId + ",on difficulty " + connection.resp.getDifficulty() + ", " + timesToFarm + " time(s)");
                    for(int i=0;i<timesToFarm;i++){
                        startTime = System.currentTimeMillis();
                        quest(connection,stageId,diff);
                        System.out.println("Event done = " + (System.currentTimeMillis()-startTime));
                        System.out.println("Progress:" + (i+1) + "/" + timesToFarm);
                    }
                }break;
                case "allezas":{
                    for(int i = 1;i<17;i++){
                        ezaVer = String.valueOf(i);
                        if (i == 3) extremeZAwakening(connection,1,79);
                        else if(i==10) extremeZAwakening(connection,1,102);
                        else extremeZAwakening(connection,1,32);
                    }
                }break;
                case "nibba" : {
                    int diff = 3;
                    quest(connection,"26002",diff);
                    quest(connection,"26004",diff);
                    quest(connection,"26006",diff);
                    quest(connection,"26008",diff);
                    /*quest(connection,"21002",diff);
                    quest(connection,"20004",diff);
                    quest(connection,"20006",diff);
                    quest(connection,"20008",diff);*/
                }break;
                case "sbr":{
                    connection.resp.setDifficulty(5);
                    connection.teamSelected = 1;
                    quest(connection,"712001",5);
                    connection.teamSelected = 2;
                    quest(connection,"712001",5);
                    connection.teamSelected = 3;
                    quest(connection,"712001",5);
                    connection.teamSelected = 4;
                    quest(connection,"712001",5);
                    connection.teamSelected = 5;
                    quest(connection,"712001",5);
                    connection.teamSelected = 6;
                    quest(connection,"712001",5);
                    connection.teamSelected = 7;
                }break;
                case "punching":{
                    connection.punchingMachine = ",\"max_damage_to_boss\":99999999";
                    quest(connection,"711001",1);
                    quest(connection,"711002",1);
                    quest(connection,"711003",1);
                    quest(connection,"711004",1);
                    quest(connection,"711005",1);
                    quest(connection,"711006",1);
                    connection.punchingMachine ="";
                }break;
                case "dupe":{
                    String[] options = choose.split(" ");
                    String t = "";
                    if(!choose.contains("with")) {
                        String toDupeCardId = options[1];
                        t = options[2];
                        connection.toDupeCardId = toDupeCardId;
                        connection.request("/cards", "GET");
                        System.out.println("Duping initiated!");
                    }
                    else{
                        connection.toDupe = options[1];
                        connection.fodder = options[3];
                        t = options[4];
                        System.out.println("#2 Duping initiated!");
                    }
                    connection.dupeSuccess = 0;
                    DupeUnits a =  new DupeUnits(connection,connection.toDupe,t + "00002",connection.fodder,connection.baseRequest);
                    DupeUnits b = new DupeUnits(connection,connection.toDupe,t + "00003",connection.fodder,connection.baseRequest);
                    DupeUnits c = new DupeUnits(connection,connection.toDupe,t + "00004",connection.fodder,connection.baseRequest);
                    DupeUnits d = new DupeUnits(connection,connection.toDupe,t + "00044",connection.fodder,connection.baseRequest);
                    a.start();
                    b.start();
                    c.start();
                    d.start();
                    Thread.sleep(1500);
                    int success = a.dupeSuccess+b.dupeSuccess+c.dupeSuccess+d.dupeSuccess;
                    connection.responseMessage = "Finished duping! Successful path unlocked : " + success + ".Paths that failed or were already unlocked : " + (4-success);
                };break;
                case "use" :{
                    String[] options = choose.split(" ");
                    String owner = options[1];
                    AuthBasicFile[] all;
                    String line = connection.readFile("authbasic.json");
                    ObjectMapper ob = new ObjectMapper();
                    all = ob.readValue(line,AuthBasicFile[].class);
                    int l = all.length;
                    for(int i = 0;i<l;i++){
                        if(all[i].getOwner().equals(owner)){
                            connection.customAuthBasic = all[i].getAuthBasic();
                        }
                    }
                    connection.responseMessage = "Using " + owner;

                };break;
                case "add" :{
                    String[] options = choose.split(" ");
                    String owner = options[1];
                    String auth = options[2];
                    AuthBasicFile[] all;
                    String line = connection.readFile("authbasic.json");
                    ObjectMapper ob = new ObjectMapper();
                    all = ob.readValue(line,AuthBasicFile[].class);
                    ArrayList<AuthBasicFile> allList = new ArrayList<>(Arrays.asList(all));
                    allList.add(new AuthBasicFile(owner,auth));
                    line = ob.writerWithDefaultPrettyPrinter().writeValueAsString(allList);
                    connection.writeFile("authbasic.json",line,false);
                    connection.responseMessage = "Added " + owner;
                };break;
                case "dsa":{
                    String[] options = choose.split(" ");
                    String toTrain = options[1];
                    String toUse = options[2];
                    int amount = 10;
                    DupeSuperAttack[] dupeSummons = new DupeSuperAttack[amount];
                    for(int i = 0;i<amount;i++) dupeSummons[i] = new DupeSuperAttack(connection,toTrain,toUse,connection.baseRequest);
                    for(int i = 0;i<amount;i++) dupeSummons[i].start();
                    connection.responseMessage = "idk";
                };break;
                default: wrongCmd = true;
            }
            if(!wrongCmd) {
                session.statusMessage = connection.responseMessage;
            }
            else session.statusMessage = "Wrong command!";
        }
        
    }
    
    private void setSessionValue(AtmosphereResource resource, String name, Object value) {
        AtmosphereResourceSessionFactory factory = sessionFactory;
        AtmosphereResourceSession session = factory.getSession(resource);
        session.setAttribute(name, value);
     }

    private <T> T getSessionValue(AtmosphereResource resource, String name, Class<T> type) {
        AtmosphereResourceSessionFactory factory = sessionFactory;
        AtmosphereResourceSession session = factory.getSession(resource, false);
        T value = null;
        if (session != null) {
           value = session.getAttribute(name, type);
        }
        return value;
     }
    
    public static void getDatabase(Connect connect) throws Exception{
        connect.request("/client_assets/database","GET");
        //connect.request("/client_assets", "GET");
        
    }
    
    public static void finishTutorial(Connect connect) throws Exception{
    	connect.request("/tutorial/gasha","POST");
    	connect.request("/tutorial/finish","PUT");
    	connect.request("/tutorial","PUT");
    }
    
    public void resetTor() throws IOException, InterruptedException{
    	Thread.sleep(1500);
    	System.out.println("Reseting Tor");
		Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
		Thread.sleep(6000);
		Process p1 = Runtime.getRuntime().exec("C:\\Users\\steva\\Desktop\\Tor Browser\\Browser\\firefox.exe");
	    Thread.sleep(5000);
    }
    
    public void writeAccountsToFile(Connect connect) throws Exception{
    	connect.writeFile("accounts.txt", connect.accountsToFile, true);
    	connect.accountsToFile = "";
    }
    
    public void extremeZAwakening(Connect connect,int startStage,int lastStage) throws Exception{
    	for(int i = startStage;i<lastStage;i++){
    		System.out.println("Stage " + i);
    		connect.ezaStageNum = i;
    		connect.request("/quests/0/supporters", "GET");
    		connect.request("/z_battles/"+ ezaVer +"/start","POST");
    		connect.request("/z_battles/"+ ezaVer + "/finish","POST");
    		System.out.println();
    	}
    }

    public void quest(Connect connect, String stage, int diff) throws Exception{
        connect.resp.setDifficulty(diff);
        connect.request("/quests/" + stage + "/supporters", "GET");
        connect.request("/quests/" + stage + "/sugoroku_maps/start", "POST");
        connect.request("/quests/" + stage + "/sugoroku_maps/finish", "POST");
    }
    
    public void transferCode(Connect connection, String choose) throws Exception{
        String[] tc= choose.split(" ");
        connection.resp.setUserId(tc[1]);
        System.out.println("ID:" + tc[1] + "  code:" + tc[2]);
        System.out.println(connection.baseRequest.getServer());
        connection.request("/auth/link_codes/" + tc[2] +"/validate","POST");
        connection.request("/auth/link_codes/" + tc[2], "PUT");
        System.out.println("Transfer succesfull!");
        System.out.println(connection.baseRequest.getIdentifier());
        connection.request("/auth/sign_in","POST");
        System.out.println("Logged in succesfully!");
        connection.request("/auth/link_codes","POST");
        System.out.println("ID:" + tc[1] + " CODE:" + connection.resp.getLinkCode());
        connection.responseMessage = "transfer code " + tc[1] + " " + connection.resp.getLinkCode();
        connection.resp.setUserId(tc[1]);
    }

    public void event(Connect connection,String wantedEvent) throws Exception{
        connection.wantedEvent = Integer.valueOf(wantedEvent);
        connection.request("/user_areas","GET");
        int len = connection.areaEvent.size();
        for(int i=0;i<len;i++){
            String st = connection.areaEvent.get(i).substring(0,6);
            String diff = connection.areaEvent.get(i).substring(6);
            System.out.println("\tDoing stage " + st + " ,diff " + diff);
            FarmSession.quest(connection,st,diff);
        }
    }
    
    public void reroll(Connect connect,boolean isBossReroll) throws Exception{
    	for(int i=1;i>0;i++){
            connect.request("/auth/sign_up","POST");
            
        	Thread.sleep(17000);
            if(!connect.toReset) {
                connect.urlCount = 2;
                System.out.println("Captcha success!");
                connect.request("/auth/sign_up", "POST");
                connect.urlCount = 1;

                connect.request("/auth/sign_in", "POST");
                connect.request("/tutorial/gasha", "POST");
                connect.request("/tutorial/finish", "PUT");
                connect.request("/tutorial", "PUT");
                connect.request("/auth/link_codes", "POST");
                String toWrite = connect.resp.getId() + "|" + connect.resp.getLinkCode() + "|" + connect.chosenServer + "|android";
                connect.writeFile("accounts.txt", toWrite + "\r\n", true);
            }
    	}
    }
    
    public void bossRush(Connect connect) throws Exception{
    	connect.resp.setDifficulty("3");
        connect.request("/quests/701001/supporters", "GET");
        connect.request("/quests/701001/sugoroku_maps/start", "POST");
        connect.request("/quests/701001/sugoroku_maps/finish", "POST");
        connect.resp.setDifficulty("4");
        connect.request("/quests/701001/supporters", "GET");
        connect.request("/quests/701001/sugoroku_maps/start", "POST");
        connect.request("/quests/701001/sugoroku_maps/finish", "POST");
        
        connect.request("/quests/701002/supporters", "GET");
        connect.request("/quests/701002/sugoroku_maps/start", "POST");
        connect.request("/quests/701002/sugoroku_maps/finish", "POST");
        connect.resp.setDifficulty("5");
        connect.request("/quests/701002/supporters", "GET");
        connect.request("/quests/701002/sugoroku_maps/start", "POST");
        connect.request("/quests/701002/sugoroku_maps/finish", "POST");
        
        connect.request("/quests/701003/supporters", "GET");
        connect.request("/quests/701003/sugoroku_maps/start", "POST");
        connect.request("/quests/701003/sugoroku_maps/finish", "POST");
        
        connect.request("/quests/701004/supporters", "GET");
        connect.request("/quests/701004/sugoroku_maps/start", "POST");
        connect.request("/quests/701004/sugoroku_maps/finish", "POST");
        
        connect.request("/quests/701005/supporters", "GET");
        connect.request("/quests/701005/sugoroku_maps/start", "POST");
        connect.request("/quests/701005/sugoroku_maps/finish", "POST");
        
        connect.request("/quests/701006/supporters", "GET");
        connect.request("/quests/701006/sugoroku_maps/start", "POST");
        connect.request("/quests/701006/sugoroku_maps/finish", "POST");

        connect.request("/quests/701007/supporters", "GET");
        connect.request("/quests/701007/sugoroku_maps/start", "POST");
        connect.request("/quests/701007/sugoroku_maps/finish", "POST");

        connect.request("/missions","GET");
        connect.acceptMission();
        connect.request("/user","GET");
    }

    
    public void populateLists(Connect connect,List<String> stageIdList, List<String> diffList) throws Exception{
    	String all = "";
    	all = connect.readFile("storyfarm.txt");
    	int ind = all.indexOf(",");
    	String stage = all.substring(0,ind);
    	diffList.add(stage.substring(ind-1));
    	stageIdList.add(stage.substring(0, ind-1));
    	while(all.length()>7){
    		all = all.substring(ind+1);
    		ind = all.indexOf(","); 
    		stage = all.substring(0,ind);
        	diffList.add(stage.substring(ind-1));
        	stageIdList.add(stage.substring(0, ind-1));
    	}
    	
    	int len = diffList.size();
    	//System.out.println(len);
    }
    
    public void finishStory(Connect connect,int area,String uid) throws Exception{
        List<String> stageIdList = new ArrayList<String>();
        List<String> diffList = new ArrayList<String>();
    	int count = 0;
    	populateLists(connect,stageIdList,diffList);
    	if(area != 0){
    		int g = 1;
    		if(area>9) g = 2;
    		for(int i=0;i<diffList.size();i++){
    			if(stageIdList.get(i).substring(0,g).equals(String.valueOf(area))) {
    				count = i;
    				break;
    			}
    		}
    	}
    	System.out.println("There are " + (594-count) + " stages");
    	for(int i=count;i<diffList.size();i++){
        	connect.resp.setDifficulty(diffList.get(i));
        	long startTime = System.currentTimeMillis();
    		connect.request("/quests/" + stageIdList.get(i) + "/supporters", "GET");
    		connect.request("/quests/" + stageIdList.get(i) + "/sugoroku_maps/start", "POST");
    		connect.request("/quests/" + stageIdList.get(i) + "/sugoroku_maps/finish", "POST");
    		long endTime = System.currentTimeMillis();
    		System.out.println("[" + uid + "] " + stageIdList.get(i) + diffList.get(i) + " | " + (endTime-startTime) + "ms");
    	}
    }
  
    public final static class ChatMessage {

        private String message;
        private String author;
        private long time;

        public ChatMessage() {
            this("", "");
        }

        public ChatMessage(String author, String message) {
            this.author = author;
            this.message = message;
            this.time = new Date().getTime();
        }

        public String getMessage() {
            return message;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

    }
}