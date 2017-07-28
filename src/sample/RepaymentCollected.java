package sample;

import javafx.beans.property.StringProperty;

/**
 * Created by LUKATONI on 7/8/2017.
 */
public class RepaymentCollected {
    private final StringProperty date;
    private final StringProperty amount;

    public RepaymentCollected(StringProperty date, StringProperty amount) {
        this.date = date;
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getDate() {

        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }
}
