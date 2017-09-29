package sample.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by LUKATONI on 07/09/2017.
 */
public class Repayment_Collected  {

    public final StringProperty Rdate;
    public final StringProperty Ramount;
    public final IntegerProperty R_No;

    public int getR_No() {
        return R_No.get();
    }

    public IntegerProperty r_NoProperty() {
        return R_No;
    }

    public void setR_No(int r_No) {
        this.R_No.set(r_No);
    }

    public Repayment_Collected(String Rdate, String Ramount, Integer r_no) {
        R_No = new SimpleIntegerProperty(r_no);
        this.Rdate =  new SimpleStringProperty(Rdate);
        this.Ramount =  new SimpleStringProperty(Ramount);
    }

    public String getRdate() {
        return Rdate.get();
    }

    public StringProperty rdateProperty() {
        return Rdate;
    }

    public void setRdate(String rdate) {
        this.Rdate.set(rdate);
    }

    public String getRamount() {
        return Ramount.get();
    }

    public StringProperty ramountProperty() {
        return Ramount;
    }

    public void setRamount(String ramount) {
        this.Ramount.set(ramount);
    }
}
