package webplay.Network;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class ResponseHandler {
    private String id = "";
    private String linkCode = "";
    private String transferId = "";
    private String signIn = "";
    private String signUp = "";
    private String transferCode = "";
    private String friendId = "";
    private String questId = "";
    private String token = "";
    private String steps = "";
    private String difficulty = "2";
    private String userId = "";
    private List<String> supporterIds = new ArrayList<String>();

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getLinkCode() {

        return linkCode;
    }

    public void setLinkCode(String linkCode) {
        this.linkCode = linkCode;
    }

    public String getTransferId() {

        return transferId;
    }

    public void setTransferId(String transferId) {

        this.transferId = transferId;
    }

    public String getSignIn() {

        return signIn;
    }

    public void setSignIn(String signIn) {

        this.signIn = signIn;
    }

    public String getSignUp() {

        return signUp;
    }

    public void setSignUpWithId(String ad_id, String unique_id) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();

        JsonNode childNode1 = mapper.createObjectNode();
        ((ObjectNode) childNode1).put("ad_id", ad_id);
        ((ObjectNode) childNode1).put("country", "RS");
        ((ObjectNode) childNode1).put("currency", "RSD");
        ((ObjectNode) childNode1).put("device", "HUAWEI");
        ((ObjectNode) childNode1).put("device_model", "HUAWEI G7-L01");
        ((ObjectNode) childNode1).put("os_version", "8.0.0");
        ((ObjectNode) childNode1).put("platform", "android");
        ((ObjectNode) childNode1).put("unique_id", unique_id);
        ((ObjectNode) rootNode).set("user_account", childNode1);
        this.signUp = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode).substring(1);
    }

    public void setSignUp(String signUp) {
        this.signUp = signUp;
    }

    public String getTransferCode() {

        return transferCode;
    }

    public void setTransferCode(String transferCode) {

        this.transferCode = transferCode;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = String.valueOf(difficulty);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSupporterIds(List<String> supporterIdsC) {
        this.supporterIds = new ArrayList<String>(supporterIdsC);
    }

    public String getSupporterId() {
        System.out.println(supporterIds.size() + " friends left");
        String tempId = supporterIds.get(0);
        supporterIds.remove(0);
        return tempId;
    }

    public boolean isSupporterIdListEmpty(){
        return supporterIds.isEmpty();
    }
}
