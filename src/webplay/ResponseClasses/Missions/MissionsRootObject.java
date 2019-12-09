package webplay.ResponseClasses.Missions;

import java.util.ArrayList;

public class MissionsRootObject {
    private ArrayList<Mission> missions;
    private int processed_at;

    public ArrayList<Mission> getMissions() {
        return missions;
    }

    public void setMissions(ArrayList<Mission> missions) {
        this.missions = missions;
    }

    public int getProcessed_at() {
        return processed_at;
    }

    public void setProcessed_at(int processed_at) {
        this.processed_at = processed_at;
    }
}
