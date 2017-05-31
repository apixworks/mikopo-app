package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.io.IOException;

/**
 * Created by Apix on 07/05/2017.
 */
public class RepaymentFormController {
    @FXML public TextField repay_loanId;
    @FXML public TextField repay_name;
    @FXML public DatePicker date;
    @FXML public TextField amount;

    JSONObject userObject;

    public void getLoanDetails(String loanId,String name){
        repay_loanId.setText(loanId);
        repay_name.setText(name);
        date.requestFocus();
    }

    public void submitPayment(){
        DatabaseHandler db = new DatabaseHandler();
        db.loanPayment(Integer.parseInt(repay_loanId.getText().substring(5)),date.getValue().toString(),Double.parseDouble(amount.getText()));
        try {
            Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: "+userObject.get("id")+" received payment for Loan: MJ/L/"+repay_loanId.getText());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        db.checkIfLoanDone(Integer.parseInt(repay_loanId.getText().substring(5)));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Successful paid!");
        alert.showAndWait();
        Stage stage = (Stage) amount.getScene().getWindow();
        stage.close();
    }

    public void getUserDetails(JSONObject jsonObject){
        userObject = jsonObject;
    }
}
