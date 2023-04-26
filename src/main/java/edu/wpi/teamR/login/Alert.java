package edu.wpi.teamR.login;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class Alert implements Archivable {
    private String message;
    private Timestamp time;

    public Alert(String message, Timestamp time){
        this.message = message;
        this.time = time;
    }

    private Alert(String[] args) {
        this(args[0], Timestamp.valueOf(args[1]));
    }

    @Override
    public String toCSVEntry() {
        return message + "," + time.toString();
    }

    @Override
    public String getCSVColumns() {
        return "message,time";
    }
}
