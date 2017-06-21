package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.backend.DatabaseHandler;

import java.io.File;

/**
 * Created by Apix on 20/06/2017.
 */
public class ViewCustomerController {
    @FXML public ImageView customer_image;
    @FXML public Label name;
    @FXML public Label gender;
    @FXML public Label dob;
    @FXML public Label phone;
    @FXML public Label email;
    @FXML public Label postal;
    @FXML public Label bank;
    @FXML public Label account_num;
    @FXML public Label company_name;
    @FXML public Label company_loc;
    @FXML public Label company_phone;
    @FXML public Label check_num;
    @FXML public Label num_loans;

    public void getId(int id){

        DatabaseHandler db = new DatabaseHandler();
        EditCustomer customer = db.viewCustomer(id);
        //Path target = Paths.get("E:/MikopoPics",savedImage.getName());

        name.setText(customer.fname+" "+customer.mname+" "+customer.lname);
        gender.setText(customer.gender);
        dob.setText(customer.dob.toString());
        phone.setText(customer.phone);
        email.setText(customer.email);
        postal.setText(customer.postal);
        bank.setText(customer.bank);
        account_num.setText(customer.acc_no);
        company_name.setText(customer.company_name);
        company_phone.setText(customer.company_phone);
        company_loc.setText(customer.company_loc);
        check_num.setText(customer.checknumber);
        num_loans.setText(String.valueOf(customer.num_loans));

        File file = new File("E:/MikopoPics","MJ.C."+id+".jpg");
        Image image = new Image(file.toURI().toString());
        customer_image.setImage(image);
    }
}
