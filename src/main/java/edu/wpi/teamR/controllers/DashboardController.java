package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DashboardController {
    MapDatabase mapdb;
    @FXML
    HBox HBox;
    @FXML
    PieChart movePieChart;
    ArrayList<Move> moves;
    int pastMoves;
    int futureMoves;

    HashMap<String, Integer> movesByFloor = new HashMap<>();
    public void initialize() {
        mapdb = App.getMapData().getMapdb();
        moves = App.getMapData().getMoves();

        for (Move m: moves) {
            if (m.getMoveDate().toLocalDate().isBefore(LocalDate.now())) {
                pastMoves++;
            }
            else {
                futureMoves++;
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Past Moves", pastMoves),
                new PieChart.Data("Future Moves", futureMoves));

        movePieChart.setData(pieChartData);
    }
}
