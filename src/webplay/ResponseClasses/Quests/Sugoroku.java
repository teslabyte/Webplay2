package webplay.ResponseClasses.Quests;

public class Sugoroku {
    private String map;
    private Events events;
    private int first_focus_step;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Events getEvents() {
        return events;
    }

    public void setEvents(Events events) {
        this.events = events;
    }

    public int getFirst_focus_step() {
        return first_focus_step;
    }

    public void setFirst_focus_step(int first_focus_step) {
        this.first_focus_step = first_focus_step;
    }
}
