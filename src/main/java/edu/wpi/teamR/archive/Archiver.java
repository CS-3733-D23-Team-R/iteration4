package edu.wpi.teamR.archive;

import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import edu.wpi.teamR.requestdb.RoomRequest;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

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

    public void restoreArchive(String archivePath) throws SQLException, CSVParameterException {
        try (ZipFile zipFile = new ZipFile(archivePath)) {
            deleteALL();
            CSVReader reader = new CSVReader();
            ZipArchiveEntry entry;
            entry = zipFile.getEntry("Node.csv");
            System.out.println(entry.getName() + ": " + entry.getSize());
            mapdb.addNodes(reader.parseCSV(Node.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("Edge.csv");
            mapdb.addEdges(reader.parseCSV(Edge.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("LocationName.csv");
            mapdb.addLocationNames(reader.parseCSV(LocationName.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("Move.csv");
            mapdb.addMoves(reader.parseCSV(Move.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("DirectionArrow.csv");
            mapdb.addDirectionArrows(reader.parseCSV(DirectionArrow.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("ConferenceRoom.csv");
            mapdb.addConferenceRooms(reader.parseCSV(ConferenceRoom.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("ItemRequest.csv");
            requestdb.addItemRequests(reader.parseCSV(ItemRequest.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("RoomRequest.csv");
            requestdb.addRoomRequests(reader.parseCSV(RoomRequest.class, zipFile.getInputStream(entry)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void deleteALL() throws SQLException {
        requestdb.deleteAllItemRequests();
        requestdb.deleteAllRoomRequests();
        mapdb.deleteAllEdges();
        mapdb.deleteAllMoves();
        mapdb.deleteAllDirectionArrows();
        mapdb.deleteAllConferenceRooms();
        mapdb.deleteAllLocationNames();
        mapdb.deleteAllNodes();
    }
}
