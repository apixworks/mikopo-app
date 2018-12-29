package sample.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONException;
import org.json.JSONObject;
import sample.*;
import sample.models.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apix on 25/04/2017.
 */
public class DatabaseHandler {

    private static Connection conn;
    private static String url = "jdbc:mysql://localhost:3306/mikopo_app";
    private static String user = "root";
    private static String password = "";
//    private static String url = "jdbc:mysql://egospeltz.org:3306/egospelt_mikopo";
//    private static String user = "egospelt_mikopo";
//    private static String password = "mikopo123";
    int id;

    public static Connection createConn(){
        try{
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public boolean login(String username,String passwd){
        conn = createConn();
        String userSql = "SELECT * FROM users WHERE username=? AND password=?";
        boolean success = false;
        try {
            //Statement st = conn.createStatement();
            //ResultSet rs = st.executeQuery(userSql);
            PreparedStatement ps = conn.prepareStatement(userSql);
            ps.setString(1,username);
            ps.setString(2,passwd);
            ResultSet rs = ps.executeQuery();

            if(rs.first()){
                success = true;
               setId(rs.getInt("id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public JSONObject getUserDetails(int id) throws SQLException {
        conn = createConn();
        String userDetailsSql = "SELECT * FROM users WHERE id=?";
        JSONObject jsonObject = new JSONObject();

        try {
            PreparedStatement ps = conn.prepareStatement(userDetailsSql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                jsonObject.put("id",rs.getInt("id"));
                jsonObject.put("fname",rs.getString("fname"));
                jsonObject.put("lname",rs.getString("lname"));
                jsonObject.put("password",rs.getString("password"));
                jsonObject.put("username",rs.getString("username"));
                jsonObject.put("status",rs.getString("status"));
                jsonObject.put("role",rs.getString("role"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        } catch (JSONException j) {
            j.printStackTrace();
        }finally {
            conn.close();
        }
        return jsonObject;
    }

    public Result registerCustomer(String fname, String lname, String mname, String gender, LocalDate dob, String phone, String email,
                                   String postal, String prof_photo, String bank, String account_no, String company_name,
                                   String company_phone, String company_location, String checksum,LocalDate reg_date){
        conn = createConn();
        String regCustomerSql = "INSERT INTO customers (f_name,l_name,m_name,gender,dob,phone,email,postal," +
                "prof_photo,bank,account_no,company_name,company_phone,company_location,checksum,reg_date)"
                +"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String lastCustomerSql = "SELECT * FROM customers";

        Result result = new Result();
        result.setSuccess(false);
        try{
            PreparedStatement ps = conn.prepareStatement(regCustomerSql);
            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(3,mname);
            ps.setString(4,gender);
            ps.setDate(5,java.sql.Date.valueOf(dob));
            ps.setString(6,phone);
            ps.setString(7,email);
            ps.setString(8,postal);
            ps.setString(9,prof_photo);
            ps.setString(10,bank);
            ps.setString(11,account_no);
            ps.setString(12,company_name);
            ps.setString(13,company_phone);
            ps.setString(14,company_location);
            ps.setString(15,checksum);
            ps.setDate(16,java.sql.Date.valueOf(reg_date));

            ps.execute();
            result.setSuccess(true);

            PreparedStatement pb = conn.prepareStatement(lastCustomerSql);
            ResultSet rs = pb.executeQuery();
            while (rs.next()){
                if(rs.isLast()){
                    result.setId(rs.getInt("id"));
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Result registerLoan(int borrower,double amount,int duration,String date,double perMonth,double totalPayment,String due,String dhamanaType,int memberId,
                                String propertyId,String propertyName,String desc,Double interest){
        conn = createConn();
        String regLoanSql = "INSERT INTO loans(borrower_id,amount_borrowed,duration,date_of_loan,amount_per_month,total_payment,dhamana_type," +
                "member_id,property_id,property_name,description,due,interest,tolerance) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String lastLoanSql = "SELECT * FROM loans";

        Result result = new Result();
        result.setSuccess(false);

        try{
            PreparedStatement ps = conn.prepareStatement(regLoanSql);
            ps.setInt(1,borrower);
            ps.setDouble(2,amount);
            ps.setInt(3,duration);
            ps.setString(4,date);
            ps.setDouble(5,perMonth);
            ps.setDouble(6,totalPayment);
            ps.setString(7,dhamanaType);
            ps.setInt(8,memberId);
            ps.setString(9,propertyId);
            ps.setString(10,propertyName);
            ps.setString(11,desc);
            ps.setString(12,due);
            ps.setDouble(13,interest);
            ps.setString(14,"no");

            ps.execute();
            result.setSuccess(true);

            PreparedStatement pb = conn.prepareStatement(lastLoanSql);
            ResultSet rs = pb.executeQuery();
            while (rs.next()){
                if(rs.isLast()){
                    //System.out.println("DB:"+rs.getInt("id"));
                    result.setId(rs.getInt("loan_id"));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean loanPayment(int loan_id,LocalDate date,double amount,String month,int year){
        conn = createConn();
        String paymentSql = "INSERT INTO payments(loan_id,date,amount,month,year) VALUES(?,?,?,?,?)";
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(paymentSql);
            ps.setInt(1,loan_id);
            ps.setDate(2,java.sql.Date.valueOf(date));
            ps.setDouble(3,amount);
            ps.setString(4,month);
            ps.setInt(5,year);

            ps.execute();
            success = true;
            updateLoanTable(loan_id,date,amount);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public void updateLoanTable(int loan_id,LocalDate date,double amount){
        conn = createConn();
        String readAmountPaid = "SELECT amount_paid,due FROM loans WHERE loan_id="+loan_id;

        try{
            PreparedStatement ps = conn.prepareStatement(readAmountPaid);
            ResultSet rs = ps.executeQuery();

            if(rs.first()){
                Double amountRead = rs.getDouble("amount_paid");
                Date dateSql = rs.getDate("due");
                String newDue = dateSql.toLocalDate().plusMonths(2).withDayOfMonth(1).minusDays(1).toString();

                amountRead = amountRead + amount;
                String updateLoan = "UPDATE loans SET last_payment="+amount+",lastpay_date='"+date+"',amount_paid="+amountRead+"," +
                        "due='"+newDue+"' WHERE loan_id="+loan_id;

                PreparedStatement pb = conn.prepareStatement(updateLoan);
                pb.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public boolean loanPaymentFine(int loan_id,LocalDate date,double amount,String month,int year){
        conn = createConn();
        String paymentSql = "INSERT INTO payments(loan_id,date,amount,month,year) VALUES(?,?,?,?,?)";
        boolean success;

        try{
            PreparedStatement ps = conn.prepareStatement(paymentSql);
            ps.setInt(1,loan_id);
            ps.setDate(2,java.sql.Date.valueOf(date));
            ps.setDouble(3,amount);
            ps.setString(4,month);
            ps.setInt(5,year);

            ps.execute();
            success = true;
            updateLoanTableFine(loan_id,date,amount);
        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    public void updateLoanTableFine(int loan_id,LocalDate date,double amount){
        conn = createConn();
        String readAmountPaid = "SELECT amount_paid FROM loans WHERE loan_id="+loan_id;

        try{
            PreparedStatement ps = conn.prepareStatement(readAmountPaid);
            ResultSet rs = ps.executeQuery();

            if(rs.first()){
                Double amountRead = rs.getDouble("amount_paid");

                amountRead = amountRead + amount;
                String updateLoan = "UPDATE loans SET last_payment="+amount+",lastpay_date='"+date+"',amount_paid="+amountRead+" " +
                        "WHERE loan_id="+loan_id;

                PreparedStatement pb = conn.prepareStatement(updateLoan);
                pb.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public ObservableList<Customer> loadCustomers(){
        conn = createConn();
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String customersSql = "SELECT * FROM customers";

        try{
            PreparedStatement ps = conn.prepareStatement(customersSql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                customers.add(new Customer("MJ/C/"+rs.getInt("id"),rs.getString("f_name")+" "+rs.getString("l_name"),
                        rs.getString("phone"),rs.getString("email"),rs.getString("bank"),rs.getString("account_no"),
                        rs.getString("company_name"),rs.getString("company_phone"),rs.getString("checksum")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customers;
    }

    public ObservableList<Loan> loadLoans(){
        conn = createConn();
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String loansSql = "SELECT * FROM loans";
        int borrower_id;
        int loan_id;
        String borrower_name="";
        String borrower_phone="";
        double borrower_fine = 0;
        String fine_String="0.0";
        double amount_paid;
        String amount_paidString;
        double last_pay;
        String last_payString;
        double amount_rem;
        String amount_remString;

        try {
            PreparedStatement ps = conn.prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            String getFineSql;
            PreparedStatement pk;
            ResultSet rk;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                loan_id = rs.getInt("loan_id");
                getBorrowerSql = "SELECT * FROM customers WHERE id="+borrower_id;
                pb = conn.prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");

                }

                getFineSql = "SELECT * FROM fines WHERE loan_id="+loan_id;
                pk = conn.prepareStatement(getFineSql);
                rk = pk.executeQuery();
                borrower_fine = 0.0;
                while(rk.next()){
                    //borrower_fine = borrower_fine + (rk.getDouble("amount")-rs.getDouble("amount_per_month"));
                    borrower_fine = borrower_fine + rk.getDouble("amount");
                }
                if(borrower_fine==0){
                    fine_String = String.format("%,.1f", borrower_fine);
                }else
                    fine_String = String.format("%,.0f", borrower_fine);

                amount_paid = rs.getDouble("amount_paid");
                if(amount_paid==0){
                    amount_paidString = String.format("%,.1f", amount_paid);
                }else
                    amount_paidString = String.format("%,.0f", amount_paid);

                last_pay = rs.getDouble("last_payment");
                if (last_pay==0)
                    last_payString = String.format("%,.1f",last_pay);
                else
                    last_payString = String.format("%,.0f", last_pay);

                amount_rem = (rs.getDouble("total_payment")-rs.getDouble("amount_paid"))+borrower_fine;
                if(amount_rem==0)
                    amount_remString = String.format("%,.1f", amount_rem);
                else
                    amount_remString = String.format("%,.0f", amount_rem);

                loans.add(new Loan("MJ/L/"+loan_id,borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        String.format("%,.0f", rs.getDouble("amount_borrowed")) ,rs.getInt("duration"),String.format("%,.0f",
                        rs.getDouble("total_payment")),String.format("%,.0f", rs.getDouble("amount_per_month")),
                        rs.getString("due"),rs.getString("lastpay_date"),amount_paidString,last_payString,amount_remString,
                        rs.getString("status"),borrower_phone,fine_String));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return loans;
    }

    public ObservableList<Loan> loadDueLoans(){
        conn = createConn();
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String loansSql = "SELECT * FROM loans WHERE status='active'";
        int borrower_id;
        int loan_id;
        String borrower_name="";
        String borrower_phone="";
        double borrower_fine;
        String fine_String="0.0";
        double amount_paid;
        String amount_paidString;
        double last_pay;
        String last_payString;
        double amount_rem;
        String amount_remString;

        try {
            PreparedStatement ps = conn.prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            String getFineSql;
            PreparedStatement pk;
            ResultSet rk;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                loan_id = rs.getInt("loan_id");
                getBorrowerSql = "SELECT * FROM customers WHERE id="+borrower_id;
                pb = conn.prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");

                }

                getFineSql = "SELECT * FROM fines WHERE loan_id="+loan_id;
                pk = conn.prepareStatement(getFineSql);
                rk = pk.executeQuery();
                borrower_fine = 0.0;
                while(rk.next()){
                    borrower_fine = borrower_fine + rk.getDouble("amount");
                }
                if(borrower_fine==0){
                    fine_String = String.format("%,.1f", borrower_fine);
                }else
                    fine_String = String.format("%,.0f", borrower_fine);

                amount_paid = rs.getDouble("amount_paid");
                if(amount_paid==0){
                    amount_paidString = String.format("%,.1f", rs.getDouble("amount_paid"));
                }else
                    amount_paidString = String.format("%,.0f", rs.getDouble("amount_paid"));

                last_pay = rs.getDouble("last_payment");
                if (last_pay==0)
                    last_payString = String.format("%,.1f",last_pay);
                else
                    last_payString = String.format("%,.0f", last_pay);

                amount_rem = (rs.getDouble("total_payment")-rs.getDouble("amount_paid"))+borrower_fine;
                if(amount_rem==0)
                    amount_remString = String.format("%,.1f", amount_rem);
                else
                    amount_remString = String.format("%,.0f", amount_rem);

                loans.add(new Loan("MJ/L/"+loan_id,borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        String.format("%,.0f", rs.getDouble("amount_borrowed")),rs.getInt("duration"),String.format("%,.0f", rs.getDouble("total_payment")),String.format("%,.0f", rs.getDouble("amount_per_month")),
                        rs.getString("due"),rs.getString("lastpay_date"),amount_paidString,last_payString,
                        amount_remString,rs.getString("status"),borrower_phone,fine_String));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return loans;
    }

    public ObservableList<Loan> loadOneMonthLateLoans(){
        conn = createConn();
        ObservableList<Loan> oneLateloans = FXCollections.observableArrayList();
//        String todayDate = "'"+LocalDate.now().toString()+"'";
//        String compareDate = "'"+LocalDate.now().minusMonths(1).withDayOfMonth(1).minusDays(1).toString()+"'";
//        String loansSql = "SELECT * FROM LOANS WHERE DUE > "+compareDate+" AND DUE < "+todayDate+" AND STATUS='active'";
        int borrower_id;
        int loan_id;
        String borrower_name="";
        String borrower_phone="";
        double amount_paid;
        String amount_paidString;
        double last_pay;
        String last_payString;
        double amount_rem;
        String amount_remString;
        double borrower_fine = 0;
        String fine_String="0.0";

        String loanSql = "SELECT * FROM loans";
        int counter;

        try {
            PreparedStatement ps = conn.prepareStatement(loanSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            String getFineSql;
            PreparedStatement pk;
            ResultSet rk;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                loan_id = rs.getInt("loan_id");
                getBorrowerSql = "SELECT * FROM customers WHERE id="+borrower_id;
                pb = conn.prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");
                }


                getFineSql = "SELECT * FROM fines WHERE loan_id="+loan_id;
                pk = conn.prepareStatement(getFineSql);
                rk = pk.executeQuery();
                borrower_fine = 0.0;
                counter = 0;
                while(rk.next()){
                    borrower_fine = borrower_fine + rk.getDouble("amount");
                    if(borrower_fine>0)
                        counter++;
                }
                if(counter==1){
                    if(borrower_fine==0){
                        fine_String = String.format("%,.1f", borrower_fine);
                    }else
                        fine_String = String.format("%,.0f", borrower_fine);

                    amount_paid = rs.getDouble("amount_paid");
                    if(amount_paid==0){
                        amount_paidString = String.format("%,.1f", rs.getDouble("amount_paid"));
                    }else
                        amount_paidString = String.format("%,.0f", rs.getDouble("amount_paid"));

                    last_pay = rs.getDouble("last_payment");
                    if (last_pay==0)
                        last_payString = String.format("%,.1f",last_pay);
                    else
                        last_payString = String.format("%,.0f", last_pay);

                    amount_rem = (rs.getDouble("total_payment")-rs.getDouble("amount_paid"))+borrower_fine;
                    if(amount_rem==0)
                        amount_remString = String.format("%,.1f", amount_rem);
                    else
                        amount_remString = String.format("%,.0f", amount_rem);

                    oneLateloans.add(new Loan("MJ/L/"+loan_id,borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                            String.format("%,.0f", rs.getDouble("amount_borrowed")),rs.getInt("duration"),String.format("%,.0f", rs.getDouble("total_payment")),String.format("%,.0f", rs.getDouble("amount_per_month")),
                            rs.getString("due"),rs.getString("lastpay_date"),amount_paidString,last_payString,amount_remString,rs.getString("status"),borrower_phone,fine_String));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return oneLateloans;
    }

    public ObservableList<Loan> loadTwoMonthLateLoans(){
        conn = createConn();
        ObservableList<Loan> twoLateloans = FXCollections.observableArrayList();
//        String lastmonthDate = "'"+LocalDate.now().minusMonths(0).withDayOfMonth(1).minusDays(1).toString()+"'";
//        String compareDate = "'"+LocalDate.now().minusMonths(2).withDayOfMonth(1).minusDays(1).toString()+"'";
//        String loansSql = "SELECT * FROM LOANS WHERE DUE > "+compareDate+" AND DUE < "+lastmonthDate+" AND STATUS='active'";
        int borrower_id;
        int loan_id;
        String borrower_name="";
        String borrower_phone="";
        double amount_paid;
        String amount_paidString;
        double last_pay;
        String last_payString;
        double amount_rem;
        String amount_remString;
        double borrower_fine = 0;
        String fine_String="0.0";

        String loanSql = "SELECT * FROM loans";
        int counter;

        try {
            PreparedStatement ps = conn.prepareStatement(loanSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            String getFineSql;
            PreparedStatement pk;
            ResultSet rk;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                loan_id = rs.getInt("loan_id");
                getBorrowerSql = "SELECT * FROM customers WHERE id="+borrower_id;
                pb = conn.prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");
                }


                getFineSql = "SELECT * FROM fines WHERE loan_id="+loan_id;
                pk = conn.prepareStatement(getFineSql);
                rk = pk.executeQuery();
                borrower_fine = 0.0;
                counter = 0;
                while(rk.next()){
                    borrower_fine = borrower_fine + rk.getDouble("amount");
                    if(borrower_fine>0)
                        counter++;
                }
                if(counter==2){
                    if(borrower_fine==0){
                        fine_String = String.format("%,.1f", borrower_fine);
                    }else
                        fine_String = String.format("%,.0f", borrower_fine);

                    amount_paid = rs.getDouble("amount_paid");
                    if(amount_paid==0){
                        amount_paidString = String.format("%,.1f", rs.getDouble("amount_paid"));
                    }else
                        amount_paidString = String.format("%,.0f", rs.getDouble("amount_paid"));

                    last_pay = rs.getDouble("last_payment");
                    if (last_pay==0)
                        last_payString = String.format("%,.1f",last_pay);
                    else
                        last_payString = String.format("%,.0f", last_pay);

                    amount_rem = (rs.getDouble("total_payment")-rs.getDouble("amount_paid"))+borrower_fine;
                    if(amount_rem==0)
                        amount_remString = String.format("%,.1f", amount_rem);
                    else
                        amount_remString = String.format("%,.0f", amount_rem);

                    twoLateloans.add(new Loan("MJ/L/"+loan_id,borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                            String.format("%,.0f", rs.getDouble("amount_borrowed")),rs.getInt("duration"),String.format("%,.0f", rs.getDouble("total_payment")),String.format("%,.0f", rs.getDouble("amount_per_month")),
                            rs.getString("due"),rs.getString("lastpay_date"),amount_paidString,last_payString,amount_remString,rs.getString("status"),borrower_phone,fine_String));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return twoLateloans;
    }

    public ObservableList<Loan> loadPenaltyLateLoans(){
        conn = createConn();
        ObservableList<Loan> penaltyloans = FXCollections.observableArrayList();
//        String lastmonthDate = "'"+LocalDate.now().minusMonths(1).withDayOfMonth(1).minusDays(1).toString()+"'";
//        String compareDate = "'"+LocalDate.now().minusMonths(120).withDayOfMonth(1).minusDays(1).toString()+"'";
//        String loansSql = "SELECT * FROM LOANS WHERE DUE > "+compareDate+" AND DUE < "+lastmonthDate+" AND STATUS='active'";
        int borrower_id;
        int loan_id;
        String borrower_name="";
        String borrower_phone="";
        double amount_paid;
        String amount_paidString;
        double last_pay;
        String last_payString;
        double amount_rem;
        String amount_remString;
        double borrower_fine = 0;
        String fine_String="0.0";

        String loanSql = "SELECT * FROM loans";
        int counter;

        try {
            PreparedStatement ps = conn.prepareStatement(loanSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            String getFineSql;
            PreparedStatement pk;
            ResultSet rk;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                loan_id = rs.getInt("loan_id");
                getBorrowerSql = "SELECT * FROM customers WHERE id="+borrower_id;
                pb = conn.prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");
                }

                getFineSql = "SELECT * FROM fines WHERE loan_id="+loan_id;
                pk = conn.prepareStatement(getFineSql);
                rk = pk.executeQuery();
                borrower_fine = 0.0;
                counter = 0;
                while(rk.next()){
                    borrower_fine = borrower_fine + rk.getDouble("amount");
                    if(borrower_fine>0)
                        counter++;
                }
                if(counter>2){
                    if(borrower_fine==0){
                        fine_String = String.format("%,.1f", borrower_fine);
                    }else
                        fine_String = String.format("%,.0f", borrower_fine);

                    amount_paid = rs.getDouble("amount_paid");
                    if(amount_paid==0){
                        amount_paidString = String.format("%,.1f", rs.getDouble("amount_paid"));
                    }else
                        amount_paidString = String.format("%,.0f", rs.getDouble("amount_paid"));

                    last_pay = rs.getDouble("last_payment");
                    if (last_pay==0)
                        last_payString = String.format("%,.1f",last_pay);
                    else
                        last_payString = String.format("%,.0f", last_pay);

                    amount_rem = (rs.getDouble("total_payment")-rs.getDouble("amount_paid"))+borrower_fine;
                    if(amount_rem==0)
                        amount_remString = String.format("%,.1f", amount_rem);
                    else
                        amount_remString = String.format("%,.0f", amount_rem);

                    penaltyloans.add(new Loan("MJ/L/"+loan_id,borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                            String.format("%,.0f", rs.getDouble("amount_borrowed")),rs.getInt("duration"),String.format("%,.0f", rs.getDouble("total_payment")),String.format("%,.0f", rs.getDouble("amount_per_month")),
                            rs.getString("due"),rs.getString("lastpay_date"),amount_paidString,last_payString,amount_remString,rs.getString("status"),borrower_phone,fine_String));
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return penaltyloans;
    }

    public ObservableList<Loan> searchLoan(int loany_id){
        conn = createConn();
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String loansSql = "SELECT * FROM loans WHERE loan_id LIKE '%"+loany_id+"%'";
        int borrower_id;
        int loan_id;
        String borrower_name="";
        String borrower_phone="";
        double amount_paid;
        String amount_paidString;
        double last_pay;
        String last_payString;
        double amount_rem;
        String amount_remString;
        double borrower_fine = 0;
        String fine_String="0.0";

        try {
            PreparedStatement ps = conn.prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            String getFineSql;
            PreparedStatement pk;
            ResultSet rk;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                loan_id = rs.getInt("loan_id");
                getBorrowerSql = "SELECT * FROM customers WHERE id="+borrower_id;
                pb = conn.prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");

                }


                getFineSql = "SELECT * FROM fines WHERE loan_id="+loan_id;
                pk = conn.prepareStatement(getFineSql);
                rk = pk.executeQuery();
                borrower_fine = 0.0;
                while(rk.next()){
                    borrower_fine = borrower_fine + rk.getDouble("amount");
                }
                if(borrower_fine==0){
                    fine_String = String.format("%,.1f", borrower_fine);
                }else
                    fine_String = String.format("%,.0f", borrower_fine);

                amount_paid = rs.getDouble("amount_paid");
                if(amount_paid==0){
                    amount_paidString = String.format("%,.1f", rs.getDouble("amount_paid"));
                }else
                    amount_paidString = String.format("%,.0f", rs.getDouble("amount_paid"));

                last_pay = rs.getDouble("last_payment");
                if (last_pay==0)
                    last_payString = String.format("%,.1f",last_pay);
                else
                    last_payString = String.format("%,.0f", last_pay);

                amount_rem = (rs.getDouble("total_payment")-rs.getDouble("amount_paid"))+borrower_fine;
                if(amount_rem==0)
                    amount_remString = String.format("%,.1f", amount_rem);
                else
                    amount_remString = String.format("%,.0f", amount_rem);

                loans.add(new Loan("MJ/L/"+loan_id,borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        String.format("%,.0f", rs.getDouble("amount_borrowed")),rs.getInt("duration"),String.format("%,.0f", rs.getDouble("total_payment")),String.format("%,.0f", rs.getDouble("amount_per_month")),
                        rs.getString("due"),rs.getString("lastpay_date"),amount_paidString,last_payString,amount_remString,rs.getString("status"),borrower_phone,fine_String));
                }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return loans;
    }

    public ObservableList<Customer> searchCustomers(String id){
        conn = createConn();
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String customersIdSql = "SELECT * FROM customers WHERE id LIKE '%"+id+"%'";
        String customersNameSql = "SELECT * FROM customers WHERE f_name LIKE '%"+id+"%' OR l_name LIKE '%"+id+"%'";
        PreparedStatement ps;

        try{
            if(Checker.isStringInt(id)){
                ps = conn.prepareStatement(customersIdSql);
            }else {
                ps= conn.prepareStatement(customersNameSql);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                customers.add(new Customer("MJ/C/"+rs.getInt("id"),rs.getString("f_name")+" "+rs.getString("l_name"),
                        rs.getString("phone"),rs.getString("email"),rs.getString("bank"),rs.getString("account_no"),
                        rs.getString("company_name"),rs.getString("company_phone"),rs.getString("checksum")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customers;
    }

    public void updateProfPhoto(String photoName,int id){
        conn = createConn();
        String profPhotoSql = "UPDATE customers SET prof_photo='"+photoName+"' WHERE id="+id;

        try{
            PreparedStatement ps = conn.prepareStatement(profPhotoSql);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

     public void updateLoanFile(String FileName,int id){
         conn = createConn();
         String loanFileSql = "UPDATE loans SET loan_file='"+FileName+"' WHERE Loan_id="+id;

        try{
            PreparedStatement ps = conn.prepareStatement(loanFileSql);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public EditCustomer viewCustomer(int id){
        conn = createConn();
        String editCustomerSql = "SELECT * FROM customers WHERE id="+id;
        EditCustomer customer = null;

        String getActiveLoansSql;
        PreparedStatement pb;
        ResultSet rb;
        int num_loans = 0;

        String getDoneLoansSql;
        PreparedStatement pc;
        ResultSet rk;
        int num_done_loans = 0;

        try{
            PreparedStatement ps = conn.prepareStatement(editCustomerSql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()){
                getActiveLoansSql = "SELECT * FROM loans WHERE status='active' AND borrower_id="+id;
                pb = conn.prepareStatement(getActiveLoansSql);
                rb = pb.executeQuery();
                while (rb.next()){
                    num_loans++;
                }

                getDoneLoansSql = "SELECT * FROM loans WHERE status='done' AND borrower_id="+id;
                pc = conn.prepareStatement(getDoneLoansSql);
                rk = pc.executeQuery();
                while (rk.next()){
                    num_done_loans++;
                }

                customer = new EditCustomer(rs.getString("f_name"),rs.getString("m_name"),rs.getString("l_name"),rs.getString("gender"),
                        rs.getDate("dob"),rs.getString("phone"),rs.getString("email"),rs.getString("postal"),rs.getString("prof_photo"),
                        rs.getString("bank"),rs.getString("account_no"),rs.getString("company_name"),rs.getString("company_phone"),
                        rs.getString("company_location"),rs.getString("checksum"),num_loans,num_done_loans);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customer;
    }

    public void checkIfLoanDone(int id){
        conn = createConn();
        String checkLoanSql = "SELECT DISTINCT total_payment,amount_paid FROM loans WHERE loan_id="+id;
        String updateStatusSql = "UPDATE loans SET status='done' WHERE loan_id="+id;

        try{
            PreparedStatement ps = conn.prepareStatement(checkLoanSql);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                Double total_payment = rs.getDouble("total_payment");
                Double amount_paid = rs.getDouble("amount_paid");
                if(amount_paid >= total_payment){
                    PreparedStatement pb = conn.prepareStatement(updateStatusSql);
                    pb.execute();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Transaction> getUserTransactions(int id){
        conn = createConn();
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String sql = "SELECT customers.id,payments.loan_id,customers.f_name,customers.l_name ,payments.amount,payments.date " +
                "FROM customers,loans,payments WHERE customers.id=loans.borrower_id AND loans.loan_id=payments.loan_id AND payments.loan_id="+id;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String fullname=rs.getString("customers.f_name")+"  "+rs.getString("customers.l_name");
                transactions.add(new Transaction("MJ/C/"+rs.getString("customers.id"),"MJ/L/"+rs.getString("payments.loan_id")
                        ,fullname,rs.getDouble("payments.amount"),rs.getString("payments.date")));
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }

    public ObservableList<Transaction> getUserFineTransactions(int id){
        conn = createConn();
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String sql = "SELECT customers.id,fine_payments.loan_id,customers.f_name,customers.l_name ,fine_payments.amount,fine_payments.date " +
                "FROM customers,loans,fine_payments WHERE customers.id=loans.borrower_id AND loans.loan_id=fine_payments.loan_id AND fine_payments.loan_id="+id;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String fullname=rs.getString("customers.f_name")+"  "+rs.getString("customers.l_name");
                transactions.add(new Transaction("MJ/C/"+rs.getString("customers.id"),"MJ/L/"+rs.getString("fine_payments.loan_id")
                        ,fullname,rs.getDouble("fine_payments.amount"),rs.getString("fine_payments.date")));
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return transactions;
    }

    public BorrowerIDAndName getBorrowerNameAndId(int id){
        conn = createConn();
        String sql = "SELECT * FROM customers WHERE id="+id;
        BorrowerIDAndName borrowerIDAndName = null;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs =ps.executeQuery();

            if(rs.first()){
                borrowerIDAndName = new BorrowerIDAndName("MJ/C/"+rs.getInt("id"),rs.getString("f_name")+" "+rs.getString("l_name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return borrowerIDAndName;
    }

    public boolean editCustomerDetails(int id,String phone,String email,String postal,String bank,String account,String company_name,
                                    String company_loc,String company_phone,String checknumber){
        conn = createConn();
        String sql = "UPDATE customers SET phone='"+phone+"',email='"+email+"',postal='"+postal+"',bank='"+bank+"'," +
                "account_no='"+account+"',company_name='"+company_name+"',company_phone='"+company_phone+"',company_location='"+company_loc+"'," +
                "checksum='"+checknumber+"' WHERE id="+id;
        boolean success;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            success=true;
        }catch (SQLException e){
            success=false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public EditLoan viewLoan(int id){
        conn = createConn();
        String sql = "SELECT borrower_id,amount_borrowed,duration,date_of_loan,amount_per_month,member_id,property_id," +
                "property_name,description,total_payment,tolerance FROM loans WHERE loan_id="+id;
        EditLoan editLoan = null;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                editLoan = new EditLoan(rs.getInt("borrower_id"),rs.getDouble("amount_borrowed"),rs.getInt("duration"),
                        rs.getDate("date_of_loan").toLocalDate(),rs.getDouble("amount_per_month"),rs.getDouble("total_payment"),
                        rs.getInt("member_id"),rs.getString("property_id"),rs.getString("property_name"),
                        rs.getString("description"),rs.getString("tolerance"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return editLoan;
    }

    public boolean editLoanDetails(int id,int borrower_id,double loan_amount,int duration,LocalDate date_loan,double perMonth,
            double total_payment,int mdhamini_id,String property_id,String property_name,String desc,String tolerance){

        conn = createConn();

        Date loan_date = java.sql.Date.valueOf(date_loan);

        String sql = "UPDATE loans SET borrower_id='"+borrower_id+"',amount_borrowed='"+loan_amount+"',duration='"+duration+"'," +
                "date_of_loan='"+loan_date+"'," +"amount_per_month='"+perMonth+"',total_payment='"+total_payment+"',member_id='"+
                mdhamini_id+"',property_id='"+property_id+"',property_name='"+property_name+"',description='"+desc+"',tolerance='"+
                tolerance+"' " + "WHERE loan_id="+id;
        boolean success;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            success=true;
        }catch (SQLException e){
            success=false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public String viewSms(int id){
        conn = createConn();
        String sql = "SELECT sms FROM messages WHERE sms_id="+id;
        String sms = null;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                sms = rs.getString("sms");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sms;
    }

    public boolean editSms(int id,String sms){
        conn = createConn();
        String sql = "UPDATE messages SET sms='"+sms+"' WHERE sms_id="+id;
        boolean success;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            success=true;
        }catch (SQLException e){
            success=false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public ObservableList<User> getUsers(String level){
        conn = createConn();
        ObservableList<User> users = FXCollections.observableArrayList();
        String usersSql;

        if(level.equals("main admin"))
            usersSql = "SELECT * FROM users";
        else
            usersSql = "SELECT * FROM users WHERE role='worker' ";

        try{
            PreparedStatement ps = conn.prepareStatement(usersSql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                users.add(new User("MJ/U/"+rs.getInt("id"),rs.getString("fname")+" "+rs.getString("lname"),
                        rs.getString("status"),rs.getString("role")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public Result addUser(String fname,String lname,String uname,String password,String role,String status){
        conn = createConn();
        String userSql = "INSERT INTO users (fname,lname,username,password,role,status) VALUES(?,?,?,?,?,?)";
        String lastUserSql = "SELECT * FROM users";

        Result result = new Result();
        result.setSuccess(false);

        try{
            PreparedStatement ps = conn.prepareStatement(userSql);
            ps.setString(1,fname);
            ps.setString(2,lname);
            ps.setString(3,uname);
            ps.setString(4,password);
            ps.setString(5,role);
            ps.setString(6,status);

            ps.execute();
            result.setSuccess(true);

            PreparedStatement pb = conn.prepareStatement(lastUserSql);
            ResultSet rs = pb.executeQuery();

            while (rs.next()){
                if(rs.isLast()){
                    result.setId(rs.getInt("id"));
                }
            }

        }catch (SQLException e){
            result.setSuccess(false);
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void updateUserStatus(String action,int id){
        conn = createConn();
        String status;
        if (action.equals("activate")){
            status = "active";
        }else {
            status = "deactive";
        }

        String statusSql = "UPDATE users SET status='"+status+"' WHERE id="+id;

        try {
            PreparedStatement ps = conn.prepareStatement(statusSql);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void fineHandler(){
        conn = createConn();
        String state = "";
            int fine_id;
            int loan_id;
            double amount = 0;
            double perMonth = 0;
            double paidAmount = 0;
            Date lastDue;
            String month;
            int year;
            String tolerance;
            String monthEnd;
            String controllerSql = "SELECT * FROM controller";
            String fineSql = "SELECT * FROM fines";
            String loanSql = "SELECT * FROM loans";
            String addFineSql = "INSERT INTO fines (month,year,amount,loan_id,tolerance) VALUES(?,?,?,?,?)";

            try {
                PreparedStatement ps = conn.prepareStatement(controllerSql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    System.out.println("Imefika");
                    state = rs.getString("state");
                    System.out.println(rs.getString("state"));
                }


                if (state.equals("not yet")) {

                    PreparedStatement pb = conn.prepareStatement(fineSql);
                    ResultSet rb = pb.executeQuery();

                    PreparedStatement pd = conn.prepareStatement(loanSql);
                    ResultSet rd = pd.executeQuery();

                    while (rb.next()) {

                        // Incrementing fine
                            fine_id = rb.getInt("fine_id");
                            amount = rb.getDouble("amount");
                            loan_id = rb.getInt("loan_id");
                            month = rb.getString("month");
                            year = rb.getInt("year");
                            tolerance = rb.getString("tolerance");

                            PreparedStatement pf = conn.prepareStatement(loanSql);
                            ResultSet rf = pf.executeQuery();

                            if(tolerance.equals("no")){
                                System.out.println(amount);
                                while(rf.next()){
                                    if(rf.getInt("loan_id")==loan_id && rf.getString("status").equals("active")){
                                        String paymentsSql = "SELECT * FROM payments WHERE loan_id="+loan_id+" AND " +
                                                "payments.month='"+month+"' AND payments.year="+year;
                                        PreparedStatement ph = conn.prepareStatement(paymentsSql);
                                        ResultSet rh = ph.executeQuery();

                                        paidAmount = 0;

                                        while(rh.next()){
                                            System.out.println(rh.getDouble("amount"));
                                            paidAmount = paidAmount + rh.getDouble("amount");
                                        }

                                        if(amount>0){
                                            amount = ( ((rf.getDouble("amount_per_month")-paidAmount)  + amount)*0.15) + amount;
                                        }
                                    }
                                }
                                //amount = amount + amount*0.15;

                                if(amount>0){
                                    amount = (double)Math.round(amount);
                                    String fineUpdateSql = "UPDATE fines SET amount="+amount+" WHERE fine_id=" + fine_id;
                                    PreparedStatement pc = conn.prepareStatement(fineUpdateSql);
                                    pc.execute();
                                }
                            }
                    }

                    while(rd.next()){

                        // Adding a fine
                            lastDue = rd.getDate("due");
                            System.out.println(lastDue);
                            System.out.println(LocalDate.now().minusMonths(0).withDayOfMonth(1).minusDays(1));
                            if(lastDue.equals(java.sql.Date.valueOf(LocalDate.now().minusMonths(0).withDayOfMonth(1).minusDays(1)))
                                    && rd.getString("status").equals("active") && rd.getString("tolerance").equals("no")){
                                loan_id = rd.getInt("loan_id");
                                perMonth = rd.getDouble("amount_per_month");
                                //perMonth = perMonth + perMonth*0.15;
                                perMonth = perMonth*0.15;
                                perMonth = (double)Math.round(perMonth);
                                monthEnd = lastDue.toLocalDate().plusMonths(2).withDayOfMonth(1).minusDays(1).toString();

                                String updateLoanSql = "UPDATE loans SET due='"+monthEnd+"' WHERE loan_id="+loan_id;
                                PreparedStatement ph = conn.prepareStatement(updateLoanSql);
                                ph.execute();

                                PreparedStatement pk = conn.prepareStatement(addFineSql);
                                pk.setString(1,lastDue.toLocalDate().getMonth().name());
                                pk.setInt(2,lastDue.toLocalDate().getYear());
                                pk.setDouble(3,perMonth);
                                pk.setInt(4,loan_id);
                                pk.setString(5,"no");
                                pk.execute();

                            }

                    }

                    state = "done";
                    String stateSql = "UPDATE controller SET state='"+state+"'";
                    PreparedStatement pz = conn.prepareStatement(stateSql);
                    pz.execute();


                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    }

    public void stateChanger(){
        conn = createConn();
        String stateSql ="UPDATE controller SET state='not yet'";
        try{
            PreparedStatement ps = conn.prepareStatement(stateSql);
            ps.execute();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean updatePassword(String password,int id){
        conn = createConn();
        String passSql = "UPDATE users SET password='"+password+"' WHERE id="+id;
        boolean success = false;
        try{
            PreparedStatement ps = conn.prepareStatement(passSql);
            ps.execute();
            success = true;
        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean deleteUser(int id){
        conn = createConn();
        String userSql = "DELETE FROM users WHERE id="+id;

        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(userSql);
            ps.execute();
            success = true;
        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public double getInterest(){
        conn = createConn();
        String rateSql = "SELECT * FROM interest";
        double rate = 0;

        try{
            PreparedStatement ps = conn.prepareStatement(rateSql);
            ResultSet rs = ps.executeQuery();

            if(rs.first()){
               rate = rs.getDouble("interest_value");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rate;
    }

    public boolean setInterest(double rate){
        conn = createConn();
        String rateSql = "UPDATE interest SET interest_value ="+rate;
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(rateSql);
            ps.execute();
            success = true;
        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public ObservableList<Fine> getFines(int id){
        conn = createConn();
        ObservableList<Fine> fines = FXCollections.observableArrayList();
        double perMonth = 0;
        int loan_id;
        String month;
        int year;
        double paidAmount=0;
        double fineAmount = 0;

        try{
            String fineSql = "SELECT * FROM fines WHERE loan_id="+id;
            PreparedStatement ps = conn.prepareStatement(fineSql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                loan_id = rs.getInt("loan_id");
                month = rs.getString("month");
                year = rs.getInt("year");

                fineAmount = fineAmount + rs.getDouble("amount");

                String finePerMonthSql = "SELECT amount_per_month FROM loans WHERE loan_id="+loan_id;
                PreparedStatement pk = conn.prepareStatement(finePerMonthSql);
                ResultSet rk = pk.executeQuery();

                if(rk.first()){

                    String paymentsSql = "SELECT * FROM payments WHERE loan_id="+loan_id+" AND payments.month='"+month+"'" +
                            " AND payments.year="+year;
                    PreparedStatement ph = conn.prepareStatement(paymentsSql);
                    ResultSet rh = ph.executeQuery();

                    paidAmount = 0;

                    while(rh.next()){
                        paidAmount = paidAmount + rh.getDouble("amount");
                    }

                    perMonth = rk.getDouble("amount_per_month")-paidAmount;
                }
                if(fineAmount>0){
                    fines.add(new Fine(rs.getInt("fine_id"),rs.getString("month"),rs.getInt("year"),rs.getDouble("amount"),perMonth));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return fines;
    }

    public List<String> getPhoneNumbers(){
        conn = createConn();
        String phoneSql = "SELECT phone FROM customers";
        List<String> phoneNums = new ArrayList();
        try{
            PreparedStatement ps = conn.prepareStatement(phoneSql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                phoneNums.add(rs.getString("phone"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return phoneNums;
    }

    public boolean payFine(int loan_id,LocalDate date,double amount,String month,int year){
        conn = createConn();
        String paySql = "INSERT INTO fine_payments (loan_id,date,amount,month,year) VALUES (?,?,?,?,?)";
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(paySql);
            ps.setInt(1,loan_id);
            ps.setDate(2,java.sql.Date.valueOf(date));
            ps.setDouble(3,amount);
            ps.setString(4,month);
            ps.setInt(5,year);

            ps.execute();
            success = true;

        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public boolean reduceFine(int fine_id,double amount){
        conn = createConn();
        String fineSql = "SELECT * FROM fines WHERE fine_id="+fine_id;
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(fineSql);
            ResultSet rs = ps.executeQuery();

            if(rs.first()){
                double fine_amount = rs.getDouble("amount");

                fine_amount = fine_amount - amount;

                String reduceSql = "UPDATE fines SET amount="+fine_amount+" WHERE fine_id="+fine_id;
                PreparedStatement pk = conn.prepareStatement(reduceSql);
                pk.execute();

                System.out.println(rs.getDouble("amount"));

                String checkSql = "SELECT amount FROM fines WHERE fine_id="+fine_id;
                PreparedStatement pd = conn.prepareStatement(checkSql);
                ResultSet rd = pd.executeQuery();

                if(rd.first()){
                    if (rd.getDouble("amount")<1){
                        String zeroSql = "UPDATE fines SET amount=0 WHERE fine_id="+fine_id;
                        PreparedStatement ph = conn.prepareStatement(zeroSql);
                        ph.execute();
                    }
                }

                success = true;
            }
        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean updateFinetolerance(int loan_id,String tolerance){
        conn = createConn();
        String toleranceSql = "UPDATE fines SET tolerance='"+tolerance+"' WHERE loan_id="+loan_id;
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(toleranceSql);
            ps.execute();
            success = true;

        }catch (SQLException e){
            success = false;
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

//    public void monthInitialIncrementer(int loan_id){
//        try{
//            String updateLoanSql = "UPDATE LOANS SET MONTHS_LATE = MONTHS_LATE + 1 WHERE LOAN_ID="+loan_id;
//            PreparedStatement ph = conn.prepareStatement(updateLoanSql);
//            ph.execute();
//        }catch(SQLException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public boolean deleteFine(int fine_id){
        conn = createConn();
        String del_query = "delete from fines where fine_id="+fine_id;
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(del_query);
            ps.execute();
            success = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public boolean editFine(int fine_id,double fine_amount){
        conn = createConn();
        String edit_query = "update fines set amount="+fine_amount+" where fine_id="+fine_id;
        boolean success = false;

        try{
            PreparedStatement ps = conn.prepareStatement(edit_query);
            ps.execute();
            success = true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public boolean fineChecker(int id){
        conn = createConn();
        double fineAmount=0;
        boolean fineExists = false;

        try{
            String fineSql = "SELECT * FROM fines WHERE loan_id="+id;
            PreparedStatement ps = conn.prepareStatement(fineSql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                fineAmount = fineAmount + rs.getDouble("amount");

                if(fineAmount>0){
                    fineExists = true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return fineExists;
    }

    public String getMonthAmount(int loan_id,String month,int year){
        conn = createConn();
        double perMonth = 0;
        double paidAmount=0;

        try{

                String finePerMonthSql = "SELECT amount_per_month FROM loans WHERE loan_id="+loan_id;
                PreparedStatement pk = conn.prepareStatement(finePerMonthSql);
                ResultSet rk = pk.executeQuery();

                if(rk.first()){

                    String paymentsSql = "SELECT * FROM payments WHERE loan_id="+loan_id+" AND payments.month='"+month+"' " +
                            "AND payments.year="+year;
                    PreparedStatement ph = conn.prepareStatement(paymentsSql);
                    ResultSet rh = ph.executeQuery();

                    paidAmount = 0;

                    while(rh.next()){
                        paidAmount = paidAmount + rh.getDouble("amount");
                    }

                    perMonth = rk.getDouble("amount_per_month")-paidAmount;
                }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String perMonth_string;
        if (perMonth==0)
            perMonth_string = String.format("%,.1f",perMonth);
        else
            perMonth_string = String.format("%,.0f", perMonth);

        return perMonth_string;
    }

    public String getLastPayMonth(int loan_id){
        conn = createConn();
        String sql = "SELECT * FROM payments WHERE loan_id="+loan_id;
        String month = "";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.last()){
                month = rs.getString("month")+rs.getInt("year");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return month;
    }
}