package webplay.UsageSpecific;

import webplay.Network.BaseRequest;
import webplay.Network.Connect;
import webplay.Security.Identification;

public class DupeSuperAttack extends Thread{
    private Connect connect;
    private String toTrain;
    private String fodder;
    private String server;
    private BaseRequest baseRequest;
    public int summonSuccess = 0;

    public void run(){
        try {
            System.out.println("Request sent at - " + System.currentTimeMillis());
            connect.fodder = fodder;
            String requestResponse = connect.Request("/cards/" + toTrain + "/train", "PUT");
            System.out.println("Response arrive at - " + System.currentTimeMillis());
            if (!requestResponse.startsWith("{\"error\"")) {
                summonSuccess = 1;
            }
        }catch (Exception e){
            System.out.println("Exception in Async : " + e.getMessage());
        }
    }

    public DupeSuperAttack(Connect connection, String toTrain,String fodder, BaseRequest baseRequest){
        connect = new Connect();
        this.toTrain = toTrain;
        this.fodder = fodder;
        connect.baseRequest = baseRequest;
        connection.auth = new Identification();
        this.server = baseRequest.getServer();
        this.baseRequest = baseRequest;
    }
}
