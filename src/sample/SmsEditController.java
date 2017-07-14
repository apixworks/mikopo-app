package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

/**
 * Created by Apix on 21/06/2017.
 */
public class SmsEditController {
    @FXML public TextArea sms;
    @FXML public Label header;

    JSONObject userObject;
    int id;
    DatabaseHandler db;

    public void getId(int id){
        if(id==1){
            header.setText("1 Month Late SMS");
        }else if(id==2){
            header.setText("2 Month Late SMS");
        }else if(id==3){
            header.setText("Penalty Late SMS");
        }
        this.id=id;
        db = new DatabaseHandler();
        sms.setText(db.viewSms(id));
    }

    public void getUserDetails(JSONObject jsonObject){
        userObject = jsonObject;
    }

    public void updateSms(){
        db = new DatabaseHandler();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(db.editSms(id,sms.getText())){
            alert.setContentText("Successful Changed!");
        }else{
            alert.setContentText("UnSuccessful!");
        }
        alert.showAndWait();
        Stage stage = (Stage) sms.getScene().getWindow();
        stage.close();
    }
}
