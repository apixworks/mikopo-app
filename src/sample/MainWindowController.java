package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    @FXML public MenuButton profBtn;
    @FXML public Button btnAdmin;

    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MenuItem menuItem1 = new MenuItem("Change Password");
        MenuItem menuItem2 = new MenuItem("Log out");
        profBtn.getItems().addAll(menuItem1,menuItem2);
        menuItem1.setOnAction(event -> {
            try {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/change_password.fxml"));
                stage.setTitle("MikopoApp");
                Scene changePasswordScene = new Scene(fxmlLoader.load(),475,315);
                stage.setScene(changePasswordScene);
                ChangePasswordController changePasswordController = fxmlLoader.<ChangePasswordController>getController();
                changePasswordController.getUserDetails(userObject);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuItem2.setOnAction(event -> {
            try {
                try {
                    Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" logged out");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) profBtn.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
                stage.setTitle("MikopoApp");
                stage.setMaximized(false);
                Scene loginScene = new Scene(root,450,350);
                stage.setScene(loginScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        try{
//            Parent Mainroot = FXMLLoader.load(getClass().getResource("view_customers_loans.fxml"));
//            borderP.setCenter(Mainroot);
//        }catch (IOException e){
//            e.printStackTrace();
//        }


    }

    public  void RegCustomerClick(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/register_customer.fxml"));
                borderP.setCenter(fxmlLoader.load());
                RegisterCustomerController registerCustomerController = fxmlLoader.<RegisterCustomerController>getController();
                registerCustomerController.getUserDetails(userObject);
            }catch (IOException e){
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    @FXML
    public  void viewCustomerClick(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),ev ->{
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/view_customers_loans.fxml"));
                borderP.setCenter(fxmlLoader.load());
                ViewCustomersLoansController viewCustomersLoansController = fxmlLoader.<ViewCustomersLoansController>getController();
                viewCustomersLoansController.getUserDetails(userObject);
            }catch (IOException e){
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    @FXML
    public  void viewRepayments(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),ev ->{
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/view_repayments.fxml"));
                borderP.setCenter(fxmlLoader.load());
                ViewRepaymentsController viewRepaymentsController = fxmlLoader.<ViewRepaymentsController>getController();
                viewRepaymentsController.getUserDetails(userObject);
            }catch (IOException e){
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    @FXML
    public void viewReport(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),ev ->{
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/view_report.fxml"));
                borderP.setCenter(fxmlLoader.load());
            }catch (IOException e){
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    @FXML
    public void viewAdmin(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),ev ->{
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/admin_panel.fxml"));
                borderP.setCenter(fxmlLoader.load());
                AdminController adminController = fxmlLoader.<AdminController>getController();
                adminController.getUserDetails(userObject);
            }catch (IOException e){
                e.printStackTrace();
            }
        }));
        timeline.play();
    }

    public void getUserDetails(JSONObject jsonObject){
        try {
            userObject = jsonObject;
            profBtn.setText("Hi! "+jsonObject.get("fname"));
            if(jsonObject.get("role").equals("admin") || jsonObject.get("role").equals("main admin")){
                btnAdmin.setVisible(true);
            }else {
                btnAdmin.setVisible(false);
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/view_repayments.fxml"));
            borderP.setCenter(fxmlLoader.load());
            ViewRepaymentsController viewRepaymentsController = fxmlLoader.<ViewRepaymentsController>getController();
            viewRepaymentsController.getUserDetails(userObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

