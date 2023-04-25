package edu.wpi.teamR.controllers.mapeditor;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.mapdb.Move;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.time.LocalDate;

public class FutureMovesTableController {
    @FXML
    TableView<Move> movesTable;
    @FXML
    TableColumn<Move, Integer> nodeIDColumn;
    @FXML
    TableColumn<Move, String> locationColumn;
    @FXML TableColumn<Move, Date> dateColumn;
    @FXML
    MFXDatePicker datePicker;

    MapDatabase mapdb;
    ArrayList<Move> moveList;

    public void initialize() throws SQLException {
        this.mapdb = App.getMapData().getMapdb();
        setTableColumns();
    }

    public void setTableColumns() throws SQLException {

        moveList = mapdb.getMoves();
        ObservableList<Move> tableData = FXCollections.observableArrayList(moveList);
        movesTable.setItems(tableData);

        nodeIDColumn.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        nodeIDColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longName"));
        locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Predicate<Move> dateFilter = move -> {
                LocalDate selectedDate = datePicker.getValue();
                LocalDate moveDate = move.getMoveDate().toLocalDate();
                return (selectedDate == null) || moveDate.equals(selectedDate);
            };
            FilteredList<Move> filteredData = new FilteredList<>(tableData, dateFilter);
            movesTable.setItems(filteredData);
        });
    }

    public void updateTable() {
        movesTable.getItems().clear();
        movesTable.refresh();
        //get moves by date
        //add to table
    }
}
