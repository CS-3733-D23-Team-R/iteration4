package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListCreator;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListItem;
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

import io.github.gleidsonmt.dashboardfx.*;

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

        Node scheduleList = new ScheduleListCreator()
                .title("Upcoming Moves")
                .items(
                        new ScheduleListItem(
                                moves.get(moves.size()-7).getNodeID(),
                                moves.get(moves.size()-7).getLongName(),
                                moves.get(moves.size()-7).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-7).getNodeID()),
                                LocalTime.of(12,0)
                        ),
                        new ScheduleListItem(
                                moves.get(moves.size()-6).getNodeID(),
                                moves.get(moves.size()-6).getLongName(),
                                moves.get(moves.size()-6).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-6).getNodeID()),
                                LocalTime.of(12,0)
                        ),
                        new ScheduleListItem(
                                moves.get(moves.size()-5).getNodeID(),
                                moves.get(moves.size()-5).getLongName(),
                                moves.get(moves.size()-5).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-5).getNodeID()),
                                LocalTime.of(12,0)
                        ),
                        new ScheduleListItem(
                                moves.get(moves.size()-4).getNodeID(),
                                moves.get(moves.size()-4).getLongName(),
                                moves.get(moves.size()-4).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-4).getNodeID()),
                                LocalTime.of(12,0)
                        ),
                        new ScheduleListItem(
                                moves.get(moves.size()-3).getNodeID(),
                                moves.get(moves.size()-3).getLongName(),
                                moves.get(moves.size()-3).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-3).getNodeID()),
                                LocalTime.of(12,0)
                        ),
                        new ScheduleListItem(
                                moves.get(moves.size()-2).getNodeID(),
                                moves.get(moves.size()-2).getLongName(),
                                moves.get(moves.size()-2).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-2).getNodeID()),
                                LocalTime.of(12,0)
                        ),
                        new ScheduleListItem(
                                moves.get(moves.size()-1).getNodeID(),
                                moves.get(moves.size()-1).getLongName(),
                                moves.get(moves.size()-1).getMoveDate().toString(),
                                Integer.toString(moves.get(moves.size()-1).getNodeID()),
                                LocalTime.of(12,0)
                        )
                )
                .build();

        HBox.getChildren().add(scheduleList);
    }
}
