package sample;

import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

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

    DatabaseHandler db;
    BorrowerIDAndName borrowerIDAndName;
    int loan_id;
    JSONObject userObject;

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
        loan_id=id;

        desc.requestFocus();
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
                File dir = new File("E:/MikopoFiles/MJ.L."+loan_id);
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(db.editLoanDetails(loan_id,propertyId.getText(),propertyName.getText(),desc.getText())){
            alert.setContentText("Successful Edited!");
            try {
                Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: "+userObject.get("id")+" edited Loan: MJ/L/"+id);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            alert.setContentText("UnSuccessful try Again!");
        }
        alert.showAndWait();
    }

    public void getUserDetails(JSONObject jsonObject){
        userObject = jsonObject;
    }
}
