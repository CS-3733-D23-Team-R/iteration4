package edu.wpi.teamR.archive;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.requestdb.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Archiver {
    private final MapDatabase mapdb;
    private final RequestDatabase requestdb;
    private final UserDatabase userdb;

    public Archiver(MapDatabase mapDatabase, UserDatabase userDatabase, RequestDatabase requestDatabase) {
        mapdb = mapDatabase;
        requestdb = requestDatabase;
        userdb = userDatabase;
    }

    public void createArchive() throws SQLException, IOException {
        StringBuilder filename = new StringBuilder();
        Path p = Paths.get("./backups/");
        File backupDir = new File(p.toUri());
        if (!backupDir.exists())
            Files.createDirectory(p);
        filename.append("./backups/csv_archive_");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh.mm.ss");
        filename.append(dateTime.format(formatter));
        filename.append(".zip");
        createArchive(filename.toString());
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
            outputStream.putArchiveEntry(new ZipArchiveEntry("User.csv"));
            writer.writeCSV(outputStream, userdb.getUsers());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("Alert.csv"));
            writer.writeCSV(outputStream, userdb.getAlerts());
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
            outputStream.putArchiveEntry(new ZipArchiveEntry("AvailableFlowers.csv"));
            writer.writeCSV(outputStream, requestdb.getAvailableFlowers());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("AvailableFurniture.csv"));
            writer.writeCSV(outputStream, requestdb.getAvailableFurniture());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("AvailableMeals.csv"));
            writer.writeCSV(outputStream, requestdb.getAvailableMeals());
            outputStream.closeArchiveEntry();
            outputStream.putArchiveEntry(new ZipArchiveEntry("AvailableSupplies.csv"));
            writer.writeCSV(outputStream, requestdb.getAvailableSupplies());
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
            mapdb.addNodes(reader.parseCSV(Node.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("Edge.csv");
            mapdb.addEdges(reader.parseCSV(Edge.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("LocationName.csv");
            mapdb.addLocationNames(reader.parseCSV(LocationName.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("Move.csv");
            mapdb.addMoves(reader.parseCSV(Move.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("User.csv");
            userdb.addUsers(reader.parseCSV(User.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("Alert.csv");
            userdb.addAlerts(reader.parseCSV(Alert.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("DirectionArrow.csv");
            mapdb.addDirectionArrows(reader.parseCSV(DirectionArrow.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("ConferenceRoom.csv");
            mapdb.addConferenceRooms(reader.parseCSV(ConferenceRoom.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("ItemRequest.csv");
            requestdb.addItemRequests(reader.parseCSV(ItemRequest.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("RoomRequest.csv");
            requestdb.addRoomRequests(reader.parseCSV(RoomRequest.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("AvailableFlowers.csv");
            requestdb.addAvailableFlowers(reader.parseCSV(AvailableFlowers.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("AvailableFurniture.csv");
            requestdb.addAvailableFurniture(reader.parseCSV(AvailableFurniture.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("AvailableMeals.csv");
            requestdb.addAvailableMeals(reader.parseCSV(AvailableMeals.class, zipFile.getInputStream(entry)));
            entry = zipFile.getEntry("AvailableSupplies.csv");
            requestdb.addAvailableSupplies(reader.parseCSV(AvailableSupplies.class, zipFile.getInputStream(entry)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void deleteALL() throws SQLException {
        Configuration.deleteEverything();
    }
}
