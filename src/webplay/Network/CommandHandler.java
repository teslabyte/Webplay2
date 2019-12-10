package webplay.Network;

public class CommandHandler {

    private String[] commands;
    private String returnString = "";

    public CommandHandler(){

    }

    public String handleCommands(String command,ServerRequest serverRequest){
        commands = command.split(" ");

        switch(commands[0]){
            case "user": {
                serverRequest.request("/user","GET");
                returnString=  serverRequest.clientInfo.getUserInfo().getUser().getName();
            }break;
            case "signin": serverRequest.request("/auth/sign_in","POST");break;
            case "cards": serverRequest.request("/cards","GET");break;
            case "areas":break;
            case "missions":break;
            case "gashas":break;
            default : return "error";
        }
        return returnString;
    }
}
