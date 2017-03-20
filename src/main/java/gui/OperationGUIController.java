package gui;

import controllers.MatchController;
import controllers.SaleController;
import controllers.TicketController;
import domain.Match;
import domain.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import utils.StaticHelperClass;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.List;

import static utils.StaticHelperClass.showWarningMessage;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class OperationGUIController {
    private String currentPerson;
    private Integer currentTicketId = INVALID_CURRENT_TICKET_ID;
    private Match currentSelectedMatch = INVALID_CURRENT_SELECTD_MATCH;
    private ObservableList<Match> model;
    private MatchController matchController;
    private TicketController ticketController;
    private SaleController saleController;
    private DatabaseConnectionManager databaseConnectionManager;
    private boolean tableIsFiltered = false;

    private final static Integer INVALID_CURRENT_TICKET_ID = -1;
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

    public void initComponents(MatchController matchController, TicketController ticketController, SaleController saleController, DatabaseConnectionManager databaseConnectionManager) throws ControllerException {
        this.matchController = matchController;
        this.ticketController = ticketController;
        this.saleController = saleController;
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
            if(currentTicketId.equals(INVALID_CURRENT_TICKET_ID)){//if it's not about first match on this ticket
                try {
                    if(currentSelectedMatch.getRemainingTickets() < Integer.parseInt(textField_Capacity.getText())){
                        showWarningMessage("We haven't so much tickets");
                    }

                    currentTicketId = ticketController.add(0d,true,true);
//                    databaseConnectionManager.getConnection().setAutoCommit(false);
                    saleController.add(currentTicketId,currentSelectedMatch.getId(),this.textField_BuyerName.getText(),Integer.parseInt(textField_Capacity.getText()),true,false);
                    currentSelectedMatch.setRemainingTickets(currentSelectedMatch.getRemainingTickets() - Integer.parseInt(textField_Capacity.getText()));
                    matchController.update(currentSelectedMatch.getId(),currentSelectedMatch.getTeam1(),currentSelectedMatch.getTeam2(),currentSelectedMatch.getStage(),currentSelectedMatch.getRemainingTickets().toString(),currentSelectedMatch.getPrice().toString(),false,true);
                   // databaseConnectionManager.getConnection().commit();
                    //  databaseConnectionManager.getConnection().setAutoCommit(true);
                    //this.textField_BuyerName.setDisable(true);
                    actualiseList();
                } catch (ControllerException/* | SQLException | ClassNotFoundException*/ e) {
                    //showWarningMessage(e.getMessage());
                    //databaseConnectionManager.getConnection().rollback();
                    e.printStackTrace();
                }
            }
            else{
                if(!currentSelectedMatch.equals(INVALID_CURRENT_SELECTD_MATCH)){
                    try {
                        saleController.add(currentTicketId,currentSelectedMatch.getId(),this.textField_BuyerName.getText(),Integer.parseInt(textField_Capacity.getText()),true,false);
                        currentSelectedMatch.setRemainingTickets(currentSelectedMatch.getRemainingTickets() - Integer.parseInt(textField_Capacity.getText()));
                        matchController.update(currentSelectedMatch.getId(),currentSelectedMatch.getTeam1(),currentSelectedMatch.getTeam2(),currentSelectedMatch.getStage(),currentSelectedMatch.getRemainingTickets().toString(),currentSelectedMatch.getPrice().toString(),false,true);
//                        databaseConnectionManager.getConnection().commit();
                        actualiseList();
                    } catch (ControllerException /*| ClassNotFoundException |SQLException*/ e) {
                        e.printStackTrace();
                        showWarningMessage(e.getMessage());
                    }
                }
                else{
                    showWarningMessage("Select 1 match.");
                }
            }
        }
    }

    public void button_ExportTicket_Handler(){
        if(!this.currentTicketId.equals(INVALID_CURRENT_TICKET_ID)){
            initFields();
        }
        else{
            showWarningMessage("This ticket is empty");
        }
    }

    public void actualiseList() throws ControllerException {
        model.setAll(matchController.getAll());
    }

    private void initFields(){
        this.textField_BuyerName.clear();
        this.textField_BuyerName.setDisable(false);
        this.currentTicketId = INVALID_CURRENT_TICKET_ID;
        this.currentPerson = INVALID_CURRENT_PERSON;
    }

    public void button_CancelOperation_Handler(){
        if(this.currentTicketId != INVALID_CURRENT_TICKET_ID) {
            try {
                List<Sale> list = saleController.getAllWithThisTicketId(this.currentTicketId);
                databaseConnectionManager.getConnection().setAutoCommit(false);
                for (Sale sale : list) {
                    matchController.increaseQuantity(sale.getIdMatch(), sale.getQuantity(),false,false);
                }
                this.ticketController.delete(currentTicketId,false,true);
                initFields();
                actualiseList();
            } catch (ControllerException | SQLException | ClassNotFoundException e) {
                try {
                    databaseConnectionManager.getConnection().rollback();
                } catch (SQLException | ClassNotFoundException e1) {
                    StaticHelperClass.showWarningMessage(e.getMessage());
                }
                StaticHelperClass.showWarningMessage(e.getMessage());
            }
        }
        else{
            StaticHelperClass.showWarningMessage("The ticket is already empty.");
        }

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
