package gui;

import Users.GUIControllerUserLogIn;
import controllers.MatchController;
import controllers.TicketController;
import controllers.UserController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.StaticHelperClass;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.ControllerException;

import java.io.IOException;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class GUIController {
    private Stage currentStage;
    private MatchController matchController;
    private TicketController ticketController;
    private DatabaseConnectionManager databaseConnectionManager;
    private UserController userController;
    private OperationGUIController operationGUIController;
    private ManageMatchesGUIController mainGUIController;

    @FXML
    private Pane currentScenePane;


    private Parent parent_SellTickets;
    private Parent parent_Match;
    private FXMLLoader loaderMainInterface;

    public GUIController(){

    }

    public void initComponents(
            Stage currentStage,
            MatchController matchController,
            TicketController ticketController,
            DatabaseConnectionManager databaseConnectionManager,
            UserController userController
    ) throws IOException, ControllerException {
        this.matchController = matchController;
        this.ticketController = ticketController;
        this.databaseConnectionManager = databaseConnectionManager;
        this.currentStage = currentStage;
        this.userController = userController;

        loaderMainInterface = new FXMLLoader(getClass().getResource("/FXML/mainInterface.fxml"));
        this.parent_SellTickets = loaderMainInterface.load();
         operationGUIController = loaderMainInterface.getController();
        operationGUIController.initComponents(matchController,ticketController,databaseConnectionManager);

        loaderMainInterface = new FXMLLoader(getClass().getResource("/FXML/matchInterface.fxml"));
        this.parent_Match = loaderMainInterface.load();
         mainGUIController = loaderMainInterface.getController();
        mainGUIController.initComponents(matchController,databaseConnectionManager);

        currentScenePane.getChildren().clear();
        currentScenePane.getChildren().add(parent_SellTickets);

    }

    public void setMatchOperationsContext(){
        currentScenePane.getChildren().clear();
        currentScenePane.getChildren().add(parent_Match);
        try {
            mainGUIController.actualiseList();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    public void setSellTicketsOperationsContext(){
        currentScenePane.getChildren().clear();
        currentScenePane.getChildren().addAll(parent_SellTickets);
        try {
            operationGUIController.actualiseList();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    public void logOut_Handler(){
        System.out.print("Logout");
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogIn.fxml"));
            Pane pane = loader.load();
            Scene scene = new Scene(pane);
            GUIControllerUserLogIn guiControllerUserLogIn = loader.getController();
            guiControllerUserLogIn.initComponents(currentStage,userController,matchController,ticketController,databaseConnectionManager);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            this.currentStage.close();
        }
        catch (Exception e){
            StaticHelperClass.showWarningMessage(e.getMessage());
        }
    }

    public void exit_Handler(){
        Platform.exit();
    }
}
