package sample.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Customer;
import sample.EditCustomer;
import sample.Loan;
import sample.Result;

import java.sql.*;
import java.time.LocalDate;

/**
 * Created by Apix on 25/04/2017.
 */
public class DatabaseHandler {

    private static Connection conn;
    private static String url = "jdbc:mysql://localhost:3306/mikopo_app";
    private static String user = "root";
    private static String password = "";
    int id;

    public static Connection createConn() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public boolean login(String username,String passwd){
        String userSql = "SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?";
        boolean success = false;
        try {
            //Statement st = createConn().createStatement();
            //ResultSet rs = st.executeQuery(userSql);
            PreparedStatement ps = createConn().prepareStatement(userSql);
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
        String userDetailsSql = "SELECT * FROM USERS WHERE ID=?";
        JSONObject jsonObject = new JSONObject();

        try {
            PreparedStatement ps = createConn().prepareStatement(userDetailsSql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                jsonObject.put("id",rs.getInt("id"));
                jsonObject.put("fname",rs.getString("fname"));
                jsonObject.put("lname",rs.getString("lname"));
                jsonObject.put("username",rs.getString("username"));
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
                                   String company_phone, String company_location, String checksum){
        String regCustomerSql = "INSERT INTO CUSTOMERS (f_name,l_name,m_name,gender,dob,phone,email,postal," +
                "prof_photo,bank,account_no,company_name,company_phone,company_location,checksum)"
                +"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String lastCustomerSql = "SELECT * FROM CUSTOMERS";

        Result result = new Result();
        result.setSuccess(false);
        try{
            PreparedStatement ps = createConn().prepareStatement(regCustomerSql);
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

            ps.execute();
            result.setSuccess(true);

            PreparedStatement pb = createConn().prepareStatement(lastCustomerSql);
            ResultSet rs = pb.executeQuery();
            while (rs.next()){
                if(rs.isLast()){
                    //System.out.println("DB:"+rs.getInt("id"));
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

    public Result registerLoan(int borrower,int amount,int duration,String date,int perMonth,int totalPayment,String due,String dhamanaType,int memberId,
                                String propertyId,String propertyName,String desc){
        String regLoanSql = "INSERT INTO LOANS(borrower_id,amount_borrowed,duration,date_of_loan,amount_per_month,total_payment,dhamana_type," +
                "member_id,property_id,property_name,description,due) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        String lastLoanSql = "SELECT * FROM LOANS";

        Result result = new Result();
        result.setSuccess(false);

        try{
            PreparedStatement ps = createConn().prepareStatement(regLoanSql);
            ps.setInt(1,borrower);
            ps.setInt(2,amount);
            ps.setInt(3,duration);
            ps.setString(4,date);
            ps.setInt(5,perMonth);
            ps.setDouble(6,totalPayment);
            ps.setString(7,dhamanaType);
            ps.setInt(8,memberId);
            ps.setString(9,propertyId);
            ps.setString(10,propertyName);
            ps.setString(11,desc);
            ps.setString(12,due);

            ps.execute();
            result.setSuccess(true);

            PreparedStatement pb = createConn().prepareStatement(lastLoanSql);
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

    public boolean loanPayment(int loan_id,String date,double amount){
        String paymentSql = "INSERT INTO PAYMENTS(loan_id,date,amount) VALUES(?,?,?)";
        boolean success = false;

        try{
            PreparedStatement ps = createConn().prepareStatement(paymentSql);
            ps.setInt(1,loan_id);
            ps.setString(2,date);
            ps.setDouble(3,amount);

            if(ps.execute()){
                success = true;
            }
            updateLoanTable(loan_id,date,amount);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    public void updateLoanTable(int loan_id,String date,double amount){
        String readAmountPaid = "SELECT AMOUNT_PAID,DUE FROM LOANS WHERE LOAN_ID="+loan_id;

        try{
            PreparedStatement ps = createConn().prepareStatement(readAmountPaid);
            ResultSet rs = ps.executeQuery();

            if(rs.first()){
                Double amountRead = rs.getDouble("amount_paid");
                Date dateSql = rs.getDate("due");
                String newDue = dateSql.toLocalDate().plusMonths(2).withDayOfMonth(1).minusDays(1).toString();

                amountRead = amountRead + amount;
                String updateLoan = "UPDATE LOANS SET last_payment="+amount+",lastpay_date='"+date+"',amount_paid="+amountRead+",due='"+newDue+"' WHERE LOAN_ID="+loan_id;

                PreparedStatement pb = createConn().prepareStatement(updateLoan);
                pb.execute();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public ObservableList<Customer> loadCustomers(){
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String customersSql = "SELECT * FROM CUSTOMERS";

        try{
            PreparedStatement ps = createConn().prepareStatement(customersSql);
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
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String loansSql = "SELECT * FROM LOANS";
        int borrower_id;
        String borrower_name="";
        String borrower_phone="";

        try {
            PreparedStatement ps = createConn().prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;


            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                getBorrowerSql = "SELECT * FROM CUSTOMERS WHERE ID="+borrower_id;
                pb = createConn().prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");

                }

                loans.add(new Loan("MJ/L/"+rs.getInt("loan_id"),borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        rs.getDouble("amount_borrowed"),rs.getInt("duration"),rs.getDouble("total_payment"),rs.getDouble("amount_per_month"),
                        rs.getString("due"),rs.getString("lastpay_date"),rs.getDouble("amount_paid"),rs.getDouble("last_payment"),(rs.getDouble("total_payment")-rs.getDouble("amount_paid")),rs.getString("status"),borrower_phone));
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
        ObservableList<Loan> oneLateloans = FXCollections.observableArrayList();
        String todayDate = "'"+LocalDate.now().toString()+"'";
        String compareDate = "'"+LocalDate.now().minusMonths(1).withDayOfMonth(1).minusDays(1).toString()+"'";
        String loansSql = "SELECT * FROM LOANS WHERE DUE > "+compareDate+" AND DUE < "+todayDate;
        int borrower_id;
        String borrower_name="";
        String borrower_phone="";

        try {
            PreparedStatement ps = createConn().prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                getBorrowerSql = "SELECT * FROM CUSTOMERS WHERE ID="+borrower_id;
                pb = createConn().prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");
                }

                oneLateloans.add(new Loan("MJ/L/"+rs.getInt("loan_id"),borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        rs.getDouble("amount_borrowed"),rs.getInt("duration"),rs.getDouble("total_payment"),rs.getDouble("amount_per_month"),
                        rs.getString("due"),rs.getString("lastpay_date"),rs.getDouble("amount_paid"),rs.getDouble("last_payment"),(rs.getDouble("total_payment")-rs.getDouble("amount_paid")),rs.getString("status"),borrower_phone));
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
        ObservableList<Loan> twoLateloans = FXCollections.observableArrayList();
        String lastmonthDate = "'"+LocalDate.now().minusMonths(0).withDayOfMonth(1).minusDays(1).toString()+"'";
        String compareDate = "'"+LocalDate.now().minusMonths(6).withDayOfMonth(1).minusDays(1).toString()+"'";
        String loansSql = "SELECT * FROM LOANS WHERE DUE > "+compareDate+" AND DUE < "+lastmonthDate;
        int borrower_id;
        String borrower_name="";
        String borrower_phone="";
        System.out.println(LocalDate.now().minusMonths(6).withDayOfMonth(1).minusDays(1).toString());
        System.out.println(LocalDate.now().minusMonths(0).withDayOfMonth(1).minusDays(1).toString());

        try {
            PreparedStatement ps = createConn().prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;

            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                getBorrowerSql = "SELECT * FROM CUSTOMERS WHERE ID="+borrower_id;
                pb = createConn().prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");
                }

                twoLateloans.add(new Loan("MJ/L/"+rs.getInt("loan_id"),borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        rs.getDouble("amount_borrowed"),rs.getInt("duration"),rs.getDouble("total_payment"),rs.getDouble("amount_per_month"),
                        rs.getString("due"),rs.getString("lastpay_date"),rs.getDouble("amount_paid"),rs.getDouble("last_payment"),(rs.getDouble("total_payment")-rs.getDouble("amount_paid")),rs.getString("status"),borrower_phone));
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

    public ObservableList<Loan> searchLoan(int loan_id){
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String loansSql = "SELECT * FROM LOANS WHERE LOAN_ID LIKE '%"+loan_id+"%'";
        int borrower_id;
        String borrower_name="";
        String borrower_phone="";
        try {
            PreparedStatement ps = createConn().prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;


            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                getBorrowerSql = "SELECT * FROM CUSTOMERS WHERE ID="+borrower_id;
                pb = createConn().prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");

                }

                loans.add(new Loan("MJ/L/"+rs.getInt("loan_id"),borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        rs.getDouble("amount_borrowed"),rs.getInt("duration"),rs.getDouble("total_payment"),rs.getDouble("amount_per_month"),
                        rs.getString("due"),rs.getString("lastpay_date"),rs.getDouble("amount_paid"),rs.getDouble("last_payment"),(rs.getDouble("total_payment")-rs.getDouble("amount_paid")),rs.getString("status"),borrower_phone));
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

    public ObservableList<Customer> searchCustomers(int id){
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String customersSql = "SELECT * FROM CUSTOMERS WHERE ID LIKE '%"+id+"%'";

        try{
            PreparedStatement ps = createConn().prepareStatement(customersSql);
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
        String profPhotoSql = "UPDATE CUSTOMERS SET prof_photo='"+photoName+"' WHERE ID="+id;

        try{
            PreparedStatement ps = createConn().prepareStatement(profPhotoSql);
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

    public EditCustomer editCustomer(int id){
        String editCustomerSql = "SELECT * FROM CUSTOMERS WHERE ID="+id;
        EditCustomer customer = null;

        try{
            PreparedStatement ps = createConn().prepareStatement(editCustomerSql);
            ResultSet rs = ps.executeQuery();
            if (rs.first()){
                customer = new EditCustomer(rs.getString("f_name"),rs.getString("m_name"),rs.getString("l_name"),rs.getString("gender"),
                        rs.getDate("dob"),rs.getString("phone"),rs.getString("email"),rs.getString("postal"),rs.getString("prof_photo"),
                        rs.getString("bank"),rs.getString("account_no"),rs.getString("company_name"),rs.getString("company_phone"),
                        rs.getString("company_location"),rs.getString("checksum"));
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
        String checkLoanSql = "SELECT DISTINCT TOTAL_PAYMENT,AMOUNT_PAID FROM LOANS WHERE LOAN_ID="+id;
        String updateStatusSql = "UPDATE LOANS SET status='done' WHERE LOAN_ID="+id;

        try{
            PreparedStatement ps = createConn().prepareStatement(checkLoanSql);
            ResultSet rs = ps.executeQuery();
            if(rs.first()){
                Double total_payment = rs.getDouble("total_payment");
                Double amount_paid = rs.getDouble("amount_paid");
                if(amount_paid >= total_payment){
                    PreparedStatement pb = createConn().prepareStatement(updateStatusSql);
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

    public ObservableList<Loan> loadDueLoans(){
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String loansSql = "SELECT * FROM LOANS WHERE STATUS='active'";
        int borrower_id;
        String borrower_name="";
        String borrower_phone="";

        try {
            PreparedStatement ps = createConn().prepareStatement(loansSql);
            ResultSet rs = ps.executeQuery();

            String getBorrowerSql;
            PreparedStatement pb;
            ResultSet rb;


            while(rs.next()){
                borrower_id = rs.getInt("borrower_id");
                getBorrowerSql = "SELECT * FROM CUSTOMERS WHERE ID="+borrower_id;
                pb = createConn().prepareStatement(getBorrowerSql);
                rb = pb.executeQuery();
                if (rb.first()){
                    borrower_name = rb.getString("f_name")+" "+rb.getString("l_name");
                    borrower_phone = rb.getString("phone");

                }

                loans.add(new Loan("MJ/L/"+rs.getInt("loan_id"),borrower_name,rs.getString("date_of_loan"),rs.getDouble("interest"),
                        rs.getDouble("amount_borrowed"),rs.getInt("duration"),rs.getDouble("total_payment"),rs.getDouble("amount_per_month"),
                        rs.getString("due"),rs.getString("lastpay_date"),rs.getDouble("amount_paid"),rs.getDouble("last_payment"),
                        (rs.getDouble("total_payment")-rs.getDouble("amount_paid")),rs.getString("status"),borrower_phone));
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
}
