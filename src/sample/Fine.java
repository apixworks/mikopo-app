package sample;

/**
 * Created by Apix on 15/07/2017.
 */
public class Fine {
    int id;
    String month;
    int year;
    double amount;
    double perMonth;

    public Fine(int id,String month, int year, Double amount, double perMonth) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.perMonth = perMonth;
    }
}
