package sample;

import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by LUKATONI on 7/8/2017.
 */
public class ViewReportTable_Controller implements Initializable {
//    @FXML
//    private ChoiceBox PrintChoice;
//Parent root;
@FXML
private Tab AmountStatistics;
    @FXML
    private Tab RepaymentStatistics;
    @FXML
    final CategoryAxis catego_Axis=new CategoryAxis();
    @FXML
    final NumberAxis numb_Axis=new NumberAxis();
    @FXML
    final BarChart<String,Number>RepaymentChart=new BarChart<String, Number>(catego_Axis,numb_Axis);

    @FXML
    public  TextField searchyear;







    Stage stage;
    @FXML
    private StackPane customer_Stack;

    //Database connection......................
    private Connection conn;
    private Statement st;

    //database connection
    private Connection connectDb(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mikopo_app";
            String user = "root";
            String password = "";
            Connection connic= DriverManager.getConnection(url,user,password);
            return connic;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AmountRepaymentStatistics();

    }

    //Repayment Statistics...........................................
    XYChart.Series series1;
    public void AmountRepaymentStatistics(){

        try {
            RepaymentChart.setTitle("Loans Repayment - Monthly");
            catego_Axis.setLabel("Month");
            numb_Axis.setLabel("Amount ");
             series1=new XYChart.Series();
            //series1.setName("Amount Repaid per Month");
            Legend legend=(Legend) RepaymentChart.lookup(".chart-legend");
            legend.setDisable(true);
            legend.setVisible(false);
            legend.setStyle("-fx-legend-visible: false");
            conn=connectDb();
            String sel="select sum(amount)as paid,MONTHNAME(date) as lastmonths,YEAR(date) as yearmonth from payments GROUP BY MONTH(date),YEAR(date) ORDER BY YEAR(date) asc,MONTH (date) asc ";
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
//        RepaymentChart.setOnMouseClicked(EventHandler->{
//            try {
//                stage=(Stage)customer_Stackpane.getScene().getWindow();
//                PrintWork sp=new PrintWork();
//                sp.printImage(RepaymentChart);
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//            }
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

    //Repayment Statistics years search...........................................
    public void AmountRepaymentStatistics(String yrs){

        try {
            RepaymentChart.setTitle("Loans Repayment - Monthly");
            catego_Axis.setLabel("Month");
            numb_Axis.setLabel("Amount ");
            XYChart.Series series3=new XYChart.Series();
            //series1.setName("Amount Repaid per Month");
//            Legend legend=(Legend) RepaymentChart.lookup(".chart-legend");
//            legend.setDisable(true);
//            legend.setVisible(false);
//            legend.setStyle("-fx-legend-visible: false");
            conn=connectDb();
            String sel="select sum(amount)as paid,MONTHNAME(date) as lastmonths,YEAR(date) as yearmonth from payments where YEAR(date)='"+yrs+"' GROUP BY MONTH(date),YEAR(date) ORDER BY YEAR(date) asc,MONTH (date) asc ";
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(sel);
            while(rs.next()) {
                Double amount=rs.getDouble("paid");
                String month=rs.getString("lastmonths");
                String year=rs.getString("yearmonth");
                String fulld=month+"/"+year;
                series3.getData().add(new XYChart.Data(fulld, amount));

            }
            RepaymentChart.getData().addAll(series3);
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

           // RepaymentStatistics.setContent(null);

            RepaymentStatistics.setContent(RepaymentChart);
//        RepaymentChart.setOnMouseClicked(EventHandler->{
//            try {
//                stage=(Stage)customer_Stackpane.getScene().getWindow();
//                PrintWork sp=new PrintWork();
//                sp.printImage(RepaymentChart);
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//            }
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


    public void Search_year() {

        String repay_Year=searchyear.getText();
        //RepaymentChart.getData().removeAll(series1);
        //RepaymentStatistics.setContent(null);
        RepaymentChart.getData().clear();
        AmountRepaymentStatistics(repay_Year);

    }
//    //BorrowedStatistics..........................................
//    public void BorrowStatistics(){
//
//        try {
//            borrowerChart.setTitle("Loans Released - Monthly");
//            category_Axis.setLabel("Month");
//            number_Axis.setLabel("Amount ");
//            //category_Axis.setStyle("-fx-background-color: darkcyan; -fx-text-fill: #FFFFFF");
//            XYChart.Series series=new XYChart.Series();
//            //series.setName("Amount Borrowed per Month");
//            Legend legend=(Legend) borrowerChart.lookup(".chart-legend");
//            legend.setDisable(true);
//            legend.setVisible(false);
//            legend.setStyle("-fx-legend-visible: false");
//            conn=connectDb();
//            String sel="select sum(amount_borrowed)as AMount,MONTHNAME(date_of_loan) as months,YEAR(date_of_loan) as year from loans GROUP BY MONTH(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc";
//            st=conn.createStatement();
//            ResultSet rs=st.executeQuery(sel);
//            while(rs.next()) {
//                Double amount=rs.getDouble("Amount");
//                String month=rs.getString("months");
//                String year=rs.getString("year");
//                String fulldt=month+"/"+year;
//                series.getData().add(new XYChart.Data(fulldt, amount));
//            }
//            borrowerChart.getData().addAll(series);
//            //now you can get the nodes.
//            for(Node n:borrowerChart.lookupAll(".default-color0.chart-bar")) {
//                n.setStyle("-fx-bar-fill: #554640;");
//            }
//            for (XYChart.Series<String,Number> serie: borrowerChart.getData()){
//                for (XYChart.Data<String, Number> item: serie.getData()){
//
//                    item.getNode().setOnMousePressed((EMouseEvent) -> {
//                        /// System.out.println("you clicked "+item.toString()+serie.toString());
//                        //Report_In_Table();
//                        JOptionPane.showMessageDialog(null,item.toString()+serie.toString());
//                    });
//                }
//            }
//            AmountStatistics.setContent(borrowerChart);
////            borrowerChart.setOnMouseClicked(EventHandler->{
////                try {
////                    stage=(Stage)customer_Stackpane.getScene().getWindow();
////                    PrintWork sp=new PrintWork();
////                    sp.printImage(borrowerChart);
////                }catch (Exception e){
////                    e.printStackTrace();
////                }finally {
////                }
////            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                st.close();
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }







}
