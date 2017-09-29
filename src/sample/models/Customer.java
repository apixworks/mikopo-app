package sample.models;

import javafx.beans.property.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Apix on 03/05/2017.
 */
public class Customer {
    public final StringProperty c_no;
    public final StringProperty name;
    public final StringProperty phone;
    public final StringProperty email;
    public final StringProperty bank;
    public final StringProperty account_no;
    public final StringProperty company_name;
    public final StringProperty company_phone;
    public final StringProperty checksum;

    public Customer(String c_no, String name, String phone, String email, String bank, String account_no, String company_name,
                    String company_phone, String checksum) {
        this.c_no = new SimpleStringProperty(c_no);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.bank = new SimpleStringProperty(bank);
        this.account_no = new SimpleStringProperty(account_no);
        this.company_name = new SimpleStringProperty(company_name);
        this.company_phone = new SimpleStringProperty(company_phone);
        this.checksum = new SimpleStringProperty(checksum);
    }

    public void setC_no(String c_no) {
        this.c_no.set(c_no);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setBank(String bank) {
        this.bank.set(bank);
    }

    public void setAccount_no(String account_no) {
        this.account_no.set(account_no);
    }

    public void setCompany_name(String company_name) {
        this.company_name.set(company_name);
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone.set(company_phone);
    }

    public void setChecksum(String checksum) {
        this.checksum.set(checksum);
    }

    public String getC_no() {
        return c_no.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getBank() {
        return bank.get();
    }

    public String getAccount_no() {
        return account_no.get();
    }

    public String getCompany_name() {
        return company_name.get();
    }

    public String getCompany_phone() {
        return company_phone.get();
    }

    public String getChecksum() {
        return checksum.get();
    }
}
