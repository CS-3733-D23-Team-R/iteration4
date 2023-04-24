package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class DirectionArrow implements Archivable {
    String longName;
    int kioskID;
    Direction direction;

    DirectionArrow(String longName, int kioskID, Direction direction){
        this.longName = longName;
        this.kioskID = kioskID;
        this.direction = direction;
    }

    private DirectionArrow(String[] args) throws IndexOutOfBoundsException {
        this(args[0], Integer.parseInt(args[1]), Direction.valueOf(args[3]));
    }

    @Override
    public String toCSVEntry() {
        return longName + "," + kioskID + "," + direction;
    }

    @Override
    public String getCSVColumns() {
        return "longName,kioskID,direction";
    }
}
