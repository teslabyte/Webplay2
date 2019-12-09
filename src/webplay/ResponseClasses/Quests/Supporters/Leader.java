package webplay.ResponseClasses.Quests.Supporters;

import webplay.ResponseClasses.Cards.PotentialParametersRootObject;

import java.util.ArrayList;

public class Leader {
    private int id;
    private int card_id;
    private int exp;
    private int skill_lv;
    private boolean is_favorite;
    private boolean is_released_potential;
    private double released_rate;
    private int optimal_awakening_step;
    private ArrayList<Integer> awakenings;
    private ArrayList<Integer> unlocked_square_statuses;
    private int created_at;
    private ArrayList<PotentialParametersRootObject> potential_parameters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getSkill_lv() {
        return skill_lv;
    }

    public void setSkill_lv(int skill_lv) {
        this.skill_lv = skill_lv;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public boolean isIs_released_potential() {
        return is_released_potential;
    }

    public void setIs_released_potential(boolean is_released_potential) {
        this.is_released_potential = is_released_potential;
    }

    public double getReleased_rate() {
        return released_rate;
    }

    public void setReleased_rate(double released_rate) {
        this.released_rate = released_rate;
    }

    public int getOptimal_awakening_step() {
        return optimal_awakening_step;
    }

    public void setOptimal_awakening_step(int optimal_awakening_step) {
        this.optimal_awakening_step = optimal_awakening_step;
    }

    public ArrayList<Integer> getAwakenings() {
        return awakenings;
    }

    public void setAwakenings(ArrayList<Integer> awakenings) {
        this.awakenings = awakenings;
    }

    public ArrayList<Integer> getUnlocked_square_statuses() {
        return unlocked_square_statuses;
    }

    public void setUnlocked_square_statuses(ArrayList<Integer> unlocked_square_statuses) {
        this.unlocked_square_statuses = unlocked_square_statuses;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public ArrayList<PotentialParametersRootObject> getPotential_parameters() {
        return potential_parameters;
    }

    public void setPotential_parameters(ArrayList<PotentialParametersRootObject> potential_parameters) {
        this.potential_parameters = potential_parameters;
    }
}
