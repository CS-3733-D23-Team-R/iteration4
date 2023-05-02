package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import edu.wpi.teamR.requestdb.*;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListCreator;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DashboardController {
    @FXML PieChart requestStatusChart;
    @FXML PieChart requestTypeChart;
    MapDatabase mapdb;
    RequestDatabase requestdb;
    @FXML HBox hBox;
    ArrayList<Move> moves;
    int pastMoves;
    int futureMoves;

    HashMap<String, Integer> movesByFloor = new HashMap<>();
    public void initialize() throws SQLException, SearchException {
        mapdb = App.getMapData().getMapdb();
        moves = App.getMapData().getMoves();
        requestdb = new RequestDatabase();

        for (Move m: moves) {
            if (m.getMoveDate().toLocalDate().isBefore(LocalDate.now())) {
                pastMoves++;
            }
            else {
                futureMoves++;
            }
        }

        ArrayList<PieChart.Data> dataList = new ArrayList<>();
        for (RequestStatus r : RequestStatus.values()) {
            SearchList searchList = new SearchList();
            searchList.addComparison(RequestAttribute.requestStatus, Operation.equalTo, r);
            double count = requestdb.getItemRequestByAttributes(searchList).size();
            if (count != 0) {
                PieChart.Data chartData = new PieChart.Data(r.toString(), count);
                dataList.add(chartData);
            }
        }
        requestStatusChart.setData(FXCollections.observableArrayList(dataList));

        //ArrayList<PieChart.Data> dataList = new ArrayList<>();
        dataList = new ArrayList<>();
        for (RequestType r : RequestType.values()) {
            SearchList searchList = new SearchList();
            searchList.addComparison(RequestAttribute.requestType, Operation.equalTo, r);
            double count = requestdb.getItemRequestByAttributes(searchList).size();
            if (count != 0) {
                PieChart.Data chartData = new PieChart.Data(r.toString(), count);
                dataList.add(chartData);
            }
        }
        requestTypeChart.setData(FXCollections.observableArrayList(dataList));

//        SearchList searchList = new SearchList();
//        searchList.addComparison(RequestAttribute.requestType, Operation.equalTo, RequestType.Flower);
//        double flower = requestdb.getItemRequestByAttributes(searchList).size();
//        searchList = new SearchList();
//        searchList.addComparison(RequestAttribute.requestType, Operation.equalTo, RequestType.Meal);
//        double meal = requestdb.getItemRequestByAttributes(searchList).size();
//        searchList = new SearchList();
//        searchList.addComparison(RequestAttribute.requestType, Operation.equalTo, RequestType.Furniture);
//        double furniture = requestdb.getItemRequestByAttributes(searchList).size();
//        searchList = new SearchList();
//        searchList.addComparison(RequestAttribute.requestType, Operation.equalTo, RequestType.Supplies);
//        double supplies = requestdb.getItemRequestByAttributes(searchList).size();
//        ObservableList<PieChart.Data> requestTypeData = FXCollections.observableArrayList(
//                new PieChart.Data("FLower", flower),
//                new PieChart.Data("Meal", meal),
//                new PieChart.Data("Furniture", furniture),
//                new PieChart.Data("Supplies", supplies)
//        );

//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//                new PieChart.Data("Past Moves", pastMoves),
//                new PieChart.Data("Future Moves", futureMoves));
//
//        requestTypeChart.setData(requestTypeData);

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

        //hBox.getChildren().add(scheduleList);
    }
}
