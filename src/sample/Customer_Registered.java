package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by LUKATONI on 07/09/2017.
 */
public class Customer_Registered {
    public final StringProperty Cdate;
    public final StringProperty Cust_Number;
    public final IntegerProperty C_No;


    public int getC_No() {
        return C_No.get();
    }

    public IntegerProperty c_NoProperty() {
        return C_No;
    }

    public void setC_No(int c_No) {
        this.C_No.set(c_No);
    }

    public Customer_Registered(String cdate, String cust_number, Integer c_no) {
        C_No = new SimpleIntegerProperty(c_no);
        Cdate = new SimpleStringProperty(cdate);
        Cust_Number = new SimpleStringProperty(cust_number);
    }

    public String getCdate() {
        return Cdate.get();
    }

    public StringProperty cdateProperty() {
        return Cdate;
    }

    public void setCdate(String cdate) {
        this.Cdate.set(cdate);
    }

    public String getCust_Number() {
        return Cust_Number.get();
    }

    public StringProperty cust_NumberProperty() {
        return Cust_Number;
    }

    public void setCust_Number(String cust_Number) {
        this.Cust_Number.set(cust_Number);
    }
}
