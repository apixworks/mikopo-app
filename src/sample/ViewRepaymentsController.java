package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Apix on 11/05/2017.
 */
public class ViewRepaymentsController implements Initializable,EventHandler<ActionEvent>{
    @FXML public TableView<Loan> one_month_table;
    @FXML public TableColumn<Loan,String> one_l_no;
    @FXML public TableColumn<Loan,String> one_borrower;
    @FXML public TableColumn<Loan,String> one_date;
    @FXML public TableColumn<Loan,Double> one_interest;
    @FXML public TableColumn<Loan,String> one_amount;
    @FXML public TableColumn<Loan,Integer> one_duration;
    @FXML public TableColumn<Loan,String> one_total_pay;
    @FXML public TableColumn<Loan,String> one_per_month;
    @FXML public TableColumn<Loan,String> one_due;
    @FXML public TableColumn<Loan,String> one_last_paymonth;
    @FXML public TableColumn<Loan,String> one_amount_paid;
    @FXML public TableColumn<Loan,String> one_last_pay;
    @FXML public TableColumn<Loan,String> one_amount_rem;
    @FXML public TableColumn<Loan,String> one_status;
    @FXML public TableColumn<Loan,String> one_action;
    @FXML public Button one_sms_all;
    @FXML public Button one_edit_sms;

    @FXML public TableView<Loan> two_month_table;
    @FXML public TableColumn<Loan,String> two_l_no;
    @FXML public TableColumn<Loan,String> two_borrower;
    @FXML public TableColumn<Loan,String> two_date;
    @FXML public TableColumn<Loan,Double> two_interest;
    @FXML public TableColumn<Loan,String> two_amount;
    @FXML public TableColumn<Loan,Integer> two_duration;
    @FXML public TableColumn<Loan,String> two_total_pay;
    @FXML public TableColumn<Loan,String> two_per_month;
    @FXML public TableColumn<Loan,String> two_due;
    @FXML public TableColumn<Loan,String> two_last_paymonth;
    @FXML public TableColumn<Loan,String> two_amount_paid;
    @FXML public TableColumn<Loan,String> two_last_pay;
    @FXML public TableColumn<Loan,String> two_amount_rem;
    @FXML public TableColumn<Loan,String> two_status;
    @FXML public TableColumn<Loan,String> two_action;
    @FXML public Button two_sms_all;
    @FXML public Button two_edit_sms;

    @FXML public TableView<Loan> penalty_table;
    @FXML public TableColumn<Loan,String> penalty_l_no;
    @FXML public TableColumn<Loan,String> penalty_borrower;
    @FXML public TableColumn<Loan,String> penalty_date;
    @FXML public TableColumn<Loan,Double> penalty_interest;
    @FXML public TableColumn<Loan,String> penalty_amount;
    @FXML public TableColumn<Loan,Integer> penalty_duration;
    @FXML public TableColumn<Loan,String> penalty_total_pay;
    @FXML public TableColumn<Loan,String> penalty_per_month;
    @FXML public TableColumn<Loan,String> penalty_due;
    @FXML public TableColumn<Loan,String> penalty_last_paymonth;
    @FXML public TableColumn<Loan,String> penalty_amount_paid;
    @FXML public TableColumn<Loan,String> penalty_last_pay;
    @FXML public TableColumn<Loan,String> penalty_amount_rem;
    @FXML public TableColumn<Loan,String> penalty_status;
    @FXML public TableColumn<Loan,String> penalty_action;
    @FXML public Button penalty_sms_all;
    @FXML public Button penalty_edit_sms;

