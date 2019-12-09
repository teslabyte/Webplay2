package webplay.Files;

public class AuthBasicFile {
    private String owner;
    private String authBasic;

    public AuthBasicFile(String owner, String authBasic){
        this.owner = owner;
        this.authBasic = authBasic;
    }
    public AuthBasicFile(){

    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAuthBasic() {
        return authBasic;
    }

    public void setAuthBasic(String authBasic) {
        this.authBasic = authBasic;
    }
}
