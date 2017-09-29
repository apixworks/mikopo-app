package sample;

import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sample.models.BorrowerIDAndName;
import sample.Checker;
import sample.Logger;
import sample.models.Result;
import sample.backend.DatabaseHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Apix on 21/04/2017.
 */
public class RegisterCustomerController implements Initializable {
    @FXML public TextField perMonthTxtField;
    @FXML public ChoiceBox dhamanachoice;
    @FXML public TextField borrower;
    @FXML public TextField amountTxtField;
    @FXML public TextField durationTxtField;
    @FXML public DatePicker loanDate;
    @FXML public TextField memberId;
    @FXML public TextField propertyId;
    @FXML public TextField propertyName;
    @FXML public TextArea desc;
    @FXML public Text actionTxt;
    @FXML public JFXDialogLayout borrowerDialog;
    @FXML public GridPane gridPane;
    @FXML public Label name;
    @FXML public Label id;
    @FXML public JFXDialogLayout mdhaminiDialog;
    @FXML public GridPane mdhamini_gridPane;
    @FXML public Label mdhamini_name;
    @FXML public Label mdhamini_id;

    @FXML public Button submitBtn;
    @FXML public JFXTextField fname;
    @FXML public TextField lname;
    @FXML public TextField mname;
    @FXML public RadioButton male;
    @FXML public RadioButton female;
    @FXML public DatePicker dob;
    @FXML public TextField phone;
    @FXML public TextField email;
    @FXML public TextField postal;
    @FXML public TextField bank;
    @FXML public TextField account_no;
    @FXML public TextField company_name;
    @FXML public TextField company_phone;
    @FXML public TextField company_loc;
    @FXML public TextField checksum;
    @FXML public Text photoBtnTxt;

    File savedImage;

