package webplay.ResponseClasses.Quests;

public class StartRootObject {
    private String token;
    private int dummy_card_id;
    private Sugoroku sugoroku;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDummy_card_id() {
        return dummy_card_id;
    }

    public void setDummy_card_id(int dummy_card_id) {
        this.dummy_card_id = dummy_card_id;
    }

    public Sugoroku getSugoroku() {
        return sugoroku;
    }

    public void setSugoroku(Sugoroku sugoroku) {
        this.sugoroku = sugoroku;
    }
}
