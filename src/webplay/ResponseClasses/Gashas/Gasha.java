package webplay.ResponseClasses.Gashas;

import java.util.ArrayList;

public class Gasha {
    private int id;
    private String type;
    private String name;
    private String description;
    private boolean is_display_remaining_time;
    private boolean is_rate_visible;
    private int end_at;
    private int current_step;
    private String banner_url;
    private String mypage_banner_url;
    private ArrayList<Course> courses;
    private int treasure_item_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_display_remaining_time() {
        return is_display_remaining_time;
    }

    public void setIs_display_remaining_time(boolean is_display_remaining_time) {
        this.is_display_remaining_time = is_display_remaining_time;
    }

    public boolean isIs_rate_visible() {
        return is_rate_visible;
    }

    public void setIs_rate_visible(boolean is_rate_visible) {
        this.is_rate_visible = is_rate_visible;
    }

    public int getEnd_at() {
        return end_at;
    }

    public void setEnd_at(int end_at) {
        this.end_at = end_at;
    }

    public int getCurrent_step() {
        return current_step;
    }

    public void setCurrent_step(int current_step) {
        this.current_step = current_step;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getMypage_banner_url() {
        return mypage_banner_url;
    }

    public void setMypage_banner_url(String mypage_banner_url) {
        this.mypage_banner_url = mypage_banner_url;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public int getTreasure_item_id() {
        return treasure_item_id;
    }

    public void setTreasure_item_id(int treasure_item_id) {
        this.treasure_item_id = treasure_item_id;
    }
}
