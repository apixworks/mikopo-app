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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by LUKATONI on 07/14/2017.
 */
public class CustomerRegisteredController implements Initializable {
    @FXML
    public TextField searchyear ;
    //Custome Barchart

    @FXML
    public Tab CustomerStatistics;
    @FXML
    final CategoryAxis cate_Aix=new CategoryAxis();
    @FXML
    final NumberAxis num_Axis=new NumberAxis();
    @FXML
    final BarChart<String,Number> customerChart=new BarChart<String, Number>(cate_Aix,num_Axis);

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

    //Customer Statistics

    public void CustomerStatisticsReport(){
        try{
            customerChart.setTitle("Customer Registered Report -Monthly");
            cate_Aix.setLabel("Month");
            num_Axis.setLabel("Number Of Customer");
            XYChart.Series series2=new XYChart.Series();
            //series2.setName("Customer registered Per Month");
            //customerChart.setStyle("");
            conn=connectDb();
            String select="select count(id) AS usid,MONTHName(reg_date) AS monthna,YEAR(reg_date) AS yearna from customers Group by Month(reg_date),YEAR(reg_date) ORDER BY YEAR(reg_date) asc,MONTH (reg_date) ASC ";
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
            Legend.LegendItem li1=new Legend.LegendItem("Customer registered Per Month", new Rectangle(10,4, Color.valueOf("#558C89")));
            legend.getItems().addAll(li1);

//            customerChart.setOnMouseClicked(EventHandler->{
//                try {
//                    stage=(Stage)customer_Stackpane.getScene().getWindow();
//                    PrintWork sp=new PrintWork();
//                    sp.printImage(customerChart);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                }
//            });
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

    //Customer Statistics Search year..............................
      public void CustomerStatisticsReport(String yr){
        try{
            customerChart.setTitle("Customer Registered Report -Monthly");
            cate_Aix.setLabel("Month");
            num_Axis.setLabel("Number Of Customer");
            XYChart.Series series2=new XYChart.Series();
            //series2.setName("Customer registered Per Month");
            //customerChart.setStyle("");
            conn=connectDb();
            String select="select count(id) AS usid,MONTHName(reg_date) AS monthna,YEAR(reg_date) AS yearna from customers WHERE YEAR(reg_date)='"+yr+"'  Group by Month(reg_date),YEAR(reg_date) ORDER BY YEAR(reg_date) asc,MONTH (reg_date) ASC ";
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
            Legend.LegendItem li1=new Legend.LegendItem("Customer registered Per Month", new Rectangle(10,4, Color.valueOf("#558C89")));
            legend.getItems().addAll(li1);

//            customerChart.setOnMouseClicked(EventHandler->{
//                try {
//                    stage=(Stage)customer_Stackpane.getScene().getWindow();
//                    PrintWork sp=new PrintWork();
//                    sp.printImage(customerChart);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                }
//            });
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CustomerStatisticsReport();
    }


    public void Search_year(ActionEvent actionEvent) {
        String yr=searchyear.getText();
        customerChart.getData().clear();
        CustomerStatisticsReport(yr);


    }
}