    double perMont;
    double total = 0;
    double totalInt;
    int l_memberId;
    ToggleGroup genderChoice = new ToggleGroup();
    JSONObject userObject;
    BorrowerIDAndName borrowerIDAndName;
    DatabaseHandler db;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpLoanView();
        setUpCustomerView();
    }

    private void setUpCustomerView(){

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isAfter(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        dob.setDayCellFactory(dayCellFactory);
    }

    private void setUpLoanView(){
        ObservableList<String> options = FXCollections.observableArrayList("Chagua Dhamana","Person","Property","Person&Property");
        dhamanachoice.setValue("Chagua Dhamana");
        dhamanachoice.setItems(options);
        male.setToggleGroup(genderChoice);
        female.setToggleGroup(genderChoice);

        dhamanachoice.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
            setDhamanaField();
        });

        borrower.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
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

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isAfter(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        loanDate.setDayCellFactory(dayCellFactory);
    }

    public void registerCustomer(){
//        if(savedImage == null){
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Registration Status");
//            alert.setHeaderText(null);
//            alert.setContentText("Customer Image must be added!");
//            alert.showAndWait();
//        }else {
//            String c_fname = fname.getText();
//            String c_lname = lname.getText();
//            String c_mname = mname.getText();
//            RadioButton selectedRadioButton = (RadioButton) genderChoice.getSelectedToggle();
//            String gender = selectedRadioButton.getText();
//            LocalDate c_dob = dob.getValue();
//            String c_phone = phone.getText();
//            String c_email = email.getText();
//            String c_postal = postal.getText();
//            String c_bank = bank.getText();
//            String c_account_no = account_no.getText();
//            String c_company_name = company_name.getText();
//            String c_company_phone = company_phone.getText();
//            String c_company_loc = company_loc.getText();
//            String c_checksum = checksum.getText();
//            LocalDate c_reg_date = LocalDate.now();
//
//            if(c_fname.equals("")||c_lname.equals("")||c_mname.equals("")||gender.equals("")||c_dob==null||c_phone.equals("")||c_bank.equals("")||c_account_no.equals("")){
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Empty Field");
//                alert.setHeaderText(null);
//                alert.setContentText("Please fill all fields!");
//                alert.showAndWait();
//            }else {
//                DatabaseHandler db = new DatabaseHandler();
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Registration Status");
//                alert.setHeaderText(null);
//                Result result = db.registerCustomer(c_fname,c_lname,c_mname,gender,c_dob,c_phone,c_email,c_postal,"",c_bank,c_account_no,c_company_name,c_company_phone,c_company_loc,c_checksum,c_reg_date);
//                if(result.isSuccess()){
//                    int mpya = result.getId();
//                    fname.setText("");
//                    lname.setText("");
//                    mname.setText("");
//                    genderChoice.selectToggle(null);
//                    dob.setValue(null);
//                    photoBtnTxt.setText("");
//                    phone.setText("");
//                    email.setText("");
//                    postal.setText("");
//                    bank.setText("");
//                    account_no.setText("");
//                    company_name.setText("");
//                    company_phone.setText("");
//                    company_loc.setText("");
//                    checksum.setText("");
//
//                    try {
//                        Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" registered Customer: MJ/C/"+mpya);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    /**
//                     * Needs Editing.
//                     */
//                    File newFile = new File("E:/MikopoPics",savedImage.getName());
//                    if(savedImage.getName().endsWith("pg")) {
//                        File dbFile1 = new File("E:/MikopoPics", "MJ.C."+mpya+".jpg");
//                        newFile.renameTo(dbFile1);
//                        db.updateProfPhoto(dbFile1.getName(),result.getId());
//                    }
//                    else{
//                        File dbFile2 = new File("E:/MikopoPics","MJ.C."+mpya+".png");
//                        newFile.renameTo(dbFile2);
//                        db.updateProfPhoto(dbFile2.getName(),result.getId());
//                    }
//
//                    alert.setContentText("Successful Registered!");
//                    alert.showAndWait();
//                }else{
//                    alert.setContentText("Registration UnSuccessful!");
//                    alert.showAndWait();
//                }
//            }
//
//        }
        String c_fname = "";
        String c_lname = "";
        String c_mname = "";
        String gender = "";
        LocalDate c_dob = null;
        String c_phone = "";
        String c_email = "";
        String c_postal = "";
        String c_bank = "";
        String c_account_no = "";
        String c_company_name = "";
        String c_company_phone = "";
        String c_company_loc = "";
        String c_checksum = "";
        LocalDate c_reg_date = LocalDate.now();

            try{
                c_fname = fname.getText();
                c_lname = lname.getText();
                c_mname = mname.getText();
                RadioButton selectedRadioButton = (RadioButton) genderChoice.getSelectedToggle();
                gender = selectedRadioButton.getText();
                c_dob = dob.getValue();
                String phone_string[] = phone.getText().split("-");
                for(int i=0;i<phone_string.length;i++){
                    c_phone = c_phone + phone_string[i];
                }
                c_email = email.getText();
                c_postal = postal.getText();
                c_bank = bank.getText();
                c_account_no = account_no.getText();
                c_company_name = company_name.getText();
                c_company_phone = company_phone.getText();
                c_company_loc = company_loc.getText();
                c_checksum = checksum.getText();
            }catch(Exception e){
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setHeaderText(null);
                alert.setContentText("Please choose gender!");
                alert.showAndWait();
            }


            if(c_fname.equals("")||c_lname.equals("")||c_mname.equals("")||gender.equals("")||c_dob==null||c_phone.equals("")||c_bank.equals("")||c_account_no.equals("")||savedImage==null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Field");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields!");
                alert.showAndWait();
            }else {
                DatabaseHandler db = new DatabaseHandler();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Status");
                alert.setHeaderText(null);
                Result result = db.registerCustomer(c_fname,c_lname,c_mname,gender,c_dob,c_phone,c_email,c_postal,"",c_bank,c_account_no,c_company_name,c_company_phone,c_company_loc,c_checksum,c_reg_date);
                if(result.isSuccess()){
                    int mpya = result.getId();
                    fname.setText("");
                    lname.setText("");
                    mname.setText("");
                    genderChoice.selectToggle(null);
                    dob.setValue(null);
                    photoBtnTxt.setText("");
                    phone.setText("");
                    email.setText("");
                    postal.setText("");
                    bank.setText("");
                    account_no.setText("");
                    company_name.setText("");
                    company_phone.setText("");
                    company_loc.setText("");
                    checksum.setText("");

                    try {
                        Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" registered Customer: MJ/C/"+mpya);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /**
                     * Needs Editing.
                     */
                    //File newFile = new File("E:/MikopoPics",savedImage.getName());
                    File newFile = new File("C:/mikopo/MikopoPics",savedImage.getName());
                    if(savedImage.getName().endsWith("pg")) {
                        //File dbFile1 = new File("E:/MikopoPics", "MJ.C."+mpya+".jpg");
                        File dbFile1 = new File("C:/mikopo/MikopoPics", "MJ.C."+mpya+".jpg");
                        newFile.renameTo(dbFile1);
                        db.updateProfPhoto(dbFile1.getName(),result.getId());
                    }
                    else{
                        //File dbFile2 = new File("E:/MikopoPics","MJ.C."+mpya+".png");
                        File dbFile2 = new File("C:/mikopo/MikopoPics","MJ.C."+mpya+".png");
                        newFile.renameTo(dbFile2);
                        db.updateProfPhoto(dbFile2.getName(),result.getId());
                    }

                    alert.setContentText("Successful Registered!\nThe registered customer ID is: MJ/C/"+mpya);
                    alert.showAndWait();
                }else{
                    alert.setContentText("Registration UnSuccessful!");
                    alert.showAndWait();
                }
            }

    }

    public void registerLoan(){

        String[] amountString = amountTxtField.getText().split(",");
        String amounter = "";
        for(int i=0;i<amountString.length;i++){
            amounter = amounter + amountString[i];
        }

        LocalDate localDate = null;
        String monthEnd = "";
        int l_borrower = 0;
        double l_amount = 0;
        int l_duration = 0;
        String l_date = "";
        String l_dhamanaType = "";
        String l_propertyId = "";
        String l_propertyName = "";
        String l_desc = "";

        try{
            localDate = loanDate.getValue();
            if(localDate.getDayOfMonth()<18){
                monthEnd = localDate.plusMonths(1).withDayOfMonth(1).minusDays(1).toString();
            }else{
                monthEnd = localDate.plusMonths(2).withDayOfMonth(1).minusDays(1).toString();
            }
            l_borrower = Integer.parseInt(borrower.getText());
            l_amount = Double.parseDouble(amounter);
            l_duration = Integer.parseInt(durationTxtField.getText());
            l_date = loanDate.getValue().toString();
            l_dhamanaType = dhamanachoice.getValue().toString();
            try{
                l_memberId = Integer.parseInt(memberId.getText());
            }catch (Exception e){
                l_memberId = 0;
            }
            l_propertyId = propertyId.getText();
            l_propertyName = propertyName.getText();
            l_desc = desc.getText();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(loanDate.getValue()==null||l_borrower==0||l_amount==0||l_duration==0||l_dhamanaType.toLowerCase().equals("chagua dhamana")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Field");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        }else{
            DatabaseHandler db = new DatabaseHandler();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Status");
            alert.setHeaderText(null);

            Result result = db.registerLoan(l_borrower,l_amount,l_duration,l_date,perMont,totalInt,monthEnd,l_dhamanaType,l_memberId,l_propertyId,l_propertyName,l_desc,db.getInterest());
            if(result.isSuccess()){
                int mpya = result.getId();

                try {
                    Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" registered Loan: MJ/L/"+mpya);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                File newFile = new File("E:/MikopoFiles/mpya");
//                File dbFile1 = new File("E:/MikopoFiles/MJ.L."+result.getId());
                File newFile = new File("C:/mikopo/MikopoFiles/mpya");
                File dbFile1 = new File("C:/mikopo/MikopoFiles/MJ.L."+result.getId());
                newFile.renameTo(dbFile1);
                db.updateLoanFile(dbFile1.getName(),result.getId());

                alert.setContentText("Successful Registered!");
                alert.showAndWait();

                borrower.setText("");
                amountTxtField.setText("");
                durationTxtField.setText("");
                memberId.setText("");
                propertyId.setText("");
                propertyName.setText("");
                desc.setText("");
                perMonthTxtField.setText("");
                actionTxt.setText("");
                dhamanachoice.setValue("Chagua Dhamana");
                loanDate.setValue(null);

            }else {
                alert.setContentText("Registration UnSuccessful!");
                alert.showAndWait();
            }
        }

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
                System.out.println(j + "  : " + repay);
                total = total + repay;
                amount = amount - p1 / duration;
            }
            totalInt = (int)Math.round(total);
            perMont = (int)Math.round(total/duration);
            perMonthTxtField.setText(String.format("%,.0f", perMont));
        } catch (Exception e) {
            e.printStackTrace();
            perMonthTxtField.setText("");
        }
    }

    private void setDhamanaField(){
        String dhamana = dhamanachoice.getValue().toString();
        if (dhamana.equals("Person")){
            memberId.setDisable(false);
            propertyId.setDisable(true);
            propertyName.setDisable(true);
        }else if(dhamana.equals("Property")){
            propertyId.setDisable(false);
            propertyName.setDisable(false);
            memberId.setDisable(true);
        }else if(dhamana.equals("Chagua Dhamana")){
            memberId.setDisable(true);
            propertyId.setDisable(true);
            propertyName.setDisable(true);
        }else {
            memberId.setDisable(false);
            propertyId.setDisable(false);
            propertyName.setDisable(false);
        }
    }

    public void chooseLoanFiles(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Loan Files");
        fileChooser.setInitialFileName("");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Files", "*.jpg", "*.png", "*.pdf", ".*doc");
        fileChooser.getExtensionFilters().addAll(extFilter);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(submitBtn.getScene().getWindow());

        if (selectedFiles != null) {
            try {
                //File dir = new File("E:/MikopoFiles/mpya");
                File dir = new File("C:/mikopo/MikopoFiles/mpya");
                dir.mkdir();
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

    public void chooseImg(){
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Choose Image ");
        imageChooser.setInitialFileName("");
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        imageChooser.getExtensionFilters().addAll(extFilter);

        savedImage = imageChooser.showSaveDialog(submitBtn.getScene().getWindow());

        if (savedImage != null) {
            try {
                //Path target = Paths.get("E:/MikopoPics",savedImage.getName());
                Path target = Paths.get("C:/mikopo/MikopoPics",savedImage.getName());
                Files.copy(savedImage.toPath(),target, StandardCopyOption.REPLACE_EXISTING);

            }
            catch(Exception e) {
                e.printStackTrace();
                photoBtnTxt.setText("An ERROR occurred while saving the image!");
                return;
            }
            photoBtnTxt.setText("Image saved: " + savedImage.toString());
        }
        else {
            photoBtnTxt.setText("Image save cancelled.");
        }
    }

    public void getUserDetails(JSONObject jsonObject){
        userObject = jsonObject;
    }
}
