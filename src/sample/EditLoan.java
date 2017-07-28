package sample;

import java.time.LocalDate;

/**
 * Created by Apix on 21/06/2017.
 */
public class EditLoan {
    int borrower;
    double amount_borrowed;
    int duration;
    LocalDate date_of_loan;
    double per_month;
    double total_payment;
    int member_id;
    String property_id;
    String property_name;
    String desc;
    String tolerance;

    public EditLoan(int borrower, double amount_borrowed, int duration, LocalDate date_of_loan, double per_month,double total_payment, int member_id,
                    String property_id, String property_name, String desc,String tolerance) {
        this.borrower = borrower;
        this.amount_borrowed = amount_borrowed;
        this.duration = duration;
        this.date_of_loan = date_of_loan;
        this.per_month = per_month;
        this.total_payment = total_payment;
        this.member_id = member_id;
        this.property_id = property_id;
        this.property_name = property_name;
        this.desc = desc;
        this.tolerance = tolerance;
    }
}
