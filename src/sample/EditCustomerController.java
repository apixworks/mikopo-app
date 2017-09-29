package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Logger;
import sample.backend.DatabaseHandler;
import sample.models.EditCustomer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * Created by Apix on 21/05/2017.
 */
public class EditCustomerController implements Initializable{
    @FXML public ImageView imageView;
    @FXML public Button editBtn;
    @FXML public TextField fname;
    @FXML public TextField lname;
    @FXML public TextField mname;
    @FXML public TextField gender;
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

    int id;
    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Image image = new Image(getClass().getResourceAsStream("icons/account-circle.png"));

    }

    public void getId(int id){

        DatabaseHandler db = new DatabaseHandler();
        EditCustomer customer = db.viewCustomer(id);
        //Path target = Paths.get("E:/MikopoPics",savedImage.getName());

        fname.setText(customer.fname);
        lname.setText(customer.lname);
        mname.setText(customer.mname);
        gender.setText(customer.gender);
        dob.setValue(customer.dob.toLocalDate());
        phone.setText(customer.phone);
        email.setText(customer.email);
        postal.setText(customer.postal);
        bank.setText(customer.bank);
        account_no.setText(customer.acc_no);
        company_name.setText(customer.company_name);
        company_phone.setText(customer.company_phone);
        company_loc.setText(customer.company_loc);
        checksum.setText(customer.checknumber);
        this.id=id;
        //File file = new File("E:/MikopoPics","MJ.C."+id+".jpg");
        File file = new File("C:/mikopo/MikopoPics","MJ.C."+id+".jpg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        company_name.requestFocus();
    }

    public void editCustomerDetails(){
        String phoneString = phone.getText();
        String emailString = email.getText();
        String postalString = postal.getText();
        String bankString = bank.getText();
        String accountString = account_no.getText();
        String companyString = company_name.getText();
        String company_phoneString = company_phone.getText();
        String company_locString = company_loc.getText();
        String checksumString = checksum.getText();

        DatabaseHandler db = new DatabaseHandler();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(db.editCustomerDetails(id,phoneString,emailString,postalString,bankString,accountString,companyString,company_locString,
                company_phoneString,checksumString)){
            alert.setContentText("Successful Edited!");
            try {
                Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" edited Customer: MJ/C/"+id);
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

    public void addEditPhoto(){
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Choose Image ");
        imageChooser.setInitialFileName("");
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        imageChooser.getExtensionFilters().addAll(extFilter);

        File savedImage = imageChooser.showSaveDialog(imageView.getScene().getWindow());

        if (savedImage != null) {
            try {
                //Path target = Paths.get("E:/MikopoPics",savedImage.getName());
                Path target = Paths.get("C:/mikopo/MikopoPics","MJ.C."+id+".jpg");
                Files.copy(savedImage.toPath(),target, StandardCopyOption.REPLACE_EXISTING);

            }
            catch(Exception e) {
                e.printStackTrace();
                //photoBtnTxt.setText("An ERROR occurred while saving the image!");
                //return;
            }
           // photoBtnTxt.setText("Image saved: " + savedImage.toString());
            DatabaseHandler db = new DatabaseHandler();
            db.updateProfPhoto("MJ.C."+id+".jpg",id);
            File file = new File("C:/mikopo/MikopoPics","MJ.C."+id+".jpg");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        }
        else {
            //photoBtnTxt.setText("Image save cancelled.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Image save cancelled.");
            alert.showAndWait();
        }
    }

    public void getUserDetails(JSONObject jsonObject) throws JSONException {
            userObject = jsonObject;
            if(userObject.getString("role").equals("main admin") || userObject.getString("role").equals("admin")){
                fname.setEditable(true);
                lname.setEditable(true);
                mname.setEditable(true);
                gender.setEditable(true);
                dob.setEditable(true);
            }else{
                fname.setEditable(false);
                lname.setEditable(false);
                mname.setEditable(false);
                gender.setEditable(false);
                dob.setEditable(false);
            }
    }
}
