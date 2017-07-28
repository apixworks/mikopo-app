package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;
import sample.backend.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Apix on 07/05/2017.
 */
public class RepaymentFormController implements Initializable {
    @FXML public TextField repay_loanId;
    @FXML public TextField repay_name;
    @FXML public DatePicker date;
    @FXML public ChoiceBox month;
    @FXML public ChoiceBox year;
    @FXML public TextField amount;

    @FXML public TextField fine_loanId;
    @FXML public TextField fine_name;
    @FXML public DatePicker fine_date;
    @FXML public ChoiceBox fine_month;
    @FXML public TextField required_amount;
    @FXML public TextField fine_amount;
    @FXML public TextField total_amount;
    @FXML public Button fineSubmitBtn;

    JSONObject userObject;
    ObservableList<Fine> fines;
    Fine fine_r;

    int largest_amount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isAfter(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        date.setDayCellFactory(dayCellFactory);
        fine_date.setDayCellFactory(dayCellFactory);

        fine_month.valueProperty().addListener((observable1, oldValue1, newValue1) -> {
            for (Fine fine : fines){
                if(fine_month.getValue().toString().split("/")[0].equals(fine.month)){
                    fine_r = fine;
                    required_amount.setText(String.format("%,.0f", fine.perMonth));
                    fine_amount.setText(String.format("%,.0f", fine.amount));
                    total_amount.setText(String.format("%,.0f", fine.amount+fine.perMonth));
                }
            }
        });

        fine_amount.textProperty().addListener((observable, oldValue, newValue) -> {
            setTotal();
        });

        required_amount.textProperty().addListener((observable, oldValue, newValue) -> {
            setTotal();
        });

        ObservableList<String> yearOptions = FXCollections.observableArrayList("2017","2018");
        year.setItems(yearOptions);
        if(LocalDate.now().getYear() == 2017){
            year.setValue("2017");
        }else {
            year.setValue("2018");
        }

    }

    public void getLoanDetails(String loanId, String name, String perMont,int largest_amount,String due){
        ObservableList<String> monthOptions = FXCollections.observableArrayList("JANUARY","FEBRUARY","MARCH","APRIL","MAY",
                "JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER");
        month.setItems(monthOptions);
        month.setValue(due);

        repay_loanId.setText(loanId);
        repay_name.setText(name);
        amount.setText(perMont);
        date.requestFocus();

        fine_loanId.setText(loanId);
        fine_name.setText(name);

        this.largest_amount = largest_amount;

        DatabaseHandler db = new DatabaseHandler();
        fines = db.getFines(Integer.parseInt(loanId.substring(5)));
        System.out.println(fines.size());

        ObservableList<String> options = FXCollections.observableArrayList();
        for (Fine fine : fines){
            options.add(fine.month+"/"+fine.year);
        }
        if(options.size()!=0){
            fine_month.setValue(options.get(0));
            fine_month.setItems(options);
        }else{
            fine_month.setValue("none");
            fineSubmitBtn.setDisable(true);
        }

    }

