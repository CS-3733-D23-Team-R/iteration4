package edu.wpi.teamR.userData;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class UserData {
    static UserData instance;
    CurrentUser loggedIn;
    private UserData(){}
    public static UserData getInstance(){
        if(instance == null){
            instance = new UserData();
        }
        return instance;
    }
    public void logout(){loggedIn = null;}
    public boolean isLoggedIn(){ return loggedIn!=null;}
}
