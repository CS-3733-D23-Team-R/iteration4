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
import java.util.Enumeration;
import java.util.List;

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
            Paths.get(archivePath);
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
            String folder = "";
            ZipArchiveEntry z = zipFile.getEntries().nextElement();
            if (z.isDirectory()) {
                folder = z.getName();
            }
            for (Enumeration<ZipArchiveEntry> e = zipFile.getEntries(); e.hasMoreElements();) {
                System.out.println(e.nextElement().getName());
            }

            CSVReader reader = new CSVReader();
            ZipArchiveEntry entry;
            entry = zipFile.getEntry(folder + "Node.csv");
            List<Node> nodes = reader.parseCSV(Node.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "Edge.csv");
            List<Edge> edges = reader.parseCSV(Edge.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "LocationName.csv");
            List<LocationName> locationNames = reader.parseCSV(LocationName.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "Move.csv");
            List<Move> moves = reader.parseCSV(Move.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "User.csv");
            List<User> users = reader.parseCSV(User.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "Alert.csv");
            List<Alert> alerts = reader.parseCSV(Alert.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "DirectionArrow.csv");
            List<DirectionArrow> directionArrows = reader.parseCSV(DirectionArrow.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "ConferenceRoom.csv");
            List<ConferenceRoom> conferenceRooms = reader.parseCSV(ConferenceRoom.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "ItemRequest.csv");
            List<ItemRequest> itemRequests = reader.parseCSV(ItemRequest.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "RoomRequest.csv");
            List<RoomRequest> roomRequests = reader.parseCSV(RoomRequest.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "AvailableFlowers.csv");
            List<AvailableFlowers> availableFlowers = reader.parseCSV(AvailableFlowers.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "AvailableFurniture.csv");
            List<AvailableFurniture> availableFurniture = reader.parseCSV(AvailableFurniture.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "AvailableMeals.csv");
            List<AvailableMeals> availableMeals = reader.parseCSV(AvailableMeals.class, zipFile.getInputStream(entry));
            entry = zipFile.getEntry(folder + "AvailableSupplies.csv");
            List<AvailableSupplies> availableSupplies = reader.parseCSV(AvailableSupplies.class, zipFile.getInputStream(entry));

            deleteALL();

            mapdb.addNodes(nodes);
            mapdb.addEdges(edges);
            mapdb.addLocationNames(locationNames);
            mapdb.addMoves(moves);
            userdb.addUsers(users);
            userdb.addAlerts(alerts);
            mapdb.addDirectionArrows(directionArrows);
            mapdb.addConferenceRooms(conferenceRooms);
            requestdb.addItemRequests(itemRequests);
            requestdb.addRoomRequests(roomRequests);
            requestdb.addAvailableFlowers(availableFlowers);
            requestdb.addAvailableFurniture(availableFurniture);
            requestdb.addAvailableMeals(availableMeals);
            requestdb.addAvailableSupplies(availableSupplies);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void deleteALL() throws SQLException {
        Configuration.deleteEverything();
    }
}
