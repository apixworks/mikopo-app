package sample;

/**
 * Created by Apix on 15/06/2017.
 */
public class Transaction {
    String c_id;
    String l_id;
    String name;
    Double amount;
    String date;

    public Transaction(String c_id, String l_id, String name, Double amount, String date) {
        this.c_id = c_id;
        this.l_id = l_id;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }
}
