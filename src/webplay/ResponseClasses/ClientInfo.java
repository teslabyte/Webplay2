package webplay.ResponseClasses;

import webplay.ResponseClasses.Cards.CardsRootObject;
import webplay.ResponseClasses.Gashas.GashasRootObject;
import webplay.ResponseClasses.Missions.MissionsRootObject;
import webplay.ResponseClasses.UserAreas.UserAreasRootObject;
import webplay.Security.CustomLogger;

public class ClientInfo {
    private AuthSignIn authSignIn;
    private Tutorial tutorial;
    private UserInfo userInfo;
    private UserAreasRootObject userAreasRootObject;
    private MissionsRootObject missionRootObject;
    private CardsRootObject cardsRootObject;
    private GashasRootObject gashasRootObject;

    public AuthSignIn getAuthSignIn() {
        return authSignIn;
    }

    public void setAuthSignIn(AuthSignIn authSignIn) {
        this.authSignIn = authSignIn;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }

    public void setTutorial(Tutorial tutorial) {
        this.tutorial = tutorial;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserAreasRootObject getUserAreasRootObject() {
        return userAreasRootObject;
    }

    public void setUserAreasRootObject(UserAreasRootObject userAreasRootObject) {
        this.userAreasRootObject = userAreasRootObject;
    }

    public MissionsRootObject getMissionRootObject() {
        return missionRootObject;
    }

    public void setMissionRootObject(MissionsRootObject missionRootObject) {
        this.missionRootObject = missionRootObject;
    }

    public CardsRootObject getCardsRootObject() {
        return cardsRootObject;
    }

    public void setCardsRootObject(CardsRootObject cardsRootObject) {
        this.cardsRootObject = cardsRootObject;
    }

    public GashasRootObject getGashasRootObject() {
        return gashasRootObject;
    }

    public void setGashasRootObject(GashasRootObject gashasRootObject) {
        this.gashasRootObject = gashasRootObject;
    }

}
