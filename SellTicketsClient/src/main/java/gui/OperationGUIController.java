package gui;

import controller.ClientController;
import entity.Match;
import exceptions.ControllerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import services.SaleHouseException;
import services.ServiceException;

import java.sql.SQLException;


/**
 * Created by Sergiu on 3/18/2017.
 */
public class OperationGUIController {
    private String currentPerson;
    private Match currentSelectedMatch = INVALID_CURRENT_SELECTD_MATCH;
    private ObservableList<Match> model;
    private ClientController clientController = null;
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

    public void initComponents(ClientController clientController) throws ControllerException {
        this.clientController = clientController;
        model = FXCollections.observableArrayList(( clientController.getFilteredAndSortedMatches()));
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
            StaticHelperClass.showWarningMessage("Person name must be non-empty text.");
        }
        else if(currentSelectedMatch == null){
            StaticHelperClass.showWarningMessage("You must select 1 match.");
        }
        else if(!isValidQuantity(textField_Capacity.getText())){
            StaticHelperClass.showWarningMessage("The number of purchased tickets is invalid.");
        }
        else{
            try {
                this.clientController.sellTickets(this.currentSelectedMatch.getId().toString(),textField_Capacity.getText(),this.textField_BuyerName.getText());
                actualiseList();
            } catch (ControllerException e) {
                StaticHelperClass.showWarningMessage(e.getMessage());
                //e.printStackTrace();
            } catch (SaleHouseException | ServiceException e) {
                e.printStackTrace();
            }
        }
    }


    public void actualiseList() throws ControllerException {
        model.setAll(clientController.getAllMatches());
    }

    private void initFields(){
        this.textField_BuyerName.clear();
        this.textField_BuyerName.setDisable(false);
        this.currentPerson = INVALID_CURRENT_PERSON;
    }


    public void button_FilterMatches_Handler(){

        try {
                if(!tableIsFiltered) {
                    model.setAll(clientController.getFilteredAndSortedMatches());
                    tableIsFiltered = true;
                }
                else{
                    model.setAll(clientController.getAllMatches());
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
