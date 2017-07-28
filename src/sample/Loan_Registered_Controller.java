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
 * Created by LUKATONI on 07/14/2017.
 */
public class Loan_Registered_Controller implements Initializable {


    //customerRegisteredStatistics.............................
    @FXML
    private Tab LoansRegisteredStatistics;
    @FXML
    final CategoryAxis category_Aix=new CategoryAxis();
    @FXML
    final NumberAxis number_Ax=new NumberAxis();
    @FXML
    final BarChart<String,Number> LoansRegisteredChart=new BarChart<String, Number>(category_Aix,number_Ax);
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




    //
    public void LoansRegisteredStatisticsReport(){
        try{
            LoansRegisteredChart.setTitle("Loans Registered Report -Monthly");
            category_Aix.setLabel("Month");
            number_Ax.setLabel("Number Of Loans");
            XYChart.Series series3=new XYChart.Series();
            //series3.setName("Number Of Loans");
//            Legend legend=(Legend) LoansRegisteredChart.lookup(".chart-legend");
//            legend.setDisable(true);
//            legend.setVisible(false);
//            legend.setStyle("-fx-legend-visible: false");
            conn=connectDb();
            String select="select count(loan_id) as total,MONTHNAME(date_of_loan) AS loandate,YEAR (date_of_loan) AS loanyear from loans Group by Month(date_of_loan),YEAR(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc ";
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
                n.setStyle("-fx-bar-fill: #af755b;");
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
//       LoansRegisteredChart.setOnMouseClicked(EventHandler->{
//            //System.out.print("heloooooooooooooooooooooooo");
//           try {
//               stage=(Stage)customer_Stackpane.getScene().getWindow();
//               PrintWork s=new PrintWork();
//               s.printImage(LoansRegisteredChart);
//               //LoansRegisteredStatistics.setContent(LoansRegisteredChart);
//
//
//           }catch (Exception ef){
//               ef.printStackTrace();
//           }finally {
//               //LoansRegisteredStatisticsReport();
//
//           }
//
//        });
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoansRegisteredStatisticsReport();
    }
    @FXML
    public TextField searchyear;
    //
    public void LoansRegisteredStatisticsReport(String yr){
        try{
            LoansRegisteredChart.setTitle("Loans Registered Report -Monthly");
            category_Aix.setLabel("Month");
            number_Ax.setLabel("Number Of Loans");
            XYChart.Series series3=new XYChart.Series();
            //series3.setName("Number Of Loans");
//            Legend legend=(Legend) LoansRegisteredChart.lookup(".chart-legend");
//            legend.setDisable(true);
//            legend.setVisible(false);
//            legend.setStyle("-fx-legend-visible: false");
            conn=connectDb();
            String select="select count(loan_id) as total,MONTHNAME(date_of_loan) AS loandate,YEAR (date_of_loan) AS loanyear from loans WHERE YEAR (date_of_loan)='"+yr+"' Group by Month(date_of_loan),YEAR(date_of_loan) ORDER BY YEAR(date_of_loan) asc,MONTH (date_of_loan) asc ";
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
                n.setStyle("-fx-bar-fill: #af755b;");
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
//       LoansRegisteredChart.setOnMouseClicked(EventHandler->{
//            //System.out.print("heloooooooooooooooooooooooo");
//           try {
//               stage=(Stage)customer_Stackpane.getScene().getWindow();
//               PrintWork s=new PrintWork();
//               s.printImage(LoansRegisteredChart);
//               //LoansRegisteredStatistics.setContent(LoansRegisteredChart);
//
//
//           }catch (Exception ef){
//               ef.printStackTrace();
//           }finally {
//               //LoansRegisteredStatisticsReport();
//
//           }
//
//        });
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

    public void Search_year(ActionEvent actionEvent) {
        String yr=searchyear.getText();
        LoansRegisteredChart.getData().clear();
        LoansRegisteredStatisticsReport(yr);

    }
}
