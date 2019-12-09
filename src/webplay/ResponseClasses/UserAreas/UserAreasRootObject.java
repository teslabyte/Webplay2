package webplay.ResponseClasses.UserAreas;

import java.util.ArrayList;

public class UserAreasRootObject {
    private ArrayList<UserArea> user_areas;
    private ArrayList<UserZBattle> user_z_battles;

    public ArrayList<UserArea> getUser_areas() {
        return user_areas;
    }

    public void setUser_areas(ArrayList<UserArea> user_areas) {
        this.user_areas = user_areas;
    }

    public ArrayList<UserZBattle> getUser_z_battles() {
        return user_z_battles;
    }

    public void setUser_z_battles(ArrayList<UserZBattle> user_z_battles) {
        this.user_z_battles = user_z_battles;
    }
}
