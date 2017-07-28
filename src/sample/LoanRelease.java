package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by LUKATONI on 7/8/2017.
 */
public class LoanRelease {

    public final StringProperty loandate;
    public final StringProperty loanamount;
    public final IntegerProperty sno;

    public LoanRelease(String loandate, String loanamount, int sno) {
        this.sno = new SimpleIntegerProperty(sno);
        this.loandate =  new SimpleStringProperty(loandate);
        this.loanamount = new SimpleStringProperty(loanamount);
    }

    public int getSno() {
        return sno.get();
    }

    public IntegerProperty snoProperty() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno.set(sno);
    }

    public void setlDate(String loandate) {

        this.loandate.set(loandate);
    }

    public void setlAmount(String loanamount)
    {
        this.loanamount.set(loanamount);
    }

    public String getlDate() {

        return loandate.get();
    }

    public String getlAmount() {
        return loanamount.get();
    }
//string property
    public StringProperty lDateProperty()
    {
        return loandate;
    }
    public StringProperty lamountProperty()
    {

        return loanamount;
    }
}
