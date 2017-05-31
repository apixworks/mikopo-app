package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Apix on 03/05/2017.
 */
public class ViewCustomersLoansController implements Initializable {

    @FXML public ChoiceBox searchChoice;
    @FXML public TextField searchTxt;

    @FXML public TableView<Customer> customer_table;
    @FXML public TableColumn<Customer,String> c_no;
    @FXML public TableColumn<Customer,String> name;
    @FXML public TableColumn<Customer,String> phone;
    @FXML public TableColumn<Customer,String> email;
    @FXML public TableColumn<Customer,String> bank;
    @FXML public TableColumn<Customer,String> account_no;
    @FXML public TableColumn<Customer,String> company_name;
    @FXML public TableColumn<Customer,String> company_phone;
    @FXML public TableColumn<Customer,String> checksum;
    @FXML public TableColumn<Customer,String> action;

    @FXML public TableView<Loan> loan_table;
    @FXML public TableColumn<Loan,String> l_no;
    @FXML public TableColumn<Loan,String> l_borrower;
    @FXML public TableColumn<Loan,String> l_date;
    @FXML public TableColumn<Loan,Double> interest;
    @FXML public TableColumn<Loan,Double> amount;
    @FXML public TableColumn<Loan,Integer> duration;
    @FXML public TableColumn<Loan,Double> total_pay;
    @FXML public TableColumn<Loan,Double> per_month;
    @FXML public TableColumn<Loan,String> due;
    @FXML public TableColumn<Loan,String> last_paymonth;
    @FXML public TableColumn<Loan,Double> amount_paid;
    @FXML public TableColumn<Loan,Double> last_pay;
    @FXML public TableColumn<Loan,Double> amount_rem;
    @FXML public TableColumn<Loan,String> status;
    @FXML public TableColumn<Loan,String> l_action;

    JSONObject userObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> searchOptions = FXCollections.observableArrayList("Search For:","Search Loans","Search Customers");
        searchChoice.setValue("Search For:");
        searchChoice.setItems(searchOptions);

