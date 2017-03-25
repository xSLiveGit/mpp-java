package gui;

import controllers.MatchController;
import controllers.TicketController;
import domain.Match;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import utils.StaticHelperClass;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.ControllerException;

import java.sql.SQLException;
import java.util.List;

import static utils.StaticHelperClass.showWarningMessage;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class OperationGUIController {
    private String currentPerson;
    private Match currentSelectedMatch = INVALID_CURRENT_SELECTD_MATCH;
    private ObservableList<Match> model;
    private MatchController matchController;
    private TicketController ticketController;
    private DatabaseConnectionManager databaseConnectionManager;
    private boolean tableIsFiltered = false;

    private final static Match INVALID_CURRENT_SELECTD_MATCH = null;
    private final static String INVALID_CURRENT_PERSON = "";
    private final static String INVALID_COLUMN = "SOLD OUT";

    @FXML
    private TableView<Match> tableView_Match;
        @FXML
        private TableColumn<Match,String> tableColumn_Team1;
        @FXML
        private TableColumn<Match,String> tableColumn_Team2;
        @FXML
        private TableColumn<Match,String> tableColumn_Stage;
        @FXML
        private TableColumn<Match,String> tableColumn_NoTickets;
        @FXML
        private TableColumn<Match,Double> tableColumn_Price;

    @FXML
    Button button_FilterMatches;
    @FXML
    Button button_CancelOperation;
    @FXML
    Button button_AddMatch;
    @FXML
    Button button_ExportTicket;

    @FXML
    TextField textField_BuyerName;
    @FXML
    TextField textField_Capacity;
    @FXML
    Label label_TotalPrice;

    public OperationGUIController(){
//        label_TotalPrice.setText("25000");
    }

    public void initComponents(MatchController matchController, TicketController ticketController,DatabaseConnectionManager databaseConnectionManager) throws ControllerException {
        this.matchController = matchController;
        this.ticketController = ticketController;
        this.databaseConnectionManager = databaseConnectionManager;
        model = FXCollections.observableArrayList(( matchController.getAll()));
        this.tableView_Match.setItems(model);
        tableColumn_Team1.setCellValueFactory(new PropertyValueFactory<Match, String>("team1"));
        tableColumn_Team2.setCellValueFactory(new PropertyValueFactory<Match, String>("team2"));
        tableColumn_Stage.setCellValueFactory(new PropertyValueFactory<Match, String>("stage"));
        tableColumn_NoTickets.setCellValueFactory(new PropertyValueFactory<Match, String>("ticketsString"));
        tableColumn_Price.setCellValueFactory(new PropertyValueFactory<Match, Double>("price"));

        tableView_Match.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            currentSelectedMatch = newSelection;

        });
        tableColumn_NoTickets.setCellFactory(column -> {
            return new TableCell<Match, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(super.getText());
                    } else {
                        setText(item);
                        if(item.equals(INVALID_COLUMN))
                            setTextFill(Color.RED);
                        else
                            setTextFill(Color.BLACK);
                    }
                }
            };
        });

    }

    public void button_AddMatch_Handler() throws SQLException, ClassNotFoundException {
        if(textField_BuyerName.getText().equals(INVALID_CURRENT_PERSON)){
            showWarningMessage("Person name must be non-empty text.");
        }
        else if(currentSelectedMatch == null){
            showWarningMessage("You must select 1 match.");
        }
        else if(!isValidQuantity(textField_Capacity.getText())){
            showWarningMessage("The number of purchased tickets is invalid.");
        }
        else{
            try {
                this.ticketController.add(this.currentSelectedMatch.getId().toString(),this.textField_BuyerName.getText(),textField_Capacity.getText());
                actualiseList();
            } catch (ControllerException e) {
                StaticHelperClass.showWarningMessage(e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public void actualiseList() throws ControllerException {
        model.setAll(matchController.getAll());
    }

    private void initFields(){
        this.textField_BuyerName.clear();
        this.textField_BuyerName.setDisable(false);
        this.currentPerson = INVALID_CURRENT_PERSON;
    }


    public void button_FilterMatches_Handler(){

        try {
                if(!tableIsFiltered) {
                    model.setAll(matchController.getAllMatchesWithRemainingTickets());
                    tableIsFiltered = true;
                }
                else{
                    model.setAll(matchController.getAll());
                    tableIsFiltered = false;
                }
            } catch ( ControllerException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidQuantity(String nr){
        for(char c : nr.toCharArray()){
            if(c < '0' || c > '9')
                return false;
        }
        return true;
    }
}
