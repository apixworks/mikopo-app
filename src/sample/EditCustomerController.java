package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import sample.backend.DatabaseHandler;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Image image = new Image(getClass().getResourceAsStream("icons/account-circle.png"));

    }

    public void getId(int id){

        DatabaseHandler db = new DatabaseHandler();
        EditCustomer customer = db.editCustomer(id);
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
        company_phone.setText(customer.phone);
        company_loc.setText(customer.company_loc);
        checksum.setText(customer.checknumber);

        File file = new File("E:/MikopoPics","MJ.C."+id+".jpg");
        System.out.println(file.getName());
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

        company_name.requestFocus();
    }
}
