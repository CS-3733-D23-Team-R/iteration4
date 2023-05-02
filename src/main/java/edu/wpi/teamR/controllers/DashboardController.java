package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import edu.wpi.teamR.requestdb.*;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListCreator;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListItem;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DashboardController {
    @FXML TableView<Move> moveTable;
    @FXML TableColumn<Move, String> locationColumn;
    @FXML TableColumn<Move, Date> dateColumn;
    @FXML TableColumn<Move, Integer> nodeColumn;
    @FXML PieChart requestStatusChart;
    @FXML PieChart requestTypeChart;
    MapDatabase mapdb;
    RequestDatabase requestdb;
    ArrayList<Move> moves;
    int pastMoves;
    int futureMoves;


    Date todayDate = Date.valueOf(LocalDate.now());

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

        displayPieCharts();
        displayMoveTable();


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

    private void displayMoveTable() throws SQLException {
        nodeColumn.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("moveDate"));

        List<Move> moves = mapdb.getMoves();
        FilteredList<Move> filteredMoves = new FilteredList<>(FXCollections.observableArrayList(moves),
                m -> todayDate.before(m.getMoveDate()));
        SortedList<Move> sortedMoves = new SortedList<>(filteredMoves, Comparator.comparing(Move::getMoveDate));
        sortedMoves.comparatorProperty().bind(moveTable.comparatorProperty());
        moveTable.setItems(filteredMoves);
    }

    private void displayPieCharts() throws SearchException, SQLException {
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
    }
}
