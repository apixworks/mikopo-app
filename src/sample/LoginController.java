package sample;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.backend.DatabaseHandler;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML  private JFXButton actionBtn;
    @FXML public TextField usernameTxtField;
    @FXML public TextField passwordTxtField;
    @FXML public Text actiontarget;

    DatabaseHandler db;

    @FXML protected void handleLoginButtonAction(){
        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();
        if(username.equals("")||password.equals("")){
           actiontarget.setText("Please fill all the fields");
        }else {
            if(Checker.isStringInt(username)){
                actiontarget.setText("Please check your username");
                passwordTxtField.setText("");
            }else {
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),ev -> {
                    db = new DatabaseHandler();
                    if (db.login(username,password)){
                        try{
                            Stage stage = (Stage) actionBtn.getScene().getWindow();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_window.fxml"));
                            Scene MainScene = new Scene(fxmlLoader.load(),1100,800);
                            stage.setTitle("MikopoApp");
                            stage.setScene(MainScene);
                            stage.setMaximized(true);
                            MainWindowController mainWindowController = fxmlLoader.<MainWindowController>getController();
                            mainWindowController.getUserDetails(db.getUserDetails(db.getId()));
                            stage.show();
                        }catch (IOException e){
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else {
                        actiontarget.setText("Please check your login credentials");
                        passwordTxtField.setText("");
                    }
                }));
                timeline.play();
            }
        }
    }
}