package edu.wpi.teamR.login;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter @Setter
public class Alert implements Archivable {
    private String message;
    private Date startDate;
    private Date endDate;

    public Alert(String message, Date startDate, Date endDate){
        this.message = message;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private Alert(String[] args) {
        this(args[0], Date.valueOf(args[1]), Date.valueOf(args[2]));
    }

    @Override
    public String[] toCSVEntry() {
        return new String[]{message, startDate.toString(), endDate.toString()};
    }

    @Override
    public String getCSVColumns() {
        return "message,startDate,endDate";
    }
}
