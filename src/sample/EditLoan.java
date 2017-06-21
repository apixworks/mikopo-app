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
    int member_id;
    String property_id;
    String property_name;
    String desc;

    public EditLoan(int borrower, double amount_borrowed, int duration, LocalDate date_of_loan, double per_month, int member_id,
                    String property_id, String property_name, String desc) {
        this.borrower = borrower;
        this.amount_borrowed = amount_borrowed;
        this.duration = duration;
        this.date_of_loan = date_of_loan;
        this.per_month = per_month;
        this.member_id = member_id;
        this.property_id = property_id;
        this.property_name = property_name;
        this.desc = desc;
    }
}
