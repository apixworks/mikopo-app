package sample.models;

/**
 * Created by Apix on 15/06/2017.
 */
public class Transaction {
    public String c_id;
    public String l_id;
    public String name;
    public Double amount;
    public String date;

    public Transaction(String c_id, String l_id, String name, Double amount, String date) {
        this.c_id = c_id;
        this.l_id = l_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }
}
