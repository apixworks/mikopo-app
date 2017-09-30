package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.backend.DatabaseHandler;

import java.io.IOException;
import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("layouts/login.fxml"));
        primaryStage.setTitle("MikopoApp");
        Scene loginScene = new Scene(root,450,350);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        if(LocalDate.now().getDayOfMonth()>7) {
            DatabaseHandler db = new DatabaseHandler();
            db.fineHandler();
        }else {
            DatabaseHandler db = new DatabaseHandler();
            db.stateChanger();
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
                //attachFiles[0] = "E:\\MJsyslog.txt";
                attachFiles[0] = "C:\\mikopo\\MJsyslog.txt";
                //attachFiles[1] = "e:/Test/Music.mp3";
                //attachFiles[2] = "e:/Test/Video.mp4";

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

        launch(args);
    }

}
