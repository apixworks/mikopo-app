package sample;

import javafx.beans.property.*;

/**
 * Created by Apix on 06/05/2017.
 */
public class Loan {
    public final StringProperty l_no;
    public final StringProperty l_borrower;
    public final StringProperty l_borrower_phone;
    public final StringProperty l_date;
    public final DoubleProperty interest;
    public final StringProperty amount;
    public final IntegerProperty duration;
    public final StringProperty total_pay;
    public final StringProperty per_month;
    public final StringProperty due;
    public final StringProperty last_paymonth;
    public final StringProperty amount_paid;
    public final StringProperty last_pay;
    public final StringProperty amount_rem;
    public final StringProperty status;

    public String getAmount_rem() {
        return amount_rem.get();
    }

    public StringProperty amount_remProperty() {
        return amount_rem;
    }

    public void setAmount_rem(String amount_rem) {
        this.amount_rem.set(amount_rem);
    }

    public Loan(String l_no, String l_borrower, String l_date, double interest, String amount, int duration, String total_pay,
                String per_month, String due, String last_paymonth, String amount_paid, String last_pay,String amount_rem, String status, String l_borrower_phone) {
        this.l_no = new SimpleStringProperty(l_no);
        this.l_borrower = new SimpleStringProperty(l_borrower);
        this.l_borrower_phone = new SimpleStringProperty(l_borrower_phone);
        this.l_date = new SimpleStringProperty(l_date);
        this.interest = new SimpleDoubleProperty(interest);
        this.amount = new SimpleStringProperty(amount);
        this.duration = new SimpleIntegerProperty(duration);
        this.total_pay = new SimpleStringProperty(total_pay);
        this.per_month = new SimpleStringProperty(per_month);
        this.due = new SimpleStringProperty(due);
        this.last_paymonth = new SimpleStringProperty(last_paymonth);
        this.amount_paid = new SimpleStringProperty(amount_paid);
        this.last_pay = new SimpleStringProperty(last_pay);
        this.amount_rem = new SimpleStringProperty(amount_rem);
        this.status = new SimpleStringProperty(status);
    }

    public String getL_no() {
        return l_no.get();
    }

    public StringProperty l_noProperty() {
        return l_no;
    }

    public void setL_no(String l_no) {
        this.l_no.set(l_no);
    }

    public String getL_borrower() {
        return l_borrower.get();
    }

    public StringProperty l_borrowerProperty() {
        return l_borrower;
    }

    public void setL_borrower(String l_borrower) {
        this.l_borrower.set(l_borrower);
    }

    public String getL_borrower_phone() {
        return l_borrower_phone.get();
    }

    public StringProperty l_borrower_phoneProperty() {
        return l_borrower_phone;
    }

    public void setL_borrower_phone(String l_borrower_phone) {
        this.l_borrower.set(l_borrower_phone);
    }

    public String getL_date() {
        return l_date.get();
    }

    public StringProperty l_dateProperty() {
        return l_date;
    }

    public void setL_date(String l_date) {
        this.l_date.set(l_date);
    }

    public double getInterest() {
        return interest.get();
    }

    public DoubleProperty interestProperty() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest.set(interest);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public int getDuration() {
        return duration.get();
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public String getTotal_pay() {
        return total_pay.get();
    }

    public StringProperty total_payProperty() {
        return total_pay;
    }

    public void setTotal_pay(String total_pay) {
        this.total_pay.set(total_pay);
    }

    public String getPer_month() {
        return per_month.get();
    }

    public StringProperty per_monthProperty() {
        return per_month;
    }

    public void setPer_month(String per_month) {
        this.per_month.set(per_month);
    }

    public String getDue() {
        return due.get();
    }

    public StringProperty dueProperty() {
        return due;
    }

    public void setDue(String due) {
        this.due.set(due);
    }

    public String getAmount_paid() {
        return amount_paid.get();
    }

    public StringProperty amount_paidProperty() {
        return amount_paid;
    }

    public void setAmount_paid(String amount_paid) {
        this.amount_paid.set(amount_paid);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getLast_pay() {
        return last_pay.get();
    }

    public StringProperty last_payProperty() {
        return last_pay;
    }

    public void setLast_pay(String last_pay) {
        this.last_pay.set(last_pay);
    }

    public String getLast_paymonth() {
        return last_paymonth.get();
    }

    public StringProperty last_paymonthProperty() {
        return last_paymonth;
    }

    public void setLast_paymonth(String last_paymonth) {
        this.last_paymonth.set(last_paymonth);
    }
}
