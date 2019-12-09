package webplay.ResponseClasses.Quests.Supporters;

public class Supporter {
    private long id;
    private String name;
    private int rank;
    private int login_at;
    private boolean is_friend;
    private int gasha_point;
    private Leader leader;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLogin_at() {
        return login_at;
    }

    public void setLogin_at(int login_at) {
        this.login_at = login_at;
    }

    public boolean isIs_friend() {
        return is_friend;
    }

    public void setIs_friend(boolean is_friend) {
        this.is_friend = is_friend;
    }

    public int getGasha_point() {
        return gasha_point;
    }

    public void setGasha_point(int gasha_point) {
        this.gasha_point = gasha_point;
    }

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }
}
