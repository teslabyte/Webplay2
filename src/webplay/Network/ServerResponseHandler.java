package webplay.Network;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import webplay.ResponseClasses.*;
import webplay.ResponseClasses.Cards.CardsRootObject;
import webplay.ResponseClasses.Gashas.GashasRootObject;
import webplay.ResponseClasses.Missions.MissionsRootObject;
import webplay.ResponseClasses.Quests.QuestInfo;
import webplay.ResponseClasses.Quests.StartRootObject;
import webplay.ResponseClasses.Quests.StartSign;
import webplay.ResponseClasses.Quests.Supporters.SupportersRootObject;
import webplay.ResponseClasses.UserAreas.UserAreasRootObject;
import webplay.Security.Decryptor;

public class ServerResponseHandler {

    private ObjectMapper objectMapper;
    private ClientInfo clientInfo;
    private QuestInfo questInfo;
    private Object obj;
    private JSONObject jsonObject;
    private Decryptor decryptor;

    public ServerResponseHandler(){
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        clientInfo = new ClientInfo();
        questInfo = new QuestInfo();
        decryptor = new Decryptor();
    }

    public void HandleServerResponse(String serverResponse,String serverResponseUrl) throws Exception{
        String[] urlParts = serverResponseUrl.split("/");

        switch(urlParts[0]){
            case "auth": {
                switch(urlParts[1]){
                    case "sign_in":{
                        AuthSignIn authSignIn = objectMapper.readValue(serverResponse,AuthSignIn.class);
                        clientInfo.setAuthSignIn(authSignIn);
                    }break;
                    case "sign_up":break;
                    default : break;
                }
            }break;
            case "quests":{
                if(urlParts[2].equals("supporters")){
                    SupportersRootObject supportersRootObject = objectMapper.readValue(String.valueOf(jsonObject), SupportersRootObject.class);
                    questInfo.setSupportersRootObject(supportersRootObject);
                }
                else if(urlParts[3].equals("start")){
                    StartSign startSign = objectMapper.readValue(String.valueOf(jsonObject),StartSign.class);
                    StartRootObject startRootObject = objectMapper.readValue(decryptor.decrypt(startSign.getSign()),StartRootObject.class);
                    questInfo.setStartRootObject(startRootObject);
                    //Za korake nema drugog resenja osim da ostane staro
                }
            } break;
            case "user":{
                UserInfo userInfo = new UserInfo();
                User user = objectMapper.readValue(serverResponse,User.class);
                userInfo.setUser(user);
                clientInfo.setUserInfo(userInfo);
            } break;
            case "areas":{
                UserAreasRootObject rootObject = objectMapper.readValue(serverResponse, UserAreasRootObject.class);
                clientInfo.setUserAreasRootObject(rootObject);
            } break;
            case "missions":{
                MissionsRootObject missionRootObject = objectMapper.readValue(serverResponse, MissionsRootObject.class);
                clientInfo.setMissionRootObject(missionRootObject);
            } break;
            case "cards":{
                CardsRootObject cardsRootObject = objectMapper.readValue(serverResponse,CardsRootObject.class);
                clientInfo.setCardsRootObject(cardsRootObject);
            } break;
            case "gashas":{
                GashasRootObject gashasRootObject = objectMapper.readValue(serverResponse,GashasRootObject.class);
                clientInfo.setGashasRootObject(gashasRootObject);
            } break;
            default : return;
        }
        //return clientInfo
    }

    public void HandleServerErrors(String errorCode,String request,String requestType,int responseCode){

    }

    private JSONObject parseStringResponseToJson(String serverResponse){
        obj = JSONValue.parse(serverResponse);
        jsonObject = (JSONObject) obj;
        return jsonObject;
    }
}
