package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter(AccessLevel.PACKAGE)
public class DirectionArrow {
    String longname;
    int kioskID;
    Direction direction;
    Date date;

    DirectionArrow(String longname, int kioskID, Direction direction, Date date){
        this.longname = longname;
        this.kioskID = kioskID;
        this.direction = direction;
        this.date = date;
    }
}
