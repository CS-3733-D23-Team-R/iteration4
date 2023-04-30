package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Move implements MapData, Archivable {

    private int nodeID;
    private String longName;
    private Date moveDate;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
    public Move(int nodeID, String longName, Date moveDate){
        this.nodeID = nodeID;
        this.longName = longName;
        this.moveDate = moveDate;
    }

    private Move(String[] args) throws IndexOutOfBoundsException {
        this(Integer.parseInt(args[0]), args[1], Date.valueOf(args[2]));
    }

    private static Date getDateFromString(String dateString){
        try {
            return new Date(dateFormat.parse(dateString).getTime());
        }
        catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String[] toCSVEntry() {
        return new String[]{String.valueOf(nodeID), longName, String.valueOf(moveDate)};
    }

    @Override
    public String getCSVColumns() {
        return "nodeID,longName,date";
    }

    @Override
    public MapDataType getDataType() {
        return MapDataType.MOVE;
    }
}
