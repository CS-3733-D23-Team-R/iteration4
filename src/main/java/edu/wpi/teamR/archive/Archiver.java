package edu.wpi.teamR.archive;

import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class Archiver {
    private MapDatabase mapdb;
    private RequestDatabase requestdb;

    public Archiver(MapDatabase mapDatabase, RequestDatabase requestDatabase) {
        mapdb = mapDatabase;
        requestdb = requestDatabase;
    }

    public void createArchive(String archivePath) throws SQLException {
        try (ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(new File(archivePath))) {
            CSVWriter writer = new CSVWriter();
            outputStream.putArchiveEntry(new ZipArchiveEntry("Node.csv"));
            writer.writeCSV(outputStream, mapdb.getNodes());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("Edge.csv"));
            writer.writeCSV(outputStream, mapdb.getEdges());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("Move.csv"));
            writer.writeCSV(outputStream, mapdb.getMoves());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("LocationName.csv"));
            writer.writeCSV(outputStream, mapdb.getLocationNames());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("DirectionArrow.csv"));
            writer.writeCSV(outputStream, mapdb.getDirectionArrows());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("ConferenceRoom.csv"));
            writer.writeCSV(outputStream, mapdb.getConferenceRooms());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("ItemRequest.csv"));
            writer.writeCSV(outputStream, requestdb.getItemRequests());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("RoomRequest.csv"));
            writer.writeCSV(outputStream, requestdb.getRoomRequests());
            outputStream.closeArchiveEntry();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void restoreArchive(String archivePath) {

    }
}
