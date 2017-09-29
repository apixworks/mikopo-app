package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import sample.backend.DatabaseHandler;
import sample.models.Transaction;

/**
 * Created by Apix on 15/06/2017.
 */
public class UserTransactionController {
    @FXML public GridPane gridPane;
    @FXML public GridPane gridPane1;
    @FXML public Label name;
    @FXML public Label c_id;
    @FXML public Label l_id;

    ObservableList<Transaction> transactions;
    ObservableList<Transaction> fine_transactions;

    public void setUp(int id){
        DatabaseHandler db = new DatabaseHandler();
        transactions = db.getUserTransactions(id);
        fine_transactions = db.getUserFineTransactions(id);
        name.setText(transactions.get(0).name);
        c_id.setText(transactions.get(0).c_id);
        l_id.setText(transactions.get(0).l_id);

        for(int r=0;r<transactions.size();r++){
            for(int c=0;c<2;c++){
                if(c==0)
                    gridPane.add(new Label(transactions.get(r).date),c,r+1);
                else
                    gridPane.add(new Label(String.format("%,.0f", transactions.get(r).amount)),c,r+1);
            }
        }

        if(fine_transactions.size()>0){
            for(int r=0;r<fine_transactions.size();r++){
                for(int c=0;c<2;c++){
                    if(c==0)
                        gridPane1.add(new Label(fine_transactions.get(r).date),c,r+1);
                    else
                        gridPane1.add(new Label(String.format("%,.0f", fine_transactions.get(r).amount)),c,r+1);
                }
            }
        }

    }
}
