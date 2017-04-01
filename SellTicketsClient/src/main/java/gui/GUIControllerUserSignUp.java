package gui;

import controller.ClientController;
import exceptions.ControllerException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by Sergiu on 1/20/2017.
 */
public class GUIControllerUserSignUp {
    private ClientController controller;
    private Stage currentStage;
    private Stage parentStage;
    public GUIControllerUserSignUp() {
    }

    public void initComponents(ClientController controller,Stage currentStage,Stage parentStage){
        this.controller = controller;
        this.currentStage = currentStage;
        this.parentStage = parentStage;
        this.button_SignUp.setId("my-register-button");
    }

    @FXML
    TextField textFieldUserName;
    @FXML
    PasswordField textFieldPassword;

    @FXML
    Button button_SignUp;

    public void signUpHandler(){
        controller.register(textFieldUserName.getText(),textFieldPassword.getText());
        StaticHelperClass.showWarningMessage("Succesfuly registered.Try log in.");
    }
}
