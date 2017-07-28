package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
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

    @FXML public TextField rate;
    @FXML public Button rateBtn;

    @FXML public TextField smsTxt;

    JSONObject userObject;

    @FXML public StackPane customer_Stackpane;
    Stage stage;
    //Database connection......................
    private Connection conn;
    private Statement st;

    //
    //loan release................................
    @FXML public TableView<LoanRelease> loanTable;
    @FXML public TableColumn<LoanRelease,String> ldate;
    @FXML public TableColumn<LoanRelease,String> lamount;
    @FXML public TableColumn<LoanRelease,Integer> S_No;
    private ObservableList<LoanRelease>datas;


    //repayment......................................
    @FXML public TableView<Repayment_Collected> repayment_table;
    @FXML public TableColumn<Repayment_Collected,String> Rdate;
    @FXML public TableColumn<Repayment_Collected,String> Ramount;
    @FXML public TableColumn<Repayment_Collected,String>  R_No;
    private ObservableList<Repayment_Collected> rdata;
    @FXML public TextField monthrepaysearch;

    private Connection connectDb(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mikopo_app";
            String user = "root";
            String password = "";
            Connection conni= DriverManager.getConnection(url,user,password);
            return conni;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void InitiLoanRelease() {
        try {
            conn = connectDb();
            datas = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount_borrowed)as AMount,MONTHNAME(date_of_loan) as dtmonths,YEAR(date_of_loan) as dtyears from loans GROUP BY MONTH(date_of_loan)");
            while (rs.next()) {
                int s= rs.getRow();
                String fulldt = rs.getString("dtmonths") + "/" + rs.getString("dtyears");
                //System.out.println(fulldt);
                datas.add(new LoanRelease(fulldt, rs.getString("AMount"), s));
            }

        } catch (Exception ex) {
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            //System.err.println("error :"+ex);
            ex.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        S_No.setCellValueFactory(new PropertyValueFactory<>("Sno"));
        ldate.setCellValueFactory(new PropertyValueFactory<>("lDate"));
        lamount.setCellValueFactory(new PropertyValueFactory<>("lamount"));
        loanTable.setItems(null);
        loanTable.setItems(datas);
    }

    @FXML private TextField searchmonth;

    public void Search_Month(ActionEvent actionEvent) {
        String mon=searchmonth.getText();
        InitiLoanReleaseSearch(mon);
    }

    //search loan release.........................
    public void InitiLoanReleaseSearch(String mon) {
        try {
            conn = connectDb();
            datas = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount_borrowed)as AMount,MONTHNAME(date_of_loan) as dtmonths,YEAR(date_of_loan) as dtyears from loans WHERE MONTHNAME(date_of_loan) LIKE '"+mon+"%' OR YEAR(date_of_loan) LIKE '"+mon+"%' GROUP BY MONTH(date_of_loan),YEAR(date_of_loan)");
            while (rs.next()) {
                int s= rs.getRow();
                String fulldt = rs.getString("dtmonths") + "/" + rs.getString("dtyears");
                //System.out.println(fulldt);
                datas.add(new LoanRelease(fulldt, rs.getString("AMount"), s));
            }

        } catch (Exception ex) {
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            //System.err.println("error :"+ex);
            ex.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        S_No.setCellValueFactory(new PropertyValueFactory<>("Sno"));
        ldate.setCellValueFactory(new PropertyValueFactory<>("lDate"));
        lamount.setCellValueFactory(new PropertyValueFactory<>("lamount"));
        loanTable.setItems(null);
        loanTable.setItems(datas);
    }

    public void Search_refresh(ActionEvent actionEvent) {
        InitiLoanRelease();
    }

    public void PrintDetailsLoans(ActionEvent actionEvent) {
        try {
            stage = (Stage) customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout, loanTable);
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void InitiRepayment_Collected() {
        try {
            conn = connectDb();
            rdata = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount)as paid,MONTHNAME(date) as lastmonths,YEAR(date) as yearmonth from payments GROUP BY MONTH(date),YEAR(date) ORDER BY YEAR(date) asc,MONTH (date) asc ");
            while (rs.next()) {
                String fulldt = rs.getString("lastmonths") + "/" + rs.getString("yearmonth");
                // System.out.println(fulldt);
                int s= rs.getRow();
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount"))
                rdata.add(new Repayment_Collected(fulldt, rs.getString("paid"), s));
            }

        } catch (Exception ex) {
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            //System.err.println("error :"+ex);
            ex.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        R_No.setCellValueFactory(new PropertyValueFactory<>("R_No"));
        Rdate.setCellValueFactory(new PropertyValueFactory<>("Rdate"));
        Ramount.setCellValueFactory(new PropertyValueFactory<>("Ramount"));
        repayment_table.setItems(null);
        repayment_table.setItems(rdata);
    }

    public void PrintDetailsRepayment(ActionEvent actionEvent) {

        try {
            stage = (Stage) customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout, repayment_table);
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void Report_In_Table(){
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewReportTables.fxml"));
            stage.setTitle("MikopoApp");
            //stage.setResizable(false);
            stage.isFullScreen();
            Scene reportTable = new Scene(fxmlLoader.load(),1000,620);
            stage.setScene(reportTable);
            //ViewReportTable_Controller repaymentFormController = fxmlLoader.<RepaymentFormController>getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ViewReport_Repayment(ActionEvent actionEvent) {
        Report_In_Table();
    }


    public void SearchRepayment_Month(ActionEvent actionEvent) {
        String mont=monthrepaysearch.getText();
        InitiRepayment_Collected(mont);
    }

    public void Searchrepayment_refresh(ActionEvent actionEvent) {
        InitiRepayment_Collected();

    }

    public void InitiRepayment_Collected(String mont) {
        try {
            conn = connectDb();
            rdata = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount)as paid,MONTHNAME(date) as lastmonths,YEAR(date) as yearmonth from payments where MONTHNAME(date) LIKE '"+mont+"%' OR YEAR(date) LIKE '"+mont+"%' GROUP BY MONTH(date),YEAR(date) ORDER BY YEAR(date) asc,MONTH (date) asc ");
            while (rs.next()) {
                String fulldt = rs.getString("lastmonths") + "/" + rs.getString("yearmonth");
                // System.out.println(fulldt);
                int s= rs.getRow();
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount")));
                rdata.add(new Repayment_Collected(fulldt, rs.getString("paid"), s));
            }

        } catch (Exception ex) {
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            //System.err.println("error :"+ex);
            ex.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        R_No.setCellValueFactory(new PropertyValueFactory<>("R_No"));
        Rdate.setCellValueFactory(new PropertyValueFactory<>("Rdate"));
        Ramount.setCellValueFactory(new PropertyValueFactory<>("Ramount"));
        repayment_table.setItems(null);
        repayment_table.setItems(rdata);
    }

    public void ViewReport_of_loanRelease_Summary(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoanRelease.fxml"));
            stage.setTitle("MikopoApp");
            //stage.setResizable(false);
            stage.isFullScreen();
            Scene reportTable = new Scene(fxmlLoader.load(), 1000, 620);
            stage.setScene(reportTable);
            //ViewReportTable_Controller repaymentFormController = fxmlLoader.<RepaymentFormController>getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options = FXCollections.observableArrayList("Chagua Role", "Admin", "Worker");
        choiceBox.setValue("Chagua Role");
        choiceBox.setItems(options);

        DatabaseHandler db = new DatabaseHandler();
        rate.setText(String.valueOf(db.getInterest()));

        InitiLoanRelease();
        InitiRepayment_Collected();

    }

    public void registerUser() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(fname.getText().equals("")||lname.getText().equals("")||uname.getText().equals("")||choiceBox.getValue().toString().toLowerCase().equals("chagua role")){
            alert.setContentText("Please fill all fields!");
            alert.showAndWait();
        }else {
            DatabaseHandler db = new DatabaseHandler();
            Result result = db.addUser(fname.getText(),lname.getText(),uname.getText(),fname.getText().toLowerCase()+"123",choiceBox.getValue().toString().toLowerCase(),"active");
            if(result.isSuccess()){
                alert.setContentText("Successful added!");
                initializeViewUsers();
                fname.setText("");
                lname.setText("");
                uname.setText("");
                try {
                    Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" registered User: MJ/U/"+result.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                alert.setContentText("UnSuccessful!");
            }
            alert.showAndWait();
        }

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
        try {
            System.out.println(userObject.getString("role"));
            user_table.setItems(db.getUsers(userObject.getString("role")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                                    if(user.getUser_no().equals("MJ/U/1")){
                                        statusBtn.setVisible(false);
                                        deleteBtn.setVisible(false);
                                    }
                                    statusBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            User user = getTableView().getItems().get(getIndex());
                                            DatabaseHandler db = new DatabaseHandler();
                                            db.updateUserStatus(statusBtn.getText().toLowerCase(),Integer.parseInt(user.getUser_no().substring(5)));
                                            initializeViewUsers();
                                        }
                                    });
                                    deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Alert alert = new Alert(Alert.AlertType.WARNING);
                                            alert.setTitle("Warning Dialog");
                                            alert.setHeaderText(null);alert.setContentText("Are you sure you want to delete the user?");
                                            Optional<ButtonType> result =alert.showAndWait();
                                            if(result.get() == ButtonType.OK){
                                                User user = getTableView().getItems().get(getIndex());
                                                DatabaseHandler db = new DatabaseHandler();
                                                if(db.deleteUser(Integer.parseInt(user.getUser_no().substring(5)))){
                                                    try {
                                                        Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" deleted User: MJ/U/"+user.getUser_no().substring(5));
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    initializeViewUsers();
                                                }
                                            }
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


    public void getUserDetails(JSONObject userObject){
       this.userObject = userObject;
        initializeViewUsers();
    }

    public void changeInterest() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(rate.getText().equals("")){
            alert.setContentText("Please fill the field!");
            alert.showAndWait();
        }else{
            DatabaseHandler db = new DatabaseHandler();
            if(db.setInterest(Double.parseDouble(rate.getText()))){
                alert.setContentText("Successful changed!");
                alert.showAndWait();
            }
        }

    }

    public void sendSMS(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(rate.getText().equals("")){
            alert.setContentText("Please fill the field!");
            alert.showAndWait();
        }else{
            DatabaseHandler db = new DatabaseHandler();
            if(SendSMSMany.sendSms(db.getPhoneNumbers(),0,smsTxt.getText())){
                alert.setContentText("Successful sent!");
                alert.showAndWait();
            }
        }
    }
}
