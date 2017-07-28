package sample;

import com.sun.javafx.charts.Legend;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by LUKATONI on 07/12/2017.
 */
public class LoanReleaseController implements Initializable {

    @FXML
    private Tab AmountStatistics;
    Stage stage;
    @FXML
    public TextField searchyear;
    @FXML
    private StackPane customer_Stack;
    //Borrowed amount chart...............................
    @FXML
    final CategoryAxis category_Axis=new CategoryAxis();
    @FXML
    final NumberAxis number_Axis=new NumberAxis();
    @FXML
    final BarChart<String,Number> borrowerChart=new BarChart<String, Number>(category_Axis,number_Axis);


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
        BorrowStatistics();
    }

    //BorrowedStatistics..........................................
    public void BorrowStatistics(){

        try {
            borrowerChart.setTitle("Loans Released - Monthly");
            category_Axis.setLabel("Month");
            number_Axis.setLabel("Amount ");
            //category_Axis.setStyle("-fx-background-color: darkcyan; -fx-text-fill: #FFFFFF");
            XYChart.Series series=new XYChart.Series();
            //series.setName("Amount Borrowed per Month");
            Legend legend=(Legend) borrowerChart.lookup(".chart-legend");
            legend.setDisable(true);
            legend.setVisible(false);
            legend.setStyle("-fx-legend-visible: false");
            conn=connectDb();
            String sel="select sum(amount_borrowed)as AMount,MONTHNAME(date_of_loan) as months,YEAR(date_of_loan) as year from loans GROUP BY MONTH(date_of_loan),YEAR(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc";
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(sel);
            while(rs.next()) {
                Double amount=rs.getDouble("Amount");
                String month=rs.getString("months");
                String year=rs.getString("year");
                String fulldt=month+"/"+year;
                series.getData().add(new XYChart.Data(fulldt, amount));
            }
            borrowerChart.getData().addAll(series);
            //now you can get the nodes.
            for(Node n:borrowerChart.lookupAll(".default-color0.chart-bar")) {
                n.setStyle("-fx-bar-fill: #554640;");
            }
            for (XYChart.Series<String,Number> serie: borrowerChart.getData()){
                for (XYChart.Data<String, Number> item: serie.getData()){

                    item.getNode().setOnMousePressed((EMouseEvent) -> {
                        /// System.out.println("you clicked "+item.toString()+serie.toString());
                        //Report_In_Table();
                        JOptionPane.showMessageDialog(null,item.toString()+serie.toString());
                    });
                }
            }
            AmountStatistics.setContent(borrowerChart);
//            borrowerChart.setOnMouseClicked(EventHandler->{
//                try {
//                    stage=(Stage)customer_Stackpane.getScene().getWindow();
//                    PrintWork sp=new PrintWork();
//                    sp.printImage(borrowerChart);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                }
//            });
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

    //BorrowedStatistics serach Year..........................................
    public void BorrowStatistics(String yr){

        try {
            borrowerChart.setTitle("Loans Released - Monthly");
            category_Axis.setLabel("Month");
            number_Axis.setLabel("Amount ");
            //category_Axis.setStyle("-fx-background-color: darkcyan; -fx-text-fill: #FFFFFF");
            XYChart.Series series=new XYChart.Series();
            //series.setName("Amount Borrowed per Month");
//            Legend legend=(Legend) borrowerChart.lookup(".chart-legend");
//            legend.setDisable(true);
//            legend.setVisible(false);
//            legend.setStyle("-fx-legend-visible: false");
            conn=connectDb();
            String sel="select sum(amount_borrowed)as AMount,MONTHNAME(date_of_loan) as months,YEAR(date_of_loan) as year from loans WHERE YEAR(date_of_loan)='"+yr+"' GROUP BY MONTH(date_of_loan),YEAR(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc";
            st=conn.createStatement();
            ResultSet rs=st.executeQuery(sel);
            while(rs.next()) {
                Double amount=rs.getDouble("Amount");
                String month=rs.getString("months");
                String year=rs.getString("year");
                String fulldt=month+"/"+year;
                series.getData().add(new XYChart.Data(fulldt, amount));
            }
            borrowerChart.getData().addAll(series);
            //now you can get the nodes.
            for(Node n:borrowerChart.lookupAll(".default-color0.chart-bar")) {
                n.setStyle("-fx-bar-fill: #554640;");
            }
            for (XYChart.Series<String,Number> serie: borrowerChart.getData()){
                for (XYChart.Data<String, Number> item: serie.getData()){

                    item.getNode().setOnMousePressed((EMouseEvent) -> {
                        /// System.out.println("you clicked "+item.toString()+serie.toString());
                        //Report_In_Table();
                        JOptionPane.showMessageDialog(null,item.toString()+serie.toString());
                    });
                }
            }
            AmountStatistics.setContent(borrowerChart);
//            borrowerChart.setOnMouseClicked(EventHandler->{
//                try {
//                    stage=(Stage)customer_Stackpane.getScene().getWindow();
//                    PrintWork sp=new PrintWork();
//                    sp.printImage(borrowerChart);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                }
//            });
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

    public void Search_year(ActionEvent actionEvent) {
        String yr=searchyear.getText();
        borrowerChart.getData().clear();
        BorrowStatistics(yr);

    }
}
