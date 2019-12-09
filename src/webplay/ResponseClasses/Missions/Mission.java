package webplay.ResponseClasses.Missions;

public class Mission {
    private int id;
    private int current_value;
    private int mission_id;
    private int mission_category_id;
    private int area_id;
    private int completed_at;
    private int accepted_reward_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrent_value() {
        return current_value;
    }

    public void setCurrent_value(int current_value) {
        this.current_value = current_value;
    }

    public int getMission_id() {
        return mission_id;
    }

    public void setMission_id(int mission_id) {
        this.mission_id = mission_id;
    }

    public int getMission_category_id() {
        return mission_category_id;
    }

    public void setMission_category_id(int mission_category_id) {
        this.mission_category_id = mission_category_id;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }

    public int getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(int completed_at) {
        this.completed_at = completed_at;
    }

    public int getAccepted_reward_at() {
        return accepted_reward_at;
    }

    public void setAccepted_reward_at(int accepted_reward_at) {
        this.accepted_reward_at = accepted_reward_at;
    }
}
