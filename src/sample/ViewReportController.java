package sample;

import com.sun.javafx.charts.Legend;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.PrintWork;
import sample.Transaction_View;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by LUKATONI on 5/26/2017.
 */
public class ViewReportController implements Initializable{

    @FXML
    public TableView<Transaction_View> Customer_transaction;
    @FXML public TableColumn<Transaction_View,String> sno;
    @FXML public TableColumn<Transaction_View,String> loan_id;
    @FXML public TableColumn<Transaction_View,String> full_name;
    @FXML public TableColumn<Transaction_View,String> amount;
    @FXML public TableColumn<Transaction_View,String> date_transaction;

    private ObservableList<Transaction_View> data;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField month_Loan_search_Day;
    @FXML
    private TextField searchTxt;
    @FXML
   public StackPane customer_Stackpane;
    @FXML
    private AnchorPane RepayPane;
    Stage stage;
    //Database connection......................
    private Connection conn;
    private Statement st;

    //
    //customer......................................
    @FXML
    public TableView<Customer_Registered> cust_Reg_table;
    @FXML
    public TableColumn<Customer_Registered,String> Cdate;
    @FXML
    public TableColumn<Customer_Registered,String> Cust_Number;
    @FXML
    public TableColumn<Customer_Registered,String> C_No;
    private ObservableList<Customer_Registered> cdata;
    //
    //Loan Registered......................................
    @FXML
    public TableView<Loan_Registered> LoansTable;
    @FXML
    public TableColumn<Loan_Registered,String> Loan_Reg_Date;
    @FXML
    public TableColumn<Loan_Registered,String> loan_total;
    @FXML
    public TableColumn<Loan_Registered,String> LR_No;
    private ObservableList<Loan_Registered> lrdata;

    //loan Released................................
    @FXML
    public TableView<LoanRelease> LoansTable_Day;
    @FXML
    public TableColumn<LoanRelease,String> Loan_Reg_Date_Day;
    @FXML
    public TableColumn<LoanRelease,String> loan_total_Day;
    @FXML
    public TableColumn<LoanRelease,String> LR_No_Day;
    private ObservableList<LoanRelease> lr_Day_data;
    //Loan Repayment
    @FXML
    public TableView<Repayment_Collected> RepayTable_Day;
    @FXML
    public TableColumn<Repayment_Collected,String> repay_Reg_Date_Day;
    @FXML
    public TableColumn<Repayment_Collected,String> repay_total_Day;
    @FXML
    public TableColumn<Repayment_Collected,String> repay_No_Day;
    private ObservableList<Repayment_Collected> repay_Day_data;
    @FXML
    public TextField month_Repay_search_Day;


