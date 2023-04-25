package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class LocationName implements MapData, Archivable {
    private String longName, shortName, nodeType;

    public LocationName(String longName, String shortName, String nodeType){
        this.longName = longName;
        this.shortName = shortName;
        this.nodeType = nodeType;
    }

    private LocationName(String[] args) throws IndexOutOfBoundsException{
        this(args[0], args[1], args[2]);
    }

    @Override
    public String toCSVEntry() {
        return longName + "," +shortName + "," + nodeType;
    }

    @Override
    public String getCSVColumns() {
        return "longName,shortName,nodeType";
    }

    @Override
    public MapDataType getDataType() {
        return MapDataType.LOCATION_NAME;
    }
}
