package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
        System.out.println(this.id);
        File file = new File("E:/MikopoPics","MJ.C."+id+".jpg");
        System.out.println(file.getName());
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
                Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: "+userObject.get("id")+" edited Customer: MJ/C/"+id);
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
