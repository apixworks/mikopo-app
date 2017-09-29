package sample;

import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sample.models.BorrowerIDAndName;
import sample.Checker;
import sample.Logger;
import sample.backend.DatabaseHandler;
import sample.models.EditLoan;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Apix on 21/06/2017.
 */
public class EditLoanController implements Initializable{

    @FXML public TextField borrower;
    @FXML public TextField amountTxtField;
    @FXML public TextField durationTxtField;
    @FXML public DatePicker loanDate;
    @FXML public TextField memberId;
    @FXML public TextField propertyId;
    @FXML public TextField perMonthTxtField;
    @FXML public TextField propertyName;
    @FXML public TextArea desc;
    @FXML public Button editBtn;
    @FXML public JFXDialogLayout borrowerDialog;
    @FXML public JFXDialogLayout mdhaminiDialog;
    @FXML public Label name;
    @FXML public Label id;
    @FXML public Label mdhamini_name;
    @FXML public Label mdhamini_id;
    @FXML public Text actionTxt;
    @FXML public JFXToggleButton toggle;

    DatabaseHandler db;
    BorrowerIDAndName borrowerIDAndName;
    int loan_id;
    JSONObject userObject;

    double total = 0;
    double totalInt;
    double perMont;
    double total_payment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        borrower.setEditable(false);
        amountTxtField.setEditable(false);
        durationTxtField.setEditable(false);
        loanDate.setEditable(false);
        memberId.setEditable(false);
        perMonthTxtField.setEditable(false);

