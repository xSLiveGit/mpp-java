package Users;

import controllers.UserController;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.StaticHelperClass;
import utils.exceptions.ControllerException;


import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Created by Sergiu on 1/20/2017.
 */
public class GUIControllerUserSignUp {
    private UserController controller;
    private Stage currentStage;
    private Stage parentStage;
    public GUIControllerUserSignUp() {
    }

    public void initComponents(UserController userController,Stage currentStage,Stage parentStage){
        this.controller = userController;
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
        try {
            controller.register(textFieldUserName.getText(),textFieldPassword.getText(),true,true);
            StaticHelperClass.showWarningMessage("Succesfuly registered.Try log in.");
        } catch (ControllerException e) {
            StaticHelperClass.showWarningMessage(e.getMessage());
        }
    }
}
