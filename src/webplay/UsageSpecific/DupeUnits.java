package webplay.UsageSpecific;


import webplay.Network.BaseRequest;
import webplay.Network.Connect;
import webplay.Security.Identification;

public class DupeUnits extends Thread{
    private Connect connect;
    private String toDupe;
    private String num;
    private String fodder;
    private String server;
    private BaseRequest baseRequest;
    public int dupeSuccess = 0;

    public void run(){
        try {
            connect.fodder = fodder;
            System.out.println("Request sent at - " + System.currentTimeMillis());
            String requestResponse = connect.Request("/user_cards/" + toDupe + "/potentials/" + num + "/unlock", "POST");
            System.out.println("Response arrive at - " + System.currentTimeMillis());
            if (!requestResponse.startsWith("{\"error\"")) {
                connect.dupeSuccess++;
                dupeSuccess = 1;
            }
        }catch (Exception e){
            System.out.println("Exception in Async : " + e.getMessage());
        }
    }

    public DupeUnits(Connect connection, String toDupe, String num, String fodder, BaseRequest baseRequest){
        connect = new Connect();
        this.toDupe = toDupe;
        this.num = num;
        this.fodder = fodder;
        connect.baseRequest = baseRequest;
        connection.auth = new Identification();
        server = baseRequest.getServer();
        this.baseRequest = baseRequest;
    }
}
