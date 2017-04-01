package gui;

import controller.ClientController;
import exceptions.ControllerException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class GUI {
    private Stage currentStage;
    private Scene currentScene;
    private Pane globalPane;
    private ClientController clientController;

    public GUI(ClientController clientController) throws IOException, ControllerException {
        currentStage = new Stage();
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
        guiControllerUserLogIn.initComponents(currentStage,clientController);
        currentStage.setScene(currentScene);
    }

    public void start() {
        currentStage.setTitle("MPP Apps");
       // currentStage.setScene(currentScene);
        currentStage.setResizable(false);
        currentStage.showAndWait();
    }
}

