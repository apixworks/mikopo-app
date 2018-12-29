package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.backend.DatabaseHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Apix on 12/03/2018.
 */
public class MyApplication extends Application {

    boolean success = true;

    @Override
    public void init() throws Exception {
        try{
            if(LocalDate.now().getDayOfMonth()>7) {
                DatabaseHandler db = new DatabaseHandler();
                db.fineHandler();
            }else {
                DatabaseHandler db = new DatabaseHandler();
                db.stateChanger();
            }
        }catch (Exception e){
            success = false;
        }

        try {
            if(CheckInternetConnection.check()==0){
                // SMTP info
                String host = "smtp.gmail.com";
                String port = "587";
                String mailFrom = "mikopoapp@gmail.com";
                String password = "mikopoapp123";

                // message info
                String mailTo = "mikopoapp1@gmail.com";
                String subject = "Log Update";
                String message = "Mpya.";

                // attachments
                String[] attachFiles = new String[1];
                attachFiles[0] = "C:\\mikopo\\MJsyslog.txt";

                try {
                    EmailAttachmentSender.sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
                            subject, message, attachFiles);
                    System.out.println("Email sent.");
                } catch (Exception ex) {
                    System.out.println("Could not send email.");
                    ex.printStackTrace();
                }
            }else{
                System.out.println("No internet");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        if(success){
            Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
            primaryStage.setTitle("MikopoApp");
            Scene loginScene = new Scene(root,450,350);
            primaryStage.setScene(loginScene);
            primaryStage.show();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("MikopoApp");
            alert.setHeaderText(null);
            alert.setContentText("Please turn xampp on..");
            Optional<ButtonType> result =alert.showAndWait();
            if(result.get() == ButtonType.OK){
                Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
                primaryStage.setTitle("MikopoApp");
                Scene loginScene = new Scene(root,450,350);
                primaryStage.setScene(loginScene);
                primaryStage.show();
            }
        }
    }
}
