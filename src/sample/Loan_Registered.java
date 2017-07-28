package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by LUKATONI on 07/09/2017.
 */
public class Loan_Registered {

    public final StringProperty Loan_Reg_Date;
    public final StringProperty loan_total;
    public final IntegerProperty LR_No;

    public int getLR_No() {
        return LR_No.get();
    }

    public IntegerProperty LR_NoProperty() {
        return LR_No;
    }

    public void setLR_No(int LR_No) {
        this.LR_No.set(LR_No);
    }

    public Loan_Registered(String loan_reg_date, String loan_total, Integer lr_no) {
        LR_No = new SimpleIntegerProperty(lr_no);
        Loan_Reg_Date = new SimpleStringProperty(loan_reg_date);
        this.loan_total = new SimpleStringProperty(loan_total);
    }

    public String getLoan_Reg_Date() {
        return Loan_Reg_Date.get();
    }

    public StringProperty loan_Reg_DateProperty() {
        return Loan_Reg_Date;
    }

    public void setLoan_Reg_Date(String loan_Reg_Date) {
        this.Loan_Reg_Date.set(loan_Reg_Date);
    }

    public String getLoan_total() {
        return loan_total.get();
    }

    public StringProperty loan_totalProperty() {
        return loan_total;
    }

    public void setLoan_total(String loan_total) {
        this.loan_total.set(loan_total);
    }
}
