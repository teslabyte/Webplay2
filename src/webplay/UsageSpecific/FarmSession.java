package webplay.UsageSpecific;

import webplay.Network.Connect;

import java.util.ArrayList;
import java.util.List;

public class FarmSession extends Thread{
    Connect connection;
    static String uid;

    public FarmSession(Connect connect, String uid){
        connection = connect;
        this.uid = uid;
    }

    public void run(){
        try {
            finishStory(connection, 0);
            bossRush(connection);
            connection.responseMessage = "Farming is finished!";
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void populateLists(Connect connect, List<String> stageIdList, List<String> diffList) throws Exception{
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

    public void finishStory(Connect connect,int area) throws Exception{
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
        for(int i=count;i<diffList.size();i++){
            quest(connect, stageIdList.get(i), diffList.get(i));
        }
    }

    public void bossRush(Connect connect) throws Exception{
        quest(connect,"701001","3");
        quest(connect,"701001","4");
        quest(connect,"701002","4");
        quest(connect,"701002","5");
        quest(connect,"701003","5");
        quest(connect,"701004","5");
        quest(connect,"701005","5");
        quest(connect,"701006","5");

        connect.request("/missions","GET");
        connect.acceptMission();
        System.out.println("FARMING IS FINISHED\n------------------");
        String finishedTc = connect.resp.getUserId() + "," + connect.resp.getLinkCode();
        String ser = connect.whichServer() ? "jp" : "glb";
        connect.writeFile("FarmedAccounts.txt",finishedTc + "|" + ser + "|" + connect.baseRequest.getOsVersion() + "\r\n",true);
    }

    public static void quest(Connect connect, String stageId, String difficulty) throws Exception{
        connect.resp.setDifficulty(difficulty);
        long startTime = System.currentTimeMillis();
        connect.request("/quests/" + stageId + "/supporters", "GET");
        connect.request("/quests/" + stageId + "/sugoroku_maps/start", "POST");
        connect.request("/quests/" + stageId + "/sugoroku_maps/finish", "POST");
        long endTime = System.currentTimeMillis();
        System.out.println("[" + uid + "] " + stageId + difficulty + " | " + (endTime-startTime) + "ms");
    }
}