        initializeViewCustomers();
        initializeViewLoans();
    }

    public void initializeViewCustomers(){
        c_no.setCellValueFactory(new PropertyValueFactory<>("c_no"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        bank.setCellValueFactory(new PropertyValueFactory<>("bank"));
        account_no.setCellValueFactory(new PropertyValueFactory<>("account_no"));
        company_name.setCellValueFactory(new PropertyValueFactory<>("company_name"));
        company_phone.setCellValueFactory(new PropertyValueFactory<>("company_phone"));
        checksum.setCellValueFactory(new PropertyValueFactory<>("checksum"));
        action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        setUpCustomer();

        DatabaseHandler db = new DatabaseHandler();
        customer_table.setItems(null);
        customer_table.setItems(db.loadCustomers());
    }

    public void initializeViewLoans(){
        l_no.setCellValueFactory(new PropertyValueFactory<>("l_no"));
        l_borrower.setCellValueFactory(new PropertyValueFactory<>("l_borrower"));
        l_date.setCellValueFactory(new PropertyValueFactory<>("l_date"));
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        total_pay.setCellValueFactory(new PropertyValueFactory<>("total_pay"));
        per_month.setCellValueFactory(new PropertyValueFactory<>("per_month"));
        due.setCellValueFactory(new PropertyValueFactory<>("due"));
        last_paymonth.setCellValueFactory(new PropertyValueFactory<>("last_paymonth"));
        amount_paid.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));
        last_pay.setCellValueFactory(new PropertyValueFactory<>("last_pay"));
        amount_rem.setCellValueFactory(new PropertyValueFactory<>("amount_rem"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        l_action.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        setUpLoan();

        DatabaseHandler db = new DatabaseHandler();
        loan_table.setItems(null);
        loan_table.setItems(db.loadLoans());
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

    public void editCustomerForm(int id){
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_Customer.fxml"));
            stage.setTitle("MikopoApp");
            Scene editCustomerScene = new Scene(fxmlLoader.load(),1100,600);
            stage.setScene(editCustomerScene);
            EditCustomerController editCustomerController = fxmlLoader.<EditCustomerController>getController();
            editCustomerController.getId(id);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void search(){
        int id = Integer.parseInt(searchTxt.getText());
        if(searchChoice.getValue().toString().equals("Search Loans")){
            setUpLoan();
            DatabaseHandler db = new DatabaseHandler();
            loan_table.setItems(null);
            loan_table.setItems(db.searchLoan(id));
        }else if (searchChoice.getValue().toString().equals("Search Customers")){
            setUpCustomer();
            DatabaseHandler db = new DatabaseHandler();
            customer_table.setItems(null);
            customer_table.setItems(db.searchCustomers(id));
        }else if(searchChoice.getValue().toString().equals("Search For:")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Please choice what to search for:");
            alert.showAndWait();
        }
    }

    public void refresh(){
        initializeViewLoans();
        initializeViewCustomers();
    }

    public void setUpLoan(){
        Callback<TableColumn<Loan,String>, TableCell<Loan,String>> cellFactory =
                new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
                    @Override
                    public TableCell<Loan, String> call(TableColumn<Loan, String> param) {
                        final TableCell<Loan,String> cell = new TableCell<Loan,String>(){
                            final Button payBtn = new Button("Pay");
                            final Button editBtn = new Button("Edit");
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(empty){
                                    //URL url = @icons/view_account.png;
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
                                    hBox.getChildren().addAll(payBtn, editBtn);
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
        l_action.setCellFactory(cellFactory);

        status.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {

            @Override
            public TableCell<Loan, String> call(TableColumn<Loan, String> p) {


                return new TableCell<Loan, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
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

        l_no.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
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

        l_borrower.setCellFactory(new Callback<TableColumn<Loan, String>, TableCell<Loan, String>>() {
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

    public void setUpCustomer(){
        Callback<TableColumn<Customer,String>, TableCell<Customer,String>> cellFactory =
                new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
                    @Override
                    public TableCell<Customer, String> call(TableColumn<Customer, String> param) {
                        final TableCell<Customer,String> cell = new TableCell<Customer,String>(){
                            final Button editBtn = new Button();
                            final Button deleteBtn = new Button();
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if(empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    Image accEdit = new Image(getClass().getResourceAsStream("icons/account-edit.png"),18, 18, false, false);
                                    editBtn.setGraphic(new ImageView(accEdit));
                                    editBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Customer customer = getTableView().getItems().get(getIndex());
                                            editCustomerForm(Integer.parseInt(customer.getC_no().substring(5)));
                                        }
                                    });

                                    Image accDelete = new Image(getClass().getResourceAsStream("icons/account-view.png"),18, 18, false, false);
                                    deleteBtn.setGraphic(new ImageView(accDelete));
                                    deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            Customer customer = getTableView().getItems().get(getIndex());
                                            System.out.println(customer.getName());
                                        }
                                    });

                                    HBox hBox = new HBox();
                                    hBox.getChildren().addAll(editBtn,deleteBtn);
                                    hBox.setAlignment(Pos.CENTER);
                                    hBox.setSpacing(5);
                                    setGraphic(hBox);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        action.setCellFactory(cellFactory);

        c_no.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
            @Override
            public TableCell<Customer, String> call(TableColumn<Customer, String> param) {
                return new TableCell<Customer, String>() {

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

        name.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
            @Override
            public TableCell<Customer, String> call(TableColumn<Customer, String> param) {
                return new TableCell<Customer, String>() {

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

        email.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
            @Override
            public TableCell<Customer, String> call(TableColumn<Customer, String> param) {
                return new TableCell<Customer, String>() {

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

        company_name.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
            @Override
            public TableCell<Customer, String> call(TableColumn<Customer, String> param) {
                return new TableCell<Customer, String>() {

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
}
