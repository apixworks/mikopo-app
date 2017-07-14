package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import sample.backend.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Apix on 12/07/2017.
 */
public class AdminController implements Initializable {

    @FXML public ChoiceBox choiceBox;
    @FXML public TableView<User> user_table;
    @FXML public TableColumn<User, String> user_no;
    @FXML public TableColumn<User, String> name;
    @FXML public TableColumn<User, String> role;
    @FXML public TableColumn<User, String> status;
    @FXML public TableColumn<User, String> action;

    @FXML public TextField fname;
    @FXML public TextField lname;
    @FXML public TextField uname;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("Chagua Role", "Admin", "Worker");
        choiceBox.setValue("Chagua Role");
        choiceBox.setItems(options);

        initializeViewUsers();
    }

    public void registerUser() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        DatabaseHandler db = new DatabaseHandler();
        if(db.addUser(fname.getText(),lname.getText(),uname.getText(),fname.getText()+"123",choiceBox.getValue().toString().toLowerCase(),"active")){
            alert.setContentText("Successful added!");
            initializeViewUsers();
        }else{
            alert.setContentText("UnSuccessful!");
        }
        alert.showAndWait();
    }

    public void initializeViewUsers() {
        user_no.setCellValueFactory(new PropertyValueFactory<>("user_no"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        setUpUserTable();
        DatabaseHandler db = new DatabaseHandler();
        user_table.setItems(null);
        user_table.setItems(db.getUsers());
    }

    public void setUpUserTable() {
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory =
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
                    @Override
                    public TableCell<User, String> call(TableColumn<User, String> param) {
                        final TableCell<User, String> cell = new TableCell<User, String>() {
                            final Button statusBtn = new Button("Block");
                            final Button deleteBtn = new Button("Delete");

                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    User user = getTableView().getItems().get(getIndex());
                                    if (user.getStatus().equals("active")) {
                                        statusBtn.setPrefWidth(65);
                                        statusBtn.setText("Block");
                                    } else {
                                        statusBtn.setPrefWidth(65);
                                        statusBtn.setText("Activate");
                                    }
                                    statusBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            User user = getTableView().getItems().get(getIndex());
                                            System.out.println(user.getUser_no());
                                            DatabaseHandler db = new DatabaseHandler();
                                            db.updateUserStatus(statusBtn.getText().toLowerCase(),Integer.parseInt(user.getUser_no().substring(5)));
                                            initializeViewUsers();
                                        }
                                    });
                                    deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {

                                        }
                                    });
                                    HBox hBox = new HBox();
                                    hBox.getChildren().addAll(statusBtn, deleteBtn);
                                    hBox.setAlignment(Pos.CENTER);
                                    hBox.setSpacing(5);
                                    setGraphic(hBox);
                                    this.setStyle("-fx-font-weight: normal");
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        action.setCellFactory(cellFactory);

        status.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {

            @Override
            public TableCell<User, String> call(TableColumn<User, String> p) {


                return new TableCell<User, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if(item.equals("deactive")) {
                                this.setStyle("-fx-background-color: #D9853B;-fx-border-color: #FFFFFF;" +
                                        "-fx-border-width: 1px;-fx-text-fill: #FFFFFF;-fx-font-size: 12px;-fx-font-weight: normal");
                            }
                            else {
                                this.setStyle("-fx-background-color:  #74AFAD;-fx-border-color: #EEEEEE;" +
                                        "-fx-border-width: 1px;-fx-text-fill: #FFFFFF;-fx-font-size: 12px;-fx-font-weight:normal;");
                            }
                            setText(item);
                        }
                    }
                };
            }
        });

        user_no.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setStyle("-fx-alignment: center-left");
                            setText(item);
                        }
                    }
                };
            }
        });

        name.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                return new TableCell<User, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setStyle("-fx-alignment: center-left");
                            setText(item);
                        }
                    }
                };
            }
        });
    }




}
