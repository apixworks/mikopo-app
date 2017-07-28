package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Apix on 14/07/2017.
 */
public class ChangePasswordController implements Initializable {

    @FXML public TextField current;
    @FXML public TextField newPassword;
    @FXML public TextField confirmPassword;
    @FXML public Text actionTxt;
    @FXML public Text actionTxt2;

    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newPassword.setEditable(false);
        confirmPassword.setEditable(false);
        actionTxt.setVisible(false);
        actionTxt2.setVisible(false);

        current.textProperty().addListener((observable, oldValue, newValue) -> {
            actionTxt.setVisible(true);
            try {
                if(newValue.equals(userObject.get("password"))){
                    actionTxt.setText("Correct");
                    actionTxt.setStyle("-fx-font-weight: bold;-fx-text-fill: forestgreen;");
                    newPassword.setEditable(true);
                    confirmPassword.setEditable(true);
                }else{
                    actionTxt.setText("Wrong");
                    actionTxt.setStyle("-fx-font-weight: bold;-fx-text-fill: firebrick;");
                    newPassword.setEditable(false);
                    confirmPassword.setEditable(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            actionTxt2.setVisible(true);
                if(newValue.equals(newPassword.getText())){
                    actionTxt2.setText("Match");
                    actionTxt2.setStyle("-fx-font-weight: bold;-fx-text-fill: forestgreen;");
                }else{
                    actionTxt2.setText("Do not Match");
                    actionTxt2.setStyle("-fx-font-weight: bold;-fx-text-fill: firebrick;");
                }
        });
    }

    public void changePassword() throws JSONException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(current.getText().equals("") || newPassword.getText().equals("") || confirmPassword.getText().equals("")){
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();
        }else {
            if(newPassword.getText().equals(confirmPassword.getText())){
                DatabaseHandler db = new DatabaseHandler();
                if(db.updatePassword(newPassword.getText(),userObject.getInt("id"))){
                    alert.setContentText("Successful changed!");
                    current.setText("");
                    newPassword.setText("");
                    confirmPassword.setText("");
                    actionTxt.setVisible(false);
                    alert.showAndWait();
                    Stage stage = (Stage) newPassword.getScene().getWindow();
                    stage.close();
                }
            }else{
                alert.setContentText("Passwords don't match!");
                newPassword.setText("");
                confirmPassword.setText("");
                alert.showAndWait();
            }
        }

    }

    public void getUserDetails(JSONObject userObject) {
        this.userObject = userObject;
    }
}
