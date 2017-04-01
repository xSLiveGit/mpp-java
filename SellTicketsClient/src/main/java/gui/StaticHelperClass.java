package gui;

import javafx.scene.control.Alert;

/**
 * Created by Sergiu on 3/19/2017.
 */
public class StaticHelperClass {
    public static void showWarningMessage(String contet){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(contet);
        alert.showAndWait();
    }
}
