package sample.models;

/**
 * Created by Apix on 15/07/2017.
 */
public class Fine {
    public int id;
    public String month;
    public int year;
    public double amount;
    public double perMonth;

    public Fine(int id,String month, int year, Double amount, double perMonth) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.perMonth = perMonth;
    }
}
