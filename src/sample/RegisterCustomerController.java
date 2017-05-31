package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

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

    @FXML public Button submitBtn;
    @FXML public TextField fname;
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

    int perMont;
    double total = 0;
    int totalInt;
    ToggleGroup genderChoice = new ToggleGroup();
    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("Chagua Dhamana","Person","Property","Person&Property");
        dhamanachoice.setValue("Chagua Dhamana");
        dhamanachoice.setItems(options);
        male.setToggleGroup(genderChoice);
        female.setToggleGroup(genderChoice);
    }

    public void register(){
        String c_fname = fname.getText();
        String c_lname = lname.getText();
        String c_mname = mname.getText();
        RadioButton selectedRadioButton = (RadioButton) genderChoice.getSelectedToggle();
        String gender = selectedRadioButton.getText();
        LocalDate c_dob = dob.getValue();
        String c_phone = phone.getText();
        String c_email = email.getText();
        String c_postal = postal.getText();
        String c_bank = bank.getText();
        String c_account_no = account_no.getText();
        String c_company_name = company_name.getText();
        String c_company_phone = company_phone.getText();
        String c_company_loc = company_loc.getText();
        String c_checksum = checksum.getText();

        DatabaseHandler db = new DatabaseHandler();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Status");
        alert.setHeaderText(null);
        Result result = db.registerCustomer(c_fname,c_lname,c_mname,gender,c_dob,c_phone,c_email,c_postal,"",c_bank,c_account_no,c_company_name,c_company_phone,c_company_loc,c_checksum);
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
                Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: "+userObject.get("id")+" registered Customer: MJ/C/"+mpya);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            File newFile = new File("E:/MikopoPics",savedImage.getName());
            if(savedImage.getName().endsWith("pg")) {
                File dbFile1 = new File("E:/MikopoPics", "MJ.C."+mpya+".jpg");
                newFile.renameTo(dbFile1);
                db.updateProfPhoto(dbFile1.getName(),result.getId());
            }
            else{
                File dbFile2 = new File("E:/MikopoPics","MJ.C."+mpya+".png");
                newFile.renameTo(dbFile2);
                db.updateProfPhoto(dbFile2.getName(),result.getId());
            }

            alert.setContentText("Successful Registered!");
            alert.showAndWait();
        }else{
            alert.setContentText("Registration UnSuccessful!");
            alert.showAndWait();
        }
    }

    /**
     * Needs Editing.
     */
    public void registerLoan(){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String monthEnd;
        if(calendar.get(Calendar.DAY_OF_MONTH)<18){
            monthEnd = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1).toString();
        }else{
            monthEnd = LocalDate.now().plusMonths(2).withDayOfMonth(1).minusDays(1).toString();
        }
        int l_borrower = Integer.parseInt(borrower.getText());
        int l_amount = Integer.parseInt(amountTxtField.getText());
        int l_duration = Integer.parseInt(durationTxtField.getText());
        String l_date = loanDate.getValue().toString();
        String l_dhamanaType = dhamanachoice.getValue().toString();
        int l_memberId = Integer.parseInt(memberId.getText());
        String l_propertyId = propertyId.getText();
        String l_propertyName = propertyName.getText();
        String l_desc = desc.getText();

        DatabaseHandler db = new DatabaseHandler();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Status");
        alert.setHeaderText(null);

        Result result = db.registerLoan(l_borrower,l_amount,l_duration,l_date,perMont,totalInt,monthEnd,l_dhamanaType,l_memberId,l_propertyId,l_propertyName,l_desc);
        if(result.isSuccess()){
            int mpya = result.getId();

            try {
                Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: "+userObject.get("id")+" registered Loan: MJ/L/"+mpya);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            alert.setContentText("Successful Registered!");
            alert.showAndWait();

            borrower.setText("");
            amountTxtField.setText("");
            durationTxtField.setText("");
            memberId.setText("");
            propertyId.setText("");
            propertyName.setText("");
            desc.setText("");

        }else {
            alert.setContentText("Registration UnSuccessful!");
            alert.showAndWait();
        }

    }

    public void checkAmount() {
        try {
            double amount = Double.parseDouble(amountTxtField.getText());
            int duration = Integer.parseInt(durationTxtField.getText());
            double p1 = amount;
            double i = 0.15;
            int j;
            double repay;

            for (j = 1; j <= duration; j++) {
                repay = (p1 / duration) + (amount * i);
                System.out.println(j + "  : " + repay);
                total = total + repay;
                amount = amount - amount / duration;
            }
            totalInt = (int)Math.round(total);
            perMont = (int)Math.round(total/duration);
            System.out.println("total is :" + total);
            System.out.println("Amount per month :" +perMont);
            perMonthTxtField.setText(""+perMont);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public void setDhamanaField(){
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please choice among the options");
            alert.showAndWait();
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
        File savedFile = fileChooser.showSaveDialog(submitBtn.getScene().getWindow());

        if (savedFile != null) {
            try {
                //saveFileRoutine(savedFile);
                Path target = Paths.get("E:/MikopoFiles",savedFile.getName());
                Files.copy(savedFile.toPath(),target, StandardCopyOption.REPLACE_EXISTING);
            }
            catch(Exception e) {
                e.printStackTrace();
                actionTxt.setText("An ERROR occurred while saving the file!");
                return;
            }
            actionTxt.setText("File saved: " + savedFile.toString());
        }
        else {
            actionTxt.setText("File save cancelled.");
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
                Path target = Paths.get("E:/MikopoPics",savedImage.getName());
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
