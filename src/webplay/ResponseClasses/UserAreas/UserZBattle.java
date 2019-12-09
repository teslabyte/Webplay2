package webplay.ResponseClasses.UserAreas;

public class UserZBattle {
    private int z_battle_stage_id;
    private int max_clear_level;
    private String challenge_label;

    public int getZ_battle_stage_id() {
        return z_battle_stage_id;
    }

    public void setZ_battle_stage_id(int z_battle_stage_id) {
        this.z_battle_stage_id = z_battle_stage_id;
    }

    public int getMax_clear_level() {
        return max_clear_level;
    }

    public void setMax_clear_level(int max_clear_level) {
        this.max_clear_level = max_clear_level;
    }

    public String getChallenge_label() {
        return challenge_label;
    }

    public void setChallenge_label(String challenge_label) {
        this.challenge_label = challenge_label;
    }
}
