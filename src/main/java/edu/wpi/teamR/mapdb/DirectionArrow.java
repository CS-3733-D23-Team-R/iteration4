package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter(AccessLevel.PACKAGE)
public class DirectionArrow implements Archivable {
    String longName;
    int kioskID;
    Direction direction;
    Date date;

    DirectionArrow(String longName, int kioskID, Direction direction, Date date){
        this.longName = longName;
        this.kioskID = kioskID;
        this.direction = direction;
        this.date = date;
    }

    private DirectionArrow(String[] args) throws IndexOutOfBoundsException {
        this(args[0], Integer.parseInt(args[1]), Direction.valueOf(args[2]), Date.valueOf(args[3]));
    }

    @Override
    public String toCSVEntry() {
        return longName + "," + kioskID + "," + direction + "," + date.toString();
    }

    @Override
    public String getCSVColumns() {
        return "longName,kioskID,direction,date";
    }
}
