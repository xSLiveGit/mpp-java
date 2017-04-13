package gui;

import controller.ClientController;

import entity.User;
import exceptions.ControllerException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.SaleHouseException;

import javax.security.sasl.SaslException;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by Sergiu on 1/20/2017.
 */
public class GUIControllerUserLogIn {
    private Stage currentStage;
    private ClientController clientController;

    @FXML
    TextField textField_UserName;
    @FXML
    PasswordField textField_Password;

    @FXML
    Button button_LogIn;
    @FXML
    Button button_SignUp;

    public GUIControllerUserLogIn() {
    }

    public void initComponents(
            Stage currentStage,
            ClientController clientController
    ) throws IOException {
        this.currentStage = currentStage;
        this.clientController = clientController;
        this.currentStage = currentStage;
        this.button_LogIn.setId("my-log-button");
        this.button_SignUp.setId("my-register-button");
    }


    public void logInHandler() throws IOException {
        String userName = textField_UserName.getText();
        String password = textField_Password.getText();
        try {
            User u = clientController.login(userName,password);

            if(u != null){
                FXMLLoader GUILoader = new FXMLLoader(getClass().getResource("/FXML/gui.fxml"));
                BorderPane pane = GUILoader.load();
                Scene scene = new Scene(pane);
                GUIController guiController = GUILoader.getController();
                Stage stage = new Stage();
                guiController.initComponents(stage,clientController);
                stage.setTitle("Application");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setOnCloseRequest(e->{
                    try {
                        this.clientController.logout();
                    } catch (SaleHouseException | RemoteException e1) {
                        e1.printStackTrace();
                    }
                });
                currentStage.hide();
                stage.show();
            }
            else{
                StaticHelperClass.showWarningMessage("Invalid username or password");
            }
        } catch (ControllerException | SaleHouseException e) {
            StaticHelperClass.showWarningMessage(e.getMessage());
        }
    }

    public void signUpHandler() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/FXML/SignUp.fxml"));
        Pane pane = loaderSignUp.load();
        GUIControllerUserSignUp controller = loaderSignUp.getController();
        controller.initComponents(clientController,stage,currentStage);
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("/css/log.css").toString());
        stage.setScene(scene);
        stage.setOnCloseRequest(e->currentStage.show());
        stage.show();
        currentStage.hide();
    }
}
