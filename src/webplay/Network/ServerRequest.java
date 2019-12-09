package webplay.Network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerRequest {
    private BaseRequest baseRequest = new BaseRequest();
    private ServerResponseHandler serverResponseHandler = new ServerResponseHandler();
    private ServerRequestBodyHandler serverRequestBodyHandler = new ServerRequestBodyHandler();

    public String Request(String request, String requestType) throws Exception{
        //long startTime = System.currentTimeMillis();
        if(request.equals("/auth/sign_in")){
            baseRequest.requestVersion = 3;
        }
        URL url = new URL("https://" + baseRequest.getServer() + request);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        if(!requestType.equals("POST"))conn.setDoOutput(true);

        conn.setRequestMethod(requestType);

        baseRequest.getBaseHeaders(conn);

        addHeaders(conn,request,requestType);
        if(requestType.equals("GET"))conn.connect();
        if(requestType.equals("POST"))conn.setDoOutput(true);
        if(!requestType.equals("GET")) serverRequestBodyHandler.handleServerRequestBody(conn,request);
        if(requestType.equals("POST"))conn.connect();
        return readResponse(conn,request,requestType);
    }

    public void request(String url, String requestType) throws Exception{
        String reqResponse = Request(url, requestType);

        if(reqResponse.startsWith("{\"error\"")){
            System.out.println("starts with error : " + reqResponse);
            serverResponseHandler.HandleServerErrors(reqResponse,url,requestType,400);
        }
        else{
            serverResponseHandler.HandleServerResponse(reqResponse, url);
        }

        //setResponse(reqResponse);
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

    private void addHeaders(HttpURLConnection conn,String request,String requestType){

    }
}
