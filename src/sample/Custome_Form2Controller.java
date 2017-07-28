package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by LUKATONI on 07/15/2017.
 */
public class Custome_Form2Controller implements Initializable {

    @FXML
    public StackPane customer_Stack;
    @FXML
    public Pane CustomerPane;
    Stage stage;
    //Database connection......................
    private Connection conn;
    private Statement st;
    @FXML
    public Label fullname, amountBorrowed, checkNo, companyName, phone, duration, perMonth, dt_of_loan,fullname1,amount_return;

    @FXML
    private TextField searchTxt;
    @FXML
    public Label mdhamini_checkNo,mdhamini_company,mdhamini_Name,dhamana_ya_mkopo,customerId,amountBorrowed1;
    //database connection

    int cid,lid;
    private Connection connectDb() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/mikopo_app";
            String user = "root";
            String password = "";
            Connection conni = DriverManager.getConnection(url, user, password);
            return conni;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Customer_Data();
    }

    public void Customer_Data(int l_id) {
        try {
            conn = connectDb();
            ResultSet rs;
            st = conn.createStatement();
            String selectId="select loans.borrower_id,loan_id from loans Where loans.loan_id='"+l_id+"' ORDER BY loan_id ASC  ";
            rs=st.executeQuery(selectId);
            if (rs.next()){
                cid=rs.getInt("loans.borrower_id");
                lid=rs.getInt("loan_id");
                System.out.println("ID is :"+cid);
            }
            String select = "SELECT customers.id,customers.f_name,customers.m_name,customers.l_name,customers.phone,loans.dhamana_type,loans.property_name,loans.member_id,loans.date_of_loan,customers.company_name,customers.checksum,loans.amount_borrowed,loans.duration,loans.total_payment,loans.amount_per_month from loans,customers WHERE loans.borrower_id=customers.id AND loans.loan_id='"+lid+"' AND customers.id='"+cid+"' ";

           rs = st.executeQuery(select);
            String memberId="";
            if (rs.next()) {
                memberId=rs.getString("loans.member_id");
                customerId.setText("MJ/C/"+rs.getString("customers.id"));
                String fulln=rs.getString("customers.f_name")+" "+rs.getString("customers.m_name")+" "+rs.getString("customers.l_name");
                //System.out.println(fulln.toUpperCase());
                fullname.setText(fulln.toUpperCase());
                checkNo.setText(rs.getString("customers.checksum"));
                phone.setText(rs.getString("customers.phone"));
                companyName.setText(rs.getString("customers.company_name"));
                amountBorrowed.setText(String.format("%,.0f", rs.getDouble("loans.amount_borrowed")));
                amount_return.setText(String.format("%,.0f", rs.getDouble("loans.total_payment")));
                perMonth.setText(String.format("%,.0f",rs.getDouble("amount_per_month")));
                duration.setText(rs.getString("duration"));
                fullname1.setText(fulln.toUpperCase());
                String dhamana_type = rs.getString("loans.dhamana_type");
                if(dhamana_type.equals("Person")){
                    dhamana_ya_mkopo.setText("Mmoja wa Mwanachama");
                }else if(dhamana_type.equals("Property")){
                    dhamana_ya_mkopo.setText(rs.getString("loans.property_name"));
                }else if(dhamana_type.equals("Person&Property")){
                    dhamana_ya_mkopo.setText("Mmoja wa Mwanachama na "+rs.getString("loans.property_name"));
                }
               //apix    totalpayment.setText(rs.getString("total_payment"));
                dt_of_loan.setText(rs.getString("loans.date_of_loan"));
                amountBorrowed1.setText(String.format("%,.0f", rs.getDouble("loans.total_payment")));
            }
            String selectMdhamini="select customers.id,customers.f_name,customers.m_name,customers.l_name,customers.company_name,customers.checksum from customers where customers.id='"+memberId+"'";
            //System.out.println(memberId);
            rs=st.executeQuery(selectMdhamini);
            if(rs.next()){
                mdhamini_checkNo.setText(rs.getString("customers.checksum"));
                mdhamini_company.setText(rs.getString("customers.company_name"));
                mdhamini_Name.setText(rs.getString("customers.f_name").toUpperCase()+" "+rs.getString("customers.m_name").toUpperCase()+" "+rs.getString("customers.l_name").toUpperCase());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void PrintForm(ActionEvent actionEvent) {
        try {
            stage = (Stage) customer_Stack.getScene().getWindow();
            Printer printer1 = Printer.getDefaultPrinter();
            PageLayout pageLayout = printer1.getDefaultPageLayout();
            pageLayout=printer1.createPageLayout(javafx.print.Paper.A4, PageOrientation.PORTRAIT,Printer.MarginType.EQUAL);
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            printerJob.printPage(pageLayout, customer_Stack);
            printerJob.endJob();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void Search_Customer(ActionEvent actionEvent) {

        if(searchTxt.getText().isEmpty() || searchTxt.getText().equals(0)) {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("");
            alert.setContentText("Please Fill the Loan ID !!!!!!!!");
            alert.showAndWait();

        }else{
            int loan_id=Integer.parseInt(searchTxt.getText());
            Customer_Data(loan_id);
        }




    }
}
