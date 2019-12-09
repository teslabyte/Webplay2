package webplay.ResponseClasses.Quests.Supporters;

import java.util.ArrayList;

public class SupportersRootObject {
    public ArrayList<Supporter> supporters;
    //public CpuSupporters cpu_supporters;


    public ArrayList<Supporter> getSupporters() {
        return supporters;
    }

    public void setSupporters(ArrayList<Supporter> supporters) {
        this.supporters = supporters;
    }
}