        borrower.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    db = new DatabaseHandler();
                    borrowerIDAndName = db.getBorrowerNameAndId(Integer.parseInt(borrower.getText()));
                    name.setText(borrowerIDAndName.name);
                    id.setText(borrowerIDAndName.id);
                    borrowerDialog.setVisible(true);
                }
                else
                {
                    borrowerDialog.setVisible(false);
                }
            }
        });

        borrower.textProperty().addListener((observable, oldValue, newValue) -> {
            db = new DatabaseHandler();
            if(Checker.isStringInt(newValue)) {
                borrowerIDAndName = db.getBorrowerNameAndId(Integer.parseInt(newValue));
                if(borrowerIDAndName==null){
                    name.setText(null);
                    id.setText(null);
                }else{
                    name.setText(borrowerIDAndName.name);
                    id.setText(borrowerIDAndName.id);
                }
            }else {
                name.setText(null);
                id.setText(null);
            }
        });

        memberId.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    db = new DatabaseHandler();
                    borrowerIDAndName = db.getBorrowerNameAndId(Integer.parseInt(memberId.getText()));
                    mdhamini_name.setText(borrowerIDAndName.name);
                    mdhamini_id.setText(borrowerIDAndName.id);
                    mdhaminiDialog.setVisible(true);
                }
                else
                {
                    mdhaminiDialog.setVisible(false);
                }
            }
        });

        memberId.textProperty().addListener((observable, oldValue, newValue) -> {
            db = new DatabaseHandler();
            if(Checker.isStringInt(newValue)) {
                borrowerIDAndName = db.getBorrowerNameAndId(Integer.parseInt(newValue));
                if(borrowerIDAndName==null){
                    mdhamini_name.setText(null);
                    mdhamini_id.setText(null);
                }else{
                    mdhamini_name.setText(borrowerIDAndName.name);
                    mdhamini_id.setText(borrowerIDAndName.id);
                }
            }else {
                mdhamini_name.setText(null);
                mdhamini_id.setText(null);
            }
        });

        durationTxtField.textProperty().addListener((observable,oldValue,newValue) -> {
            checkAmount();
        });

        amountTxtField.textProperty().addListener((observable,oldValue,newValue) -> {
            checkAmount();
        });
    }

    public void getId(int id){
        db = new DatabaseHandler();
        EditLoan editLoan = db.viewLoan(id);

        borrower.setText(String.valueOf(editLoan.borrower));
        amountTxtField.setText(String.format("%,.0f", editLoan.amount_borrowed));
        durationTxtField.setText(String.valueOf(editLoan.duration));
        loanDate.setValue(editLoan.date_of_loan);
        memberId.setText(String.valueOf(editLoan.member_id));
        propertyId.setText(editLoan.property_id);
        perMonthTxtField.setText(String.format("%,.0f", editLoan.per_month));
        propertyName.setText(editLoan.property_name);
        desc.setText(editLoan.desc);
        total_payment = editLoan.total_payment;
        loan_id=id;

        desc.requestFocus();

        if(editLoan.tolerance.equals("yes")){
            toggle.setSelected(true);
        }else{
            toggle.setSelected(false);
        }
    }

    public void chooseLoanFiles(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Loan Files");
        fileChooser.setInitialFileName("");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Files", "*.jpg", "*.png", "*.pdf", ".*doc");
        fileChooser.getExtensionFilters().addAll(extFilter);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(editBtn.getScene().getWindow());

        if (selectedFiles != null) {
            try {
                //File dir = new File("E:/MikopoFiles/MJ.L."+loan_id);
                File dir = new File("C:/mikopo/MikopoFiles/MJ.L."+loan_id);
                for (File srcFile: selectedFiles) {
                    if (!srcFile.isDirectory()) {
                        FileUtils.copyFileToDirectory(srcFile,dir);
                    }else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Alert");
                        alert.setHeaderText(null);
                        alert.setContentText("Please choose files not a folder!");
                        alert.showAndWait();
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                actionTxt.setText("An ERROR occurred while saving the files!");
                return;
            }
            actionTxt.setText("Files saved: " + selectedFiles.toString());
        }
        else {
            actionTxt.setText("Files save cancelled.");
        }
    }

    public void editLoan(){
        String tolerance;

        String[] amountString = amountTxtField.getText().split(",");
        String amounter = "";
        for(int i=0;i<amountString.length;i++){
            amounter = amounter + amountString[i];
        }
        double amount = Double.parseDouble(amounter);

        String[] perMonthString = perMonthTxtField.getText().split(",");
        String perMonther = "";
        for(int i=0;i<perMonthString.length;i++){
            perMonther = perMonther + perMonthString[i];
        }
        double perMonth = Double.parseDouble(perMonther);

        if(toggle.isSelected()){
            tolerance = "yes";
            DatabaseHandler db = new DatabaseHandler();
            db.updateFinetolerance(loan_id,tolerance);
        }else {
            tolerance = "no";
            DatabaseHandler db = new DatabaseHandler();
            db.updateFinetolerance(loan_id,tolerance);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(db.editLoanDetails(loan_id,Integer.parseInt(borrower.getText()),amount,Integer.parseInt(durationTxtField.getText())
                ,loanDate.getValue(),perMonth,total_payment,Integer.parseInt(memberId.getText()),propertyId.getText(),propertyName.getText(),desc.getText(),tolerance)){
            alert.setContentText("Successful Edited!");
            try {
                Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" edited Loan: MJ/L/"+id);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            alert.setContentText("UnSuccessful try Again!");
        }
        alert.showAndWait();
        Stage stage = (Stage) editBtn.getScene().getWindow();
        stage.close();
    }

    private void checkAmount() {
        String[] amountString = amountTxtField.getText().split(",");
        String amounter = "";
        for(int i=0;i<amountString.length;i++){
            amounter = amounter + amountString[i];
        }
        try {
            total = 0;
            double amount = Double.parseDouble(amounter);
            int duration = Integer.parseInt(durationTxtField.getText());
            double p1 = amount;
            db = new DatabaseHandler();
            double i = db.getInterest()/100;
            int j;
            double repay;

            for (j = 1; j <= duration; j++) {
                repay = (p1 / duration) + (amount * i);
                //System.out.println(j + "  : " + repay);
                total = total + repay;
                amount = amount - amount / duration;
            }
            totalInt = (int)Math.round(total);
            perMont = (int)Math.round(totalInt/duration);
//           System.out.println("total is :" + total);
//           System.out.println("Amount per month :" +total/duration);
            perMonthTxtField.setText(String.format("%,.0f", perMont));

            total_payment = totalInt;
            System.out.println("total is :" + total_payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserDetails(JSONObject jsonObject) throws JSONException {
        userObject = jsonObject;
        if(userObject.getString("role").equals("main admin") || userObject.getString("role").equals("admin")){
            borrower.setEditable(true);
            amountTxtField.setEditable(true);
            durationTxtField.setEditable(true);
            loanDate.setEditable(true);
            memberId.setEditable(true);
        }else{
            borrower.setEditable(false);
            amountTxtField.setEditable(false);
            durationTxtField.setEditable(false);
            loanDate.setEditable(false);
            memberId.setEditable(false);
        }
    }
}
