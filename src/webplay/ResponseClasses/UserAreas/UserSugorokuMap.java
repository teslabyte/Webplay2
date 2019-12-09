package webplay.ResponseClasses.UserAreas;

public class UserSugorokuMap {
    private int sugoroku_map_id;
    private int visited_count;
    private int cleared_count;
    private int challenge_label;

    public int getSugoroku_map_id() {
        return sugoroku_map_id;
    }

    public void setSugoroku_map_id(int sugoroku_map_id) {
        this.sugoroku_map_id = sugoroku_map_id;
    }

    public int getVisited_count() {
        return visited_count;
    }

    public void setVisited_count(int visited_count) {
        this.visited_count = visited_count;
    }

    public int getCleared_count() {
        return cleared_count;
    }

    public void setCleared_count(int cleared_count) {
        this.cleared_count = cleared_count;
    }

    public int getChallenge_label() {
        return challenge_label;
    }

    public void setChallenge_label(int challenge_label) {
        this.challenge_label = challenge_label;
    }
}
