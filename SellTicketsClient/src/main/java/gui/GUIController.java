package gui;


import controller.ClientController;
import exceptions.ControllerException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class GUIController {
    private Stage currentStage;
    private ClientController clientController;
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
            ClientController clientController
    ) throws IOException, ControllerException {
        this.clientController = clientController;
        this.currentStage = currentStage;

        loaderMainInterface = new FXMLLoader(getClass().getResource("/FXML/mainInterface.fxml"));
        this.parent_SellTickets = loaderMainInterface.load();
         operationGUIController = loaderMainInterface.getController();
        operationGUIController.initComponents(clientController);
        clientController.setOperationGUIController(operationGUIController);
        loaderMainInterface = new FXMLLoader(getClass().getResource("/FXML/matchInterface.fxml"));
        this.parent_Match = loaderMainInterface.load();
         mainGUIController = loaderMainInterface.getController();
        mainGUIController.initComponents(clientController);

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
            guiControllerUserLogIn.initComponents(currentStage,clientController);

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
