package gui;

import Users.GUIControllerUserLogIn;
import controllers.MatchController;
import controllers.TicketController;
import controllers.UserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.ControllerException;

import java.io.IOException;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class GUI {
    private Stage currentStage;
    private Scene currentScene;
    private Pane globalPane;
    private MatchController matchController;
    private TicketController ticketController;
    private DatabaseConnectionManager databaseConnectionManager;
    private UserController userController;

    public GUI(MatchController matchController, TicketController ticketController, DatabaseConnectionManager databaseConnectionManager, UserController userController) throws IOException, ControllerException {
        this.databaseConnectionManager = databaseConnectionManager;
        currentStage = new Stage();
        this.matchController = matchController;
        this.ticketController = ticketController;
        this.userController = userController;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gui.fxml"));
//        globalPane = loader.load();
//        currentScene= new Scene(globalPane);
//        GUIController guiController = loader.getController();
//        guiController.initComponents(currentStage,matchController,ticketController,saleController,databaseConnectionManager);
//        currentStage.setScene(currentScene);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LogIn.fxml"));
        globalPane = loader.load();
        currentScene = new Scene(globalPane);
        GUIControllerUserLogIn guiControllerUserLogIn = loader.getController();
        guiControllerUserLogIn.initComponents(currentStage,userController,matchController,ticketController,databaseConnectionManager);
        currentStage.setScene(currentScene);
    }

    public void start() {
        currentStage.setTitle("MPP Apps");
       // currentStage.setScene(currentScene);
        currentStage.setResizable(false);
        currentStage.showAndWait();
    }
}

