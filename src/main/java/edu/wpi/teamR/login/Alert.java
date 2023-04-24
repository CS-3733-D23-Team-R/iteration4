package edu.wpi.teamR.login;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class Alert {
    private String message;
    private Timestamp time;

    public Alert(String message, Timestamp time){
        this.message = message;
        this.time = time;
    }
}