    public void submitPayment(){
        String[] requireAmountString = amount.getText().split(",");
        String requiredAmounter = "";
        for(int i=0;i<requireAmountString.length;i++){
            requiredAmounter = requiredAmounter + requireAmountString[i];
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(date.getValue() == null||amount.getText().isEmpty()){
            alert.setContentText("Please fill all the fields!");
            alert.showAndWait();
        }else{
            if(Checker.isStringInt(requiredAmounter)){
                if(Integer.parseInt(requiredAmounter)<=largest_amount){
                    DatabaseHandler db = new DatabaseHandler();
                    db.loanPayment(Integer.parseInt(repay_loanId.getText().substring(5)),date.getValue(),Double.parseDouble(requiredAmounter),month.getValue().toString(),Integer.parseInt(year.getValue().toString()));
                    try {
                        Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" received payment for Loan: "+repay_loanId.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    db.checkIfLoanDone(Integer.parseInt(repay_loanId.getText().substring(5)));
                    alert.setContentText("Successful paid!");
                    alert.showAndWait();
                    Stage stage = (Stage) amount.getScene().getWindow();
                    stage.close();
                }else{
                    alert.setContentText("Amount exceeds remaining amount!");
                    alert.showAndWait();
                }
            }else{
                alert.setContentText("Please enter a valid amount!");
                alert.showAndWait();
            }

        }

    }

    public void submitFinePayment(){
        String[] fineAmountString = fine_amount.getText().split(",");
        String fineAmounter = "";
        for(int i=0;i<fineAmountString.length;i++){
            fineAmounter = fineAmounter + fineAmountString[i];
        }
        String[] requireAmountString = required_amount.getText().split(",");
        String requiredAmounter = "";
        for(int i=0;i<requireAmountString.length;i++){
            requiredAmounter = requiredAmounter + requireAmountString[i];
        }
        String[] totalAmountString = total_amount.getText().split(",");
        String totalAmounter = "";
        for(int i=0;i<totalAmountString.length;i++){
            totalAmounter = totalAmounter + totalAmountString[i];
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        if(fine_date.getValue()==null||fine_amount.getText().isEmpty()||required_amount.getText().isEmpty()){
            alert.setContentText("Please fill all the fields!");
            alert.showAndWait();
        }else{
            if(Checker.isStringInt(fineAmounter)||Checker.isStringInt(requiredAmounter)||Checker.isStringInt(totalAmounter)){
                if(Integer.parseInt(fineAmounter)<=fine_r.amount&&Integer.parseInt(requiredAmounter)<=fine_r.perMonth){
                    DatabaseHandler db = new DatabaseHandler();
                    db.loanPaymentFine(Integer.parseInt(fine_loanId.getText().substring(5)),fine_date.getValue(),Double.parseDouble(requiredAmounter),fine_month.getValue().toString().split("/")[0],Integer.parseInt(year.getValue().toString()));
                    db.payFine(Integer.parseInt(fine_loanId.getText().substring(5)),fine_date.getValue(),Double.parseDouble(fineAmounter),fine_month.getValue().toString().split("/")[0],Integer.parseInt(fine_month.getValue().toString().split("/")[1]));
                    db.reduceFine(fine_r.id,Double.parseDouble(fineAmounter));
                    try {
                        Logger.write(userObject.get("fname")+" "+userObject.get("lname")+" "+"id: MJ/U/"+userObject.get("id")+" received fine payment for Loan: "+repay_loanId.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    db.checkIfLoanDone(Integer.parseInt(fine_loanId.getText().substring(5)));
                    alert.setContentText("Successful paid!");
                    alert.showAndWait();
                    Stage stage = (Stage) fine_amount.getScene().getWindow();
                    stage.close();
                }else{
                    alert.setContentText("One of the Amounts exceed remaining amount!");
                    alert.showAndWait();
                }
            }else{
                alert.setContentText("Please enter valid amount!");
                alert.showAndWait();
            }

        }

    }


    public void setTotal(){
        try{
            String[] fineAmountString = fine_amount.getText().split(",");
            String fineAmounter = "";
            for(int i=0;i<fineAmountString.length;i++){
                fineAmounter = fineAmounter + fineAmountString[i];
            }
            String[] requireAmountString = required_amount.getText().split(",");
            String requiredAmounter = "";
            for(int i=0;i<requireAmountString.length;i++){
                requiredAmounter = requiredAmounter + requireAmountString[i];
            }
            total_amount.setText(String.format("%,.0f", Double.parseDouble(fineAmounter) + Double.parseDouble(requiredAmounter)));
        }catch (Exception e){
            total_amount.setText("");
        }

    }

    public void getUserDetails(JSONObject jsonObject){
        userObject = jsonObject;
    }


}
