package sample.models;

import java.sql.Date;

/**
 * Created by Apix on 26/05/2017.
 */
public class EditCustomer {
    public String fname;
    public String mname;
    public String lname;
    public String gender;
    public Date dob;
    public String phone;
    public String email;
    public String postal;
    public String profPhoto;
    public String bank;
    public String acc_no;
    public String company_name;
    public String company_phone;
    public String company_loc;
    public String checknumber;
    public int num_loans;
    public int num_done_loans;

    public EditCustomer(String fname, String mname, String lname, String gender, Date dob, String phone, String email,
                        String postal, String profPhoto, String bank, String acc_no, String company_name, String company_phone,
                        String company_loc, String checknumber,int num_loans,int num_done_loans) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
        this.postal = postal;
        this.profPhoto = profPhoto;
        this.bank = bank;
        this.acc_no = acc_no;
        this.company_name = company_name;
        this.company_phone = company_phone;
        this.company_loc = company_loc;
        this.checknumber = checknumber;
        this.num_loans = num_loans;
        this.num_done_loans = num_done_loans;
    }
}
