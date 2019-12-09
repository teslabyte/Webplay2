package webplay.ResponseClasses.UserAreas;

import java.util.ArrayList;

public class UserArea {
    private int area_id;
    private boolean is_cleared_normal;
    private boolean is_cleared_hard;
    private boolean is_cleared_very_hard;
    private ArrayList<UserSugorokuMap> user_sugoroku_maps;
    private boolean is_cleared_super_hard1;
    private boolean is_cleared_super_hard2;
    private boolean is_cleared_super_hard3;

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public boolean isIs_cleared_normal() {
        return is_cleared_normal;
    }

    public void setIs_cleared_normal(boolean is_cleared_normal) {
        this.is_cleared_normal = is_cleared_normal;
    }

    public boolean isIs_cleared_hard() {
        return is_cleared_hard;
    }

    public void setIs_cleared_hard(boolean is_cleared_hard) {
        this.is_cleared_hard = is_cleared_hard;
    }

    public boolean isIs_cleared_very_hard() {
        return is_cleared_very_hard;
    }

    public void setIs_cleared_very_hard(boolean is_cleared_very_hard) {
        this.is_cleared_very_hard = is_cleared_very_hard;
    }

    public ArrayList<UserSugorokuMap> getUser_sugoroku_maps() {
        return user_sugoroku_maps;
    }

    public void setUser_sugoroku_maps(ArrayList<UserSugorokuMap> user_sugoroku_maps) {
        this.user_sugoroku_maps = user_sugoroku_maps;
    }

    public boolean isIs_cleared_super_hard1() {
        return is_cleared_super_hard1;
    }

    public void setIs_cleared_super_hard1(boolean is_cleared_super_hard1) {
        this.is_cleared_super_hard1 = is_cleared_super_hard1;
    }

    public boolean isIs_cleared_super_hard2() {
        return is_cleared_super_hard2;
    }

    public void setIs_cleared_super_hard2(boolean is_cleared_super_hard2) {
        this.is_cleared_super_hard2 = is_cleared_super_hard2;
    }

    public boolean isIs_cleared_super_hard3() {
        return is_cleared_super_hard3;
    }

    public void setIs_cleared_super_hard3(boolean is_cleared_super_hard3) {
        this.is_cleared_super_hard3 = is_cleared_super_hard3;
    }
}
