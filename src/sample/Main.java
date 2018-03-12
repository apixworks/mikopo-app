package sample;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.backend.DatabaseHandler;

import java.io.IOException;
import java.time.LocalDate;

public class Main{

    public static void main(String[] args) {
        LauncherImpl.launchApplication(MyApplication.class, MyPreloader.class, args);
    }

}
