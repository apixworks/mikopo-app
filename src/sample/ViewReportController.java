package sample;

import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
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

    private ObservableList<Transaction_View>data;
    @FXML
    private Button searchBtn;
    @FXML
    private Button repayBtn;
    @FXML
    private TextField searchTxt;
    @FXML
    private StackPane customer_Stackpane;
    @FXML
    private AnchorPane RepayPane;
    Stage stage;
    //Database connection......................
    private Connection conn;
    private Statement st;
    //Parent root;
    @FXML
    private Tab AmountStatistics;
    @FXML
    private Tab RepaymentStatistics;
    //Borrowed amount chart...............................
    @FXML
    final CategoryAxis category_Axis=new CategoryAxis();
    @FXML
    final NumberAxis number_Axis=new NumberAxis();
   @FXML
    final BarChart<String,Number> borrowerChart=new BarChart<String, Number>(category_Axis,number_Axis);
    //Repayment barchart .........................................
    @FXML
    final CategoryAxis catego_Axis=new CategoryAxis();
    @FXML
    final NumberAxis numb_Axis=new NumberAxis();
    @FXML
    final BarChart<String,Number>RepaymentChart=new BarChart<String, Number>(catego_Axis,numb_Axis);
    //Custome Barchart

    @FXML
    private Tab CustomerStatistics;
    @FXML
    final CategoryAxis cate_Aix=new CategoryAxis();
    @FXML
    final NumberAxis num_Axis=new NumberAxis();
    @FXML
    final BarChart<String,Number>customerChart=new BarChart<String, Number>(cate_Aix,num_Axis);
    //customerRegisteredStatistics.............................
    @FXML
    private Tab LoansRegisteredStatistics;
    @FXML
    final CategoryAxis category_Aix=new CategoryAxis();
    @FXML
    final NumberAxis number_Ax=new NumberAxis();
    @FXML
    final BarChart<String,Number>LoansRegisteredChart=new BarChart<String, Number>(category_Aix,number_Ax);

    //initilizer
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitiTransaction_View();
        BorrowStatistics();
        AmountRepaymentStatistics();
        LoansRegisteredStatisticsReport();
        CustomerStatisticsReport();
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
    //Customer Statistics

    public void CustomerStatisticsReport(){
        try{
            customerChart.setTitle("Customer Registered Report -Monthly");
            cate_Aix.setLabel("Month");
            num_Axis.setLabel("Number Of Customer");
            XYChart.Series series2=new XYChart.Series();
            //series2.setName("Customer registered Per Month");
            customerChart.setStyle("");
            conn=connectDb();
            String select="select count(id) AS usid,MONTHName(reg_date) AS monthna,YEAR(reg_date) AS yearna from customers Group by Month(reg_date) ORDER BY YEAR(reg_date) asc";
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(select);
            while(rs.next()){
                int useid=rs.getInt("usid");
                String month=rs.getString("monthna");
                String year=rs.getString("yearna");
                String fulld=month+"/"+year;
                series2.getData().add(new XYChart.Data(fulld, useid));
            }
            customerChart.getData().addAll(series2);
            CustomerStatistics.setContent(customerChart);

            //now you can get the nodes.
            for(Node n:customerChart.lookupAll(".default-color0.chart-bar")) {
                n.setStyle("-fx-bar-fill: #558C89;");
            }


            Legend legend=(Legend) customerChart.lookup(".chart-legend");
//            legend.setDisable(true);
//            legend.setVisible(false);
//            legend.setStyle("-fx-legend-visible: false");
            Legend.LegendItem li1=new Legend.LegendItem("Customer registered Per Month", new Rectangle(10,4, Color.valueOf("#558C89")));
            legend.getItems().addAll(li1);

            customerChart.setOnMouseClicked(EventHandler->{
                try {
                    stage=(Stage)customer_Stackpane.getScene().getWindow();
                    PrintWork sp=new PrintWork();
                    sp.printImage(customerChart);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                }
            });
            //
            for (XYChart.Series<String,Number> serie: customerChart.getData()){
                for (XYChart.Data<String, Number> item: serie.getData()){
                    item.getNode().setOnMousePressed((EMouseEvent) -> {
                        //System.out.println("you clicked "+item.toString()+serie.toString());
                        JOptionPane.showMessageDialog(null,item.toString());
                    });
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void LoansRegisteredStatisticsReport(){
    try{
        LoansRegisteredChart.setTitle("Loans Registered Report -Monthly");
        category_Aix.setLabel("Month");
        number_Ax.setLabel("Number Of Loans");
        XYChart.Series series3=new XYChart.Series();
        series3.setName("Number Of Loans");
        conn=connectDb();
        String select="select count(loan_id) as total,MONTHNAME(date_of_loan) AS loandate,YEAR (date_of_loan) AS loanyear from loans Group by Month(date_of_loan);";
        st=conn.createStatement();
        ResultSet rs=st.executeQuery(select);
        while(rs.next()){
            int loanid=rs.getInt("total");
            String month=rs.getString("loandate");
            String year=rs.getString("loanyear");
            String fulld=month+"/"+year;
            series3.getData().add(new XYChart.Data(fulld, loanid));
        }

       LoansRegisteredChart.getData().addAll(series3);
        LoansRegisteredChart.setStyle("-fx-arrows-visible: true;");
        //now you can get the nodes.
        for(Node n:LoansRegisteredChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: #D9853B;");
        }
        //
        for (XYChart.Series<String,Number> serie: LoansRegisteredChart.getData()){
            for (XYChart.Data<String, Number> item: serie.getData()){
                item.getNode().setOnMousePressed((EMouseEvent) -> {
                    //System.out.println("you clicked "+item.toString()+serie.toString());
                    JOptionPane.showMessageDialog(null,item.toString());
                });
            }
        }
       LoansRegisteredStatistics.setContent(LoansRegisteredChart);
    }catch (Exception e){
        e.printStackTrace();
    }finally {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

    //Repayment Statistics...........................................
    public void AmountRepaymentStatistics(){

    try {
        RepaymentChart.setTitle("Loans Repayment - Monthly");
        catego_Axis.setLabel("Month");
        //catego_Axis.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px; -fx-font-family: fantasy");
        numb_Axis.setLabel("Amount Repaid ");
        catego_Axis.setTickMarkVisible(true);
        //catego_Axis.setTickLabelFill(Color.CYAN);
        //RepaymentChart.setStyle("fx-bar-fill:green;");
        //category_Axis.setStyle("-fx-background-color: darkcyan; -fx-text-fill: #FFFFFF");
        XYChart.Series series1=new XYChart.Series();
        series1.setName("Amount Repaid per Month");
        conn=connectDb();
        String sel="select sum(amount)as paid,MONTHNAME(date) as lastmonths,YEAR(date) as yearmonth from payments GROUP BY MONTH(date)";
        st=conn.createStatement();
        ResultSet rs=st.executeQuery(sel);
        while(rs.next()) {
            Double amount=rs.getDouble("paid");
            String month=rs.getString("lastmonths");
            String year=rs.getString("yearmonth");
            String fulld=month+"/"+year;
            series1.getData().add(new XYChart.Data(fulld, amount));
        }
        RepaymentChart.getData().addAll(series1);
        //now you can get the nodes.
        for(Node n:RepaymentChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: #74AFAD;");
        }
        for (XYChart.Series<String,Number> serie: RepaymentChart.getData()){
            for (XYChart.Data<String, Number> item: serie.getData()){
                item.getNode().setOnMousePressed((EMouseEvent) -> {
                    //System.out.println("you clicked "+item.toString()+serie.toString());
                    JOptionPane.showMessageDialog(null,item.toString()+serie.toString());
                });
            }
        }
        RepaymentStatistics.setContent(RepaymentChart);
//        series1.getNode().setOnMouseClicked(event -> {
//            RepaymentChart.setStyle("-fx-background-color: darkcyan");
//        });

    } catch (Exception e) {
        e.printStackTrace();
    }finally {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
    //BorrowedStatistics..........................................
    public void BorrowStatistics(){

        try {
            borrowerChart.setTitle("Loans Released - Monthly");
            category_Axis.setLabel("Month");
            number_Axis.setLabel("Amount Released ");
            //category_Axis.setStyle("-fx-background-color: darkcyan; -fx-text-fill: #FFFFFF");
            XYChart.Series series=new XYChart.Series();
            series.setName("Amount Borrowed per Month");
            conn=connectDb();
            String sel="select sum(amount_borrowed)as AMount,MONTHNAME(date_of_loan) as months,YEAR(date_of_loan) as year from loans GROUP BY MONTH(date_of_loan)";
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(sel);
            while(rs.next()) {
            Double amount=rs.getDouble("Amount");
                String month=rs.getString("months");
                String year=rs.getString("year");
                String fulldt=month+"/"+year;
            series.getData().add(new XYChart.Data(fulldt, amount));
            /*series.getData().add(new XYChart.Data("france", 10000));
            series.getData().add(new XYChart.Data("Tanzania", 2601.34));
            series.getData().add(new XYChart.Data("Kenya", 248.82));
            series.getData().add(new XYChart.Data("Uganda", 10));*/
            }
            borrowerChart.getData().addAll(series);
            //now you can get the nodes.
            for(Node n:borrowerChart.lookupAll(".default-color0.chart-bar")) {
                n.setStyle("-fx-bar-fill: #554640;");
            }
            for (XYChart.Series<String,Number> serie: borrowerChart.getData()){
                for (XYChart.Data<String, Number> item: serie.getData()){

                    item.getNode().setOnMousePressed((EMouseEvent) -> {
                        System.out.println("you clicked "+item.toString()+serie.toString());
                        //JOptionPane.showMessageDialog(null,item.toString()+serie.toString());
                    });
                }
            }
            AmountStatistics.setContent(borrowerChart);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void PrintDetails(){
        try {
            stage=(Stage)customer_Stackpane.getScene().getWindow();
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            //printerJob.showPrintDialog(stage);
            printerJob.printPage(Customer_transaction);
            printerJob.endJob();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void InitiTransaction_View()
    {
        String fullname;
        try {
            conn=connectDb();
            //int idd=Search_Customer();
            //AND payments.loan_id="+idd+";
            data= FXCollections.observableArrayList();
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(" select customers.id,payments.loan_id,customers.f_name,customers.l_name ,payments.amount,payments.date from customers,loans,payments\n" +
                    "where customers.id=loans.borrower_id AND loans.loan_id=payments.loan_id");
            while(rs.next()){
                fullname=rs.getString("customers.f_name")+"  "+rs.getString("customers.l_name");
                data.add(new Transaction_View("MJ/C/"+rs.getString("customers.id"),"MJ/L/"+rs.getString("payments.loan_id"),fullname,String.format("%,.0f", rs.getDouble("payments.amount")),rs.getString("payments.date")));

            }

        } catch (Exception ex) {
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

        loan_id.setCellFactory(new Callback<TableColumn<Transaction_View, String>, TableCell<Transaction_View, String>>() {
        @Override
        public TableCell<Transaction_View, String> call(TableColumn<Transaction_View, String> param) {
            return new TableCell<Transaction_View, String>() {

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

        full_name.setCellFactory(new Callback<TableColumn<Transaction_View, String>, TableCell<Transaction_View, String>>() {
            @Override
            public TableCell<Transaction_View, String> call(TableColumn<Transaction_View, String> param) {
                return new TableCell<Transaction_View, String>() {

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

        //tableuser.setItems(null);
        Customer_transaction.setItems(null);
        Customer_transaction.setItems(data);


    }

    public void  Search_Customer(){
        int id=Integer.parseInt(searchTxt.getText());
        //InitilizeTransaction_View(id);
        userTransactionsReport(id);
    }

    public void InitilizeTransaction_View( int ids)
    {
        try {
            conn=connectDb();
            data= FXCollections.observableArrayList();
           st=conn.createStatement();
            ResultSet rs=st.executeQuery(" select customers.id,payments.loan_id,customers.f_name,customers.l_name ,payments.amount,payments.date from customers,loans,payments\n" +
                    "where customers.id=loans.borrower_id AND loans.loan_id=payments.loan_id AND payments.loan_id="+ids+"");
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
        //tableuser.setItems(null);
        Customer_transaction.setItems(null);
        Customer_transaction.setItems(data);
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
}