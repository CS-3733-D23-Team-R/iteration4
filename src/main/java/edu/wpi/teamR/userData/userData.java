package edu.wpi.teamR.userData;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class userData {
    static userData instance;
    currentUser loggedIn;
    private userData(){}
    public static userData getInstance(){
        if(instance == null){
            instance = new userData();
        }
        return instance;
    }
}