    @FXML public TableView<Loan> due_table;
    @FXML public TableColumn<Loan,String> due_l_no;
    @FXML public TableColumn<Loan,String> due_borrower;
    @FXML public TableColumn<Loan,String> due_date;
    @FXML public TableColumn<Loan,Double> due_interest;
    @FXML public TableColumn<Loan,String> due_amount;
    @FXML public TableColumn<Loan,Integer> due_duration;
    @FXML public TableColumn<Loan,String> due_total_pay;
    @FXML public TableColumn<Loan,String> due_per_month;
    @FXML public TableColumn<Loan,String> due_due;
    @FXML public TableColumn<Loan,String> due_last_paymonth;
    @FXML public TableColumn<Loan,String> due_amount_paid;
    @FXML public TableColumn<Loan,String> due_last_pay;
    @FXML public TableColumn<Loan,String> due_amount_rem;
    @FXML public TableColumn<Loan,String> due_status;
    @FXML public TableColumn<Loan,String> due_action;
    @FXML public TextField searchLoanTxt;

    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDue();
        initializeOneMonthLate();
        initializeTwoMonthLate();
        initializePenaltyLate();
        one_sms_all.setOnAction(this);
        one_edit_sms.setOnAction(this);
        two_sms_all.setOnAction(this);
        two_edit_sms.setOnAction(this);
        penalty_sms_all.setOnAction(this);
        penalty_edit_sms.setOnAction(this);
    }

    public void initializeOneMonthLate(){
        one_l_no.setCellValueFactory(new PropertyValueFactory<>("l_no"));
        one_borrower.setCellValueFactory(new PropertyValueFactory<>("l_borrower"));
        one_date.setCellValueFactory(new PropertyValueFactory<>("l_date"));
        one_interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        one_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        one_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        one_total_pay.setCellValueFactory(new PropertyValueFactory<>("total_pay"));
        one_per_month.setCellValueFactory(new PropertyValueFactory<>("per_month"));
        one_due.setCellValueFactory(new PropertyValueFactory<>("due"));
        one_last_paymonth.setCellValueFactory(new PropertyValueFactory<>("last_paymonth"));
        one_amount_paid.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
        one_last_pay.setCellValueFactory(new PropertyValueFactory<>("last_pay"));
        one_amount_rem.setCellValueFactory(new PropertyValueFactory<>("amount_rem"));
        one_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        one_action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        setUpOneMonthLate();

        DatabaseHandler db = new DatabaseHandler();
        one_month_table.setItems(null);
        one_month_table.setItems(db.loadOneMonthLateLoans());
    }

    public void initializeTwoMonthLate(){
        two_l_no.setCellValueFactory(new PropertyValueFactory<>("l_no"));
        two_borrower.setCellValueFactory(new PropertyValueFactory<>("l_borrower"));
        two_date.setCellValueFactory(new PropertyValueFactory<>("l_date"));
        two_interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        two_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        two_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        two_total_pay.setCellValueFactory(new PropertyValueFactory<>("total_pay"));
        two_per_month.setCellValueFactory(new PropertyValueFactory<>("per_month"));
        two_due.setCellValueFactory(new PropertyValueFactory<>("due"));
        two_last_paymonth.setCellValueFactory(new PropertyValueFactory<>("last_paymonth"));
        two_amount_paid.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
        two_last_pay.setCellValueFactory(new PropertyValueFactory<>("last_pay"));
        two_amount_rem.setCellValueFactory(new PropertyValueFactory<>("amount_rem"));
        two_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        two_action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        setUpTwoMonthLate();

        DatabaseHandler db = new DatabaseHandler();
        two_month_table.setItems(null);
        two_month_table.setItems(db.loadTwoMonthLateLoans());
    }

    public void initializePenaltyLate(){
        penalty_l_no.setCellValueFactory(new PropertyValueFactory<>("l_no"));
        penalty_borrower.setCellValueFactory(new PropertyValueFactory<>("l_borrower"));
        penalty_date.setCellValueFactory(new PropertyValueFactory<>("l_date"));
        penalty_interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        penalty_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        penalty_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        penalty_total_pay.setCellValueFactory(new PropertyValueFactory<>("total_pay"));
        penalty_per_month.setCellValueFactory(new PropertyValueFactory<>("per_month"));
        penalty_due.setCellValueFactory(new PropertyValueFactory<>("due"));
        penalty_last_paymonth.setCellValueFactory(new PropertyValueFactory<>("last_paymonth"));
        penalty_amount_paid.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
        penalty_last_pay.setCellValueFactory(new PropertyValueFactory<>("last_pay"));
        penalty_amount_rem.setCellValueFactory(new PropertyValueFactory<>("amount_rem"));
        penalty_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        penalty_action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        setUpPenaltyLate();

        DatabaseHandler db = new DatabaseHandler();
        penalty_table.setItems(null);
        penalty_table.setItems(db.loadPenaltyLateLoans());
    }

    public void initializeDue(){
        due_l_no.setCellValueFactory(new PropertyValueFactory<>("l_no"));
        due_borrower.setCellValueFactory(new PropertyValueFactory<>("l_borrower"));
        due_date.setCellValueFactory(new PropertyValueFactory<>("l_date"));
        due_interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        due_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        due_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        due_total_pay.setCellValueFactory(new PropertyValueFactory<>("total_pay"));
        due_per_month.setCellValueFactory(new PropertyValueFactory<>("per_month"));
        due_due.setCellValueFactory(new PropertyValueFactory<>("due"));
        due_last_paymonth.setCellValueFactory(new PropertyValueFactory<>("last_paymonth"));
        due_amount_paid.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
        due_last_pay.setCellValueFactory(new PropertyValueFactory<>("last_pay"));
        due_amount_rem.setCellValueFactory(new PropertyValueFactory<>("amount_rem"));
        due_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        due_action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        setUpDue();

        DatabaseHandler db = new DatabaseHandler();
        due_table.setItems(null);
        due_table.setItems(db.loadDueLoans());
    }

    public void repaymentForm(String loanId,String name){
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("repayment_form.fxml"));
            stage.setTitle("MikopoApp");
            Scene repaymentScene = new Scene(fxmlLoader.load(),600,350);
            stage.setScene(repaymentScene);
            RepaymentFormController repaymentFormController = fxmlLoader.<RepaymentFormController>getController();
            repaymentFormController.getLoanDetails(loanId,name);
            repaymentFormController.getUserDetails(userObject);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setUpDue(){
        Callback<TableColumn<Loan,String>, TableCell<Loan,String>> cellFactory =
                new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
                    @Override
                    public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                        final TableCell<Loan,String> cell = new TableCell<Loan,String>(){
                            final Button payBtn = new Button("Pay");
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    payBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            // System.out.println(loan.getL_borrower_phone());
                                            repaymentForm(loan.getL_no(),loan.getL_borrower());
                                        }
                                    });
                                    HBox hBox = new HBox();
                                    hBox.getChildren().addAll(payBtn);
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
        due_action.setCellFactory(cellFactory);

        due_l_no.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

        due_status.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {

            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> p) {


                return new TableCell<Loan, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setAlignment(Pos.CENTER);
                            if(item.equals("done")) {
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

        due_borrower.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

    public void setUpOneMonthLate(){
        Callback<TableColumn<Loan,String>, TableCell<Loan,String>> cellFactory =
                new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
                    @Override
                    public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                        final TableCell<Loan,String> cell = new TableCell<Loan,String>(){
                            final Button smsBtn = new Button("SMS");
                            final Button payBtn = new Button("Pay");
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(empty){
                                    //URL url = @icons/view_account.png;
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    smsBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            System.out.println(loan.getL_borrower());
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("Message Status");
                                            alert.setHeaderText(null);
                                            if(SendSMS.sendSms(loan.getL_borrower_phone(),1)){
                                                alert.setContentText("Message has successful been sent");
                                                try {
                                                    Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+
                                                            "id: "+ userObject.get("id")+" sent a message to "+loan.getL_borrower()+" of Loan: "+loan.getL_no());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {
                                                alert.setContentText("Message not sent");
                                            }
                                            alert.showAndWait();
                                        }
                                    });
                                    payBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            // System.out.println(loan.getL_borrower_phone());
                                            repaymentForm(loan.getL_no(),loan.getL_borrower());
                                        }
                                    });
                                    HBox hBox = new HBox();
                                    hBox.getChildren().addAll(smsBtn,payBtn);
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
        one_action.setCellFactory(cellFactory);

        one_l_no.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

        one_borrower.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

        one_status.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {

            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> p) {


                return new TableCell<Loan, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setAlignment(Pos.CENTER);
                            if(item.equals("done")) {
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
    }

    public void setUpTwoMonthLate(){
        Callback<TableColumn<Loan,String>, TableCell<Loan,String>> cellFactory =
                new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
                    @Override
                    public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                        final TableCell<Loan,String> cell = new TableCell<Loan,String>(){
                            final Button smsBtn = new Button("SMS");
                            final Button payBtn = new Button("Pay");
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(empty){
                                    //URL url = @icons/view_account.png;
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    smsBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            System.out.println(loan.getL_borrower());
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("Message Status");
                                            alert.setHeaderText(null);
                                            if(SendSMS.sendSms(loan.getL_borrower_phone(),2)){
                                                alert.setContentText("Message has successful been sent");
                                                try {
                                                    Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+
                                                            "id: "+ userObject.get("id")+" sent a message to "+loan.getL_borrower()+" of Loan: "+loan.getL_no());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {
                                                alert.setContentText("Message not sent");
                                            }
                                            alert.showAndWait();
                                        }
                                    });
                                    payBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            // System.out.println(loan.getL_borrower_phone());
                                            repaymentForm(loan.getL_no(),loan.getL_borrower());
                                        }
                                    });
                                    HBox hBox = new HBox();
                                    hBox.getChildren().addAll(smsBtn,payBtn);
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
        two_action.setCellFactory(cellFactory);

        two_l_no.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

        two_status.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {

            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> p) {


                return new TableCell<Loan, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setAlignment(Pos.CENTER);
                            if(item.equals("done")) {
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

        two_borrower.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

    public void setUpPenaltyLate(){
        Callback<TableColumn<Loan,String>, TableCell<Loan,String>> cellFactory =
                new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
                    @Override
                    public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                        final TableCell<Loan,String> cell = new TableCell<Loan,String>(){
                            final Button smsBtn = new Button("SMS");
                            final Button payBtn = new Button("Pay");
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(empty){
                                    //URL url = @icons/view_account.png;
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    smsBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            System.out.println(loan.getL_borrower());
                                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("Message Status");
                                            alert.setHeaderText(null);
                                            if(SendSMS.sendSms(loan.getL_borrower_phone(),3)){
                                                alert.setContentText("Message has successful been sent");
                                                try {
                                                    Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+
                                                            "id: "+ userObject.get("id")+" sent a message to "+loan.getL_borrower()+" of Loan: "+loan.getL_no());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }else {
                                                alert.setContentText("Message not sent");
                                            }
                                            alert.showAndWait();
                                        }
                                    });
                                    payBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Loan loan = getTableView().getItems().get(getIndex());
                                            // System.out.println(loan.getL_borrower_phone());
                                            repaymentForm(loan.getL_no(),loan.getL_borrower());
                                        }
                                    });
                                    HBox hBox = new HBox();
                                    hBox.getChildren().addAll(smsBtn,payBtn);
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
        penalty_action.setCellFactory(cellFactory);

        penalty_l_no.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

        penalty_status.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {

            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> p) {


                return new TableCell<Loan, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            this.setAlignment(Pos.CENTER);
                            if(item.equals("done")) {
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

        penalty_borrower.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                return new TableCell<Loan, String>() {

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

    public void getUserDetails(JSONObject jsonObject){
        userObject = jsonObject;
    }

    public void searchLoan(){
        int loan_id = Integer.parseInt(searchLoanTxt.getText());
        setUpDue();
        DatabaseHandler db = new DatabaseHandler();
        due_table.setItems(null);
        due_table.setItems(db.searchLoan(loan_id));
    }

    public void refreshDue(){
        initializeDue();
    }

    @Override
      public void handle(ActionEvent event) {
        if(event.getSource()==one_edit_sms){
            editSms(1);
        }else if(event.getSource()==one_sms_all){
            List<String> phone_numbers = new ArrayList<>();
            for (Loan loan : one_month_table.getItems()) {
                phone_numbers.add(loan.getL_borrower_phone());
            }
            SendSMSMany.sendSms(phone_numbers,1);
        }else if(event.getSource()==two_edit_sms){
            editSms(2);
        }else if(event.getSource()==two_sms_all){
            List<String> phone_numbers = new ArrayList<>();
            for (Loan loan : two_month_table.getItems()) {
                phone_numbers.add(loan.getL_borrower_phone());
            }
            SendSMSMany.sendSms(phone_numbers,2);
        }else if(event.getSource()==penalty_edit_sms){
            editSms(3);
        }else if(event.getSource()==penalty_sms_all){
            List<String> phone_numbers = new ArrayList<>();
            for (Loan loan : penalty_table.getItems()) {
                phone_numbers.add(loan.getL_borrower_phone());
            }
            SendSMSMany.sendSms(phone_numbers,3);
        }
    }

    public void editSms(int id){
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sms_edit.fxml"));
            stage.setTitle("MikopoApp");
            Scene repaymentScene = new Scene(fxmlLoader.load(),430,285);
            stage.setScene(repaymentScene);
            SmsEditController smsEditController = fxmlLoader.<SmsEditController>getController();
            smsEditController.getId(id);
            smsEditController.getUserDetails(userObject);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
