package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Apix on 21/04/2017.
 */
public class MainWindowController implements Initializable {
    @FXML public BorderPane borderP;
    @FXML public Button profBtn;

    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Parent Mainroot = FXMLLoader.load(getClass().getResource("view_customers_loans.fxml"));
            borderP.setCenter(Mainroot);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public  void RegCustomerClick(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register_customer.fxml"));
            borderP.setCenter(fxmlLoader.load());
            RegisterCustomerController registerCustomerController = fxmlLoader.<RegisterCustomerController>getController();
            registerCustomerController.getUserDetails(userObject);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public  void viewCustomerClick(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view_customers_loans.fxml"));
            borderP.setCenter(fxmlLoader.load());
            ViewCustomersLoansController viewCustomersLoansController = fxmlLoader.<ViewCustomersLoansController>getController();
            viewCustomersLoansController.getUserDetails(userObject);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public  void viewRepayments(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view_repayments.fxml"));
            borderP.setCenter(fxmlLoader.load());
            ViewRepaymentsController viewRepaymentsController = fxmlLoader.<ViewRepaymentsController>getController();
            viewRepaymentsController.getUserDetails(userObject);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void getUserDetails(JSONObject jsonObject){
        try {
            userObject = jsonObject;
            profBtn.setText("Hi! "+jsonObject.get("fname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