    //initilizer
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectDb();
        InitiTransaction_View();
        InitiCustomer_Registered();
        InitiLoan_Registered();
        Initi_LoanReleased_Per_Day();
        InitRepayment_Collected();
    }
    //database connection
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

    public void InitiCustomer_Registered() {
        try {
            conn = connectDb();
            cdata = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select count(id) AS usid,MONTHName(reg_date) AS monthna,YEAR(reg_date) AS yearna from customers Group by Month(reg_date),YEAR(reg_date) ORDER BY YEAR(reg_date) asc,MONTH (reg_date) ASC  ");
            while (rs.next()) {
                String fulldt = rs.getString("monthna") + "/" + rs.getString("yearna");
                int s= rs.getRow();
                //System.out.println(fulldt);
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount")));
                cdata.add(new Customer_Registered(fulldt, rs.getString("usid"), s));
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
        C_No.setCellValueFactory(new PropertyValueFactory<>("C_No"));
        Cdate.setCellValueFactory(new PropertyValueFactory<>("Cdate"));
        Cust_Number.setCellValueFactory(new PropertyValueFactory<>("Cust_Number"));
        cust_Reg_table.setItems(null);
        cust_Reg_table.setItems(cdata);
    }

    public void InitiLoan_Registered() {
        try {
            conn = connectDb();
            lrdata = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(" select count(loan_id) as total,MONTHNAME(date_of_loan) AS loandate,YEAR (date_of_loan) AS loanyear from loans Group by Month(date_of_loan),YEAR(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc ");
            while (rs.next()) {
                int lr_No=rs.getRow();
                String fulldt = rs.getString("loandate") + "/" + rs.getString("loanyear");
                //System.out.println(fulldt);
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount")));
                lrdata.add(new Loan_Registered(fulldt, rs.getString("total"), lr_No ));
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
        LR_No.setCellValueFactory(new PropertyValueFactory<>("LR_No"));
        Loan_Reg_Date.setCellValueFactory(new PropertyValueFactory<>("Loan_Reg_Date"));
        loan_total.setCellValueFactory(new PropertyValueFactory<>("loan_total"));
        LoansTable.setItems(null);
        LoansTable.setItems(lrdata);
    }

    public void PrintDetails(){
        try {
            stage=(Stage)customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            double scaleX = pageLayout.getPrintableWidth() / Customer_transaction.getBoundsInParent().getWidth();
            double scaleY = pageLayout.getPrintableHeight() / Customer_transaction.getBoundsInParent().getHeight();
            Customer_transaction.getTransforms().add(new Scale(scaleX, scaleY));
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {

                boolean success = job.printPage(pageLayout,Customer_transaction);

                if (success) {
                    job.endJob();

                    //System.exit(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void InitiTransaction_View()
    {
        try {
            conn=connectDb();
            data= FXCollections.observableArrayList();
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(" select customers.id,payments.loan_id,customers.f_name,customers.l_name ,payments.amount,payments.date from customers,loans,payments\n" +
                    "where customers.id=loans.borrower_id AND loans.loan_id=payments.loan_id");
            while(rs.next()){
                String fullname=rs.getString("customers.f_name")+"  "+rs.getString("customers.l_name");
                data.add(new Transaction_View("MJ/C/"+rs.getString("customers.id"),"MJ/L/"+rs.getString("payments.loan_id"),fullname,rs.getString("payments.amount"),rs.getString("payments.date")));

            }

        } catch (Exception ex) {
            //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            //System.err.println("error :"+ex);
            ex.printStackTrace();
        }
        finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        sno.setCellValueFactory(new PropertyValueFactory<>("sno"));
        loan_id.setCellValueFactory(new PropertyValueFactory<>("loan_no"));
        full_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        date_transaction.setCellValueFactory(new PropertyValueFactory<>("date"));
        Customer_transaction.setItems(null);
        Customer_transaction.setItems(data);
    }

//    
//    public void  Search_Customer(){
//        int id=Integer.parseInt(searchTxt.getText());
//        InitilizeTransaction_View(id);
//    }

//    public void InitilizeTransaction_View( int ids)
//    {
//        try {
//            conn=connectDb();
//            data= FXCollections.observableArrayList();
//           st=conn.createStatement();
//            ResultSet rs=st.executeQuery(" select customers.id,payments.loan_id,customers.f_name,customers.l_name ,payments.amount,payments.date from customers,loans,payments\n" +
//                    "where customers.id=loans.borrower_id AND loans.loan_id=payments.loan_id AND payments.loan_id="+ids+"");
//            while(rs.next()){
//                String fullname=rs.getString("customers.f_name")+"  "+rs.getString("customers.l_name");
//                data.add(new Transaction_View("MJ/C/"+rs.getString("customers.id"),"MJ/L/"+rs.getString("payments.loan_id"),fullname,rs.getString("payments.amount"),rs.getString("payments.date")));
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        finally {
//            try {
//                st.close();
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        sno.setCellValueFactory(new PropertyValueFactory<>("sno"));
//        loan_id.setCellValueFactory(new PropertyValueFactory<>("loan_no"));
//        full_name.setCellValueFactory(new PropertyValueFactory<>("name"));
//        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
//        date_transaction.setCellValueFactory(new PropertyValueFactory<>("date"));
//        //tableuser.setItems(null);
//        Customer_transaction.setItems(null);
//        Customer_transaction.setItems(data);
//    }
    //
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

    @FXML
    private TextField month_Customer_search;
    public void InitiCustomer_Registered( String moc) {
        try {
            conn = connectDb();
            cdata = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select count(id) AS usid,MONTHName(reg_date) AS monthna,YEAR(reg_date) AS yearna from customers WHERE MONTHName(reg_date) LIKE '"+moc+"%' Group by Month(reg_date),YEAR(reg_date) ORDER BY YEAR(reg_date) asc,MONTH (reg_date) ASC  ");
            while (rs.next()) {
                String fulldt = rs.getString("monthna") + "/" + rs.getString("yearna");
                int s= rs.getRow();
                //System.out.println(fulldt);
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount")));
                cdata.add(new Customer_Registered(fulldt, rs.getString("usid"), s));
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
        C_No.setCellValueFactory(new PropertyValueFactory<>("C_No"));
        Cdate.setCellValueFactory(new PropertyValueFactory<>("Cdate"));
        Cust_Number.setCellValueFactory(new PropertyValueFactory<>("Cust_Number"));
        cust_Reg_table.setItems(null);
        cust_Reg_table.setItems(cdata);
    }

    public void SearchCustomer_Month(ActionEvent actionEvent) {
        String mon=month_Customer_search.getText();
            InitiCustomer_Registered(mon);
    }

    public void SearchCustomer_refresh(ActionEvent actionEvent) {
        InitiCustomer_Registered();

    }

    public void ViewReport_Customer(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerRegistered.fxml"));
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

    public void PrintDetails_Customer(ActionEvent actionEvent) {
        try {
            stage = (Stage) customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout, cust_Reg_table);
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public TextField month_Loan_search;

    public void InitiLoan_Registered(String mon) {
        try {
            conn = connectDb();
            lrdata = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(" select count(loan_id) as total,MONTHNAME(date_of_loan) AS loandate,YEAR (date_of_loan) AS loanyear from loans WHERE MONTHNAME(date_of_loan) LIKE '"+mon+"%' OR YEAR (date_of_loan) LIKE '"+mon+"%' Group by Month(date_of_loan),YEAR(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc ");
            while (rs.next()) {
                int lr_No=rs.getRow();
                String fulldt = rs.getString("loandate") + "/" + rs.getString("loanyear");
                //System.out.println(fulldt);
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount")));
                lrdata.add(new Loan_Registered(fulldt, rs.getString("total"), lr_No ));
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
        LR_No.setCellValueFactory(new PropertyValueFactory<>("LR_No"));
        Loan_Reg_Date.setCellValueFactory(new PropertyValueFactory<>("Loan_Reg_Date"));
        loan_total.setCellValueFactory(new PropertyValueFactory<>("loan_total"));
        LoansTable.setItems(null);
        LoansTable.setItems(lrdata);
    }
    public void SearchLoan_Month(ActionEvent actionEvent) {
        String mon=month_Loan_search.getText();
        InitiLoan_Registered(mon);
    }

    public void SearchLoan_refresh(ActionEvent actionEvent) {

        InitiLoan_Registered();
    }

    public void ViewReport_LoanRegistered(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Loan_Registered.fxml"));
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

    public void PrintDetails_LoanRegistered(ActionEvent actionEvent) {
        try {
            stage = (Stage) customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout,LoansTable );
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void userTransactionsReport(int id){
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user_transactions.fxml"));
            stage.setTitle("MikopoApp");
            Scene repaymentScene = new Scene(fxmlLoader.load(),600,700);
            stage.setScene(repaymentScene);
            UserTransactionController userTransactionController = fxmlLoader.<UserTransactionController>getController();
            userTransactionController.setUp(id);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  Search_Customer(){
        int id=Integer.parseInt(searchTxt.getText());
        //InitilizeTransaction_View(id);
        userTransactionsReport(id);
    }

    public void view_Form(ActionEvent actionEvent) {

        //stage for form..............................
        try {
            //setCid(Integer.parseInt(borrower.getText()));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Customer_Form_Hati_Search.fxml"));
            stage.setTitle("MikopoApp");
            stage.setResizable(false);
            //stage.isFullScreen();
            Scene cform = new Scene(fxmlLoader.load(),700,720);
            stage.setScene(cform);
            //ViewReportTable_Controller repaymentFormController = fxmlLoader.<RepaymentFormController>getController();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Initi_LoanReleased_Per_Day(){
        try {
            conn = connectDb();
            lr_Day_data= FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount_borrowed)as AMount,date_of_loan as dtmonths,YEAR(date_of_loan) as dtyears from loans GROUP BY DAYOFMONTH(date_of_loan),MONTH(date_of_loan),YEAR(date_of_loan) ORDER BY MONTH(date_of_loan) ASC ");
            while (rs.next()) {
                int s= rs.getRow();
                String fulldt = rs.getString("dtmonths");
                //System.out.println(fulldt);
                lr_Day_data.add(new LoanRelease(fulldt, rs.getString("AMount"), s));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        LR_No_Day.setCellValueFactory(new PropertyValueFactory<>("Sno"));
        Loan_Reg_Date_Day.setCellValueFactory(new PropertyValueFactory<>("lDate"));
        loan_total_Day.setCellValueFactory(new PropertyValueFactory<>("lamount"));
        LoansTable_Day.setItems(null);
        LoansTable_Day.setItems(lr_Day_data);

    }


    public void Loan_PerDay_refresh(ActionEvent actionEvent) {
        Initi_LoanReleased_Per_Day();
    }

    public void SearchLoan_Month_PerDay(ActionEvent actionEvent) {
        String month=month_Loan_search_Day.getText().toString();
        Initi_LoanReleased_Per_Day(month);

    }


    public void Initi_LoanReleased_Per_Day(String mon){
        try {
            conn = connectDb();
            lr_Day_data= FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount_borrowed)as AMount,date_of_loan as dtmonths,YEAR(date_of_loan) as dtyears from loans WHERE MONTH(date_of_loan) LIKE '"+mon+"%' OR YEAR(date_of_loan) = '"+mon+"' GROUP BY DAYOFMONTH(date_of_loan),MONTH(date_of_loan),YEAR(date_of_loan) ORDER BY MONTH(date_of_loan) ASC ");
            while (rs.next()) {
                int s= rs.getRow();
                String fulldt = rs.getString("dtmonths");
                //System.out.println(fulldt);
                lr_Day_data.add(new LoanRelease(fulldt, rs.getString("AMount"), s));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        LR_No_Day.setCellValueFactory(new PropertyValueFactory<>("Sno"));
        Loan_Reg_Date_Day.setCellValueFactory(new PropertyValueFactory<>("lDate"));
        loan_total_Day.setCellValueFactory(new PropertyValueFactory<>("lamount"));
        LoansTable_Day.setItems(null);
        LoansTable_Day.setItems(lr_Day_data);

    }

    public void PrintDetails_LoanPerDay(ActionEvent actionEvent) {

        try {
            stage = (Stage) customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout,LoansTable_Day );
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void InitRepayment_Collected(){
        try {
            conn = connectDb();
            repay_Day_data = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount)as paid,(date) as lastmonths,YEAR(date) as yearmonth from payments  GROUP BY DAYOFMONTH(date),MONTH(date),YEAR(date) ORDER BY MONTH(date) asc ");
            while (rs.next()) {
                String fulldt = rs.getString("lastmonths");
                // System.out.println(fulldt);
                int s= rs.getRow();
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount"))
                repay_Day_data.add(new Repayment_Collected(fulldt, rs.getString("paid"), s));
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
        repay_No_Day.setCellValueFactory(new PropertyValueFactory<>("R_No"));
        repay_Reg_Date_Day.setCellValueFactory(new PropertyValueFactory<>("Rdate"));
        repay_total_Day.setCellValueFactory(new PropertyValueFactory<>("Ramount"));
        RepayTable_Day.setItems(null);
        RepayTable_Day.setItems(repay_Day_data);
    }

    public void SearchRepay_Month_PerDay(ActionEvent actionEvent) {
        String month=month_Repay_search_Day.getText().toString();
        InitRepayment_Collected(month);

    }

    public void InitRepayment_Collected(String mon){
        try {
            conn = connectDb();
            repay_Day_data = FXCollections.observableArrayList();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount)as paid,(date) as lastmonths,YEAR(date) as yearmonth from payments WHERE MONTH(date) LIKE '"+mon+"%' OR YEAR(date) = '"+mon+"' GROUP BY DAYOFMONTH(date),MONTH(date),YEAR(date) ORDER BY MONTH(date) asc ");
            while (rs.next()) {
                String fulldt = rs.getString("lastmonths");
                // System.out.println(fulldt);
                int s= rs.getRow();
                //datas.add(new LoanRelease(fulldt, rs.getString("AMount"))
                repay_Day_data.add(new Repayment_Collected(fulldt, rs.getString("paid"), s));
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
        repay_No_Day.setCellValueFactory(new PropertyValueFactory<>("R_No"));
        repay_Reg_Date_Day.setCellValueFactory(new PropertyValueFactory<>("Rdate"));
        repay_total_Day.setCellValueFactory(new PropertyValueFactory<>("Ramount"));
        RepayTable_Day.setItems(null);
        RepayTable_Day.setItems(repay_Day_data);
    }

    public void Repay_PerDay_refresh(ActionEvent actionEvent) {

        InitRepayment_Collected();
    }

    public void PrintDetails_RepayPerDay(ActionEvent actionEvent) {
        try {
            stage = (Stage) customer_Stackpane.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout,RepayTable_Day );
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}