package edu.wpi.teamR.controllers;


import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import edu.wpi.teamR.requestdb.*;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListCreator;
import io.github.gleidsonmt.dashboardfx.core.view.layout.creators.ScheduleListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.chart.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @FXML TableView<ItemRequest> requestTable;
    @FXML TableColumn<ItemRequest, Integer> idCol;
    @FXML TableColumn<ItemRequest, String> requestTypeCol, locationCol, notesCol, dateCol, itemCol;
    @FXML BarChart barChartField;
    MapDatabase mapdb;
    RequestDatabase requestdb;
    ArrayList<Move> moves;
    int pastMoves;
    int futureMoves;

    private final ObservableList<ItemRequest> dataList = FXCollections.observableArrayList();
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

            LocalDate date = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            String formattedDate = date.format(dateTimeFormatter);

        dataList.addAll(new RequestDatabase().getItemRequests());
        idCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        for (ItemRequest request : new RequestDatabase().getItemRequests()) {
            requestTable.getItems().add(request);
        }

        XYChart.Series series = new XYChart.Series<>();

        SearchList searchList = new SearchList();
        searchList.addOrdering(RequestAttribute.requestDate, Operation.orderByAsc);
        ArrayList<ItemRequest> requestList = new RequestDatabase().getItemRequestByAttributes(searchList);
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();


        for (int i = 0; i < requestList.size() - 1; i++){
            int count = 1;
            ItemRequest request = requestList.get(i);
            LocalDate currentDate = request.getRequestDate().toLocalDateTime().toLocalDate();

            if(!hashMap.containsKey(currentDate.toString())) {
                for (int j = i + 1; j < requestList.size() - 1; j++) {
                    if (currentDate.equals(requestList.get(j).getRequestDate().toLocalDateTime().toLocalDate())) {
                        count++;
                    }
                }
                series.getData().add(new XYChart.Data<>(currentDate.toString(), count));
                hashMap.put(currentDate.toString(), count);
                System.out.println(count);
            }
        }

        barChartField.getData().add(series);
        barChartField.setLegendVisible(false);
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
