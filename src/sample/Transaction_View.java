package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Created by LUKATONI on 5/26/2017.
 */
public class Transaction_View {
    public final StringProperty sno;
    public final StringProperty loan_no;
    public final StringProperty name;
    public final StringProperty tamount;
    public final StringProperty date;

    public Transaction_View(String sno,String loan_no,String name,String tamount,String date){
        this.sno=new SimpleStringProperty(sno);
        this.loan_no=new SimpleStringProperty(loan_no);
        this.name=new SimpleStringProperty(name);
        this.tamount=new SimpleStringProperty(tamount);
        this.date=new SimpleStringProperty(date);
    }

    public void setSno(String sno) {
        this.sno.set(sno);
    }

    public void settAmount(String amount) {
        this.tamount.set(amount);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setLoan_no(String loan_no) {
        this.loan_no.set(loan_no);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSno() {
        return sno.get();
    }

    public String getLoan_no() {
        return loan_no.get();
    }

    public String getName() {
        return name.get();
    }

    public String getAmount() {
        return tamount.get();
    }

    public String getDate() {
        return date.get();
    }
}
