package webplay.ResponseClasses.Quests;

import webplay.ResponseClasses.Quests.Supporters.SupportersRootObject;

public class QuestInfo {
    private SupportersRootObject supportersRootObject;
    private StartRootObject startRootObject;

    public SupportersRootObject getSupportersRootObject() {
        return supportersRootObject;
    }

    public void setSupportersRootObject(SupportersRootObject supportersRootObject) {
        this.supportersRootObject = supportersRootObject;
    }

    public StartRootObject getStartRootObject() {
        return startRootObject;
    }

    public void setStartRootObject(StartRootObject startRootObject) {
        this.startRootObject = startRootObject;
    }
}
