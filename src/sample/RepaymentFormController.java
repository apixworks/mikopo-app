package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Checker;
import sample.Logger;
import sample.backend.DatabaseHandler;
import sample.models.Fine;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Apix on 07/05/2017.
 */
public class RepaymentFormController implements Initializable {
    @FXML public TextField repay_loanId;
    @FXML public TextField repay_name;
    @FXML public DatePicker date;
    @FXML public ChoiceBox<String> month;
//    @FXML public ChoiceBox<String> year;
    @FXML public TextField amount;
    @FXML public Button submitBtn;

    @FXML public TextField fine_loanId;
    @FXML public TextField fine_name;
    @FXML public DatePicker fine_date;
    @FXML public ChoiceBox<String> fine_month;
    @FXML public TextField required_amount;
    @FXML public TextField fine_amount;
    @FXML public TextField total_amount;
    @FXML public Button fineSubmitBtn;
    @FXML public Button delBtn;

    JSONObject userObject;
    ObservableList<Fine> fines;
    Fine fine_r;

    int largest_amount;

    FXMLLoader fxmlLoader;

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
                if(fine_month.getValue().split("/")[0].equals(fine.month) && fine_month.getValue().split("/")[1].equals(String.valueOf(fine.year))){
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

//        ObservableList<String> yearOptions = FXCollections.observableArrayList("2017","2018");
//        year.setItems(yearOptions);
//        if(LocalDate.now().getYear() == 2017){
//            year.setValue("2017");
//        }else {
//            year.setValue("2018");
//        }

        month.valueProperty().addListener((observable, oldValue, newValue)-> {
            setMonthAmount();
        });

    }

    public void setMonthAmount(){
        String month_string = month.getValue().split("/")[0];
        String year_string = month.getValue().split("/")[1];

        DatabaseHandler db = new DatabaseHandler();
        String amount_rem = db.getMonthAmount(Integer.parseInt(repay_loanId.getText().substring(5)),month_string,Integer.parseInt(year_string));
        amount.setText(amount_rem);
    }

    public void editFine(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to edit this fine?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            for (Fine fine : fines){
                if(fine_month.getValue().split("/")[0].equals(fine.month) && fine_month.getValue().split("/")[1].equals(String.valueOf(fine.year))){
                    int pos = fines.indexOf(fine);
                    fines.remove(fine);
                    Fine e_fine = new Fine(fine.id,fine.month,fine.year,fine.amount,fine.perMonth);
                    String[] fineAmountString = fine_amount.getText().split(",");
                    String fineAmounter = "";
                    for(int i=0;i<fineAmountString.length;i++){
                        fineAmounter = fineAmounter + fineAmountString[i];
                    }
                    e_fine.amount = Double.parseDouble(fineAmounter);
                    fines.add(pos,e_fine);
                    DatabaseHandler db = new DatabaseHandler();
                    if(db.editFine(fine.id,e_fine.amount)){
                        Alert i_alert = new Alert(Alert.AlertType.INFORMATION);
                        i_alert.setTitle("Information Dialog");
                        i_alert.setHeaderText(null);
                        i_alert.setContentText("Fine edited!");
                        i_alert.showAndWait();
//                        ViewRepaymentsController viewRepaymentsController = fxmlLoader.getController();
//                        viewRepaymentsController.initializeDue();
//                        viewRepaymentsController.initializeOneMonthLate();
//                        viewRepaymentsController.initializeTwoMonthLate();
//                        viewRepaymentsController.initializePenaltyLate();
                    }else{
                        Alert i_alert = new Alert(Alert.AlertType.WARNING);
                        i_alert.setTitle("Information Dialog");
                        i_alert.setHeaderText(null);
                        i_alert.setContentText("Fine not edited!");
                        i_alert.showAndWait();
                    }
                    ObservableList<String> options = FXCollections.observableArrayList();
                    for (Fine fine_n : fines){
                        options.add(fine_n.month+"/"+fine_n.year);
                    }
                    if(options.size()!=0){
                        fine_month.setItems(options);
                        fine_month.setValue(options.get(pos));
                    }else{
                        fine_month.setItems(null);
                        fine_month.setValue(null);
                        required_amount.setText("");
                        fine_amount.setText("");
                        total_amount.setText("");
                        fineSubmitBtn.setDisable(true);
                    }
                }
            }
        }
    }

    public void deleteFine(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this fine?");
        Optional<ButtonType> result =alert.showAndWait();
        if(result.get() == ButtonType.OK){
            for (Fine fine : fines){
                if(fine_month.getValue().toString().split("/")[0].equals(fine.month) && fine_month.getValue().toString().split("/")[1].equals(String.valueOf(fine.year))){
                    fines.remove(fine);
                    DatabaseHandler db = new DatabaseHandler();
                    if(db.deleteFine(fine.id)){
                        Alert i_alert = new Alert(Alert.AlertType.INFORMATION);
                        i_alert.setTitle("Information Dialog");
                        i_alert.setHeaderText(null);
                        i_alert.setContentText("Fine deleted!");
                        i_alert.showAndWait();
                    }else{
                        Alert i_alert = new Alert(Alert.AlertType.WARNING);
                        i_alert.setTitle("Information Dialog");
                        i_alert.setHeaderText(null);
                        i_alert.setContentText("Fine not deleted!");
                        i_alert.showAndWait();
                    }
                    ObservableList<String> options = FXCollections.observableArrayList();
                    for (Fine fine_n : fines){
                        options.add(fine_n.month+"/"+fine_n.year);
                    }
                    if(options.size()!=0){
                        fine_month.setItems(options);
                        fine_month.setValue(options.get(0));
                    }else{
                        fine_month.setItems(null);
                        fine_month.setValue(null);
                        required_amount.setText("");
                        fine_amount.setText("");
                        total_amount.setText("");
                        fineSubmitBtn.setDisable(true);
                    }
                }
            }
        }
    }

    public void getLoanDetails(String loanId, String name, String perMont,int largest_amount,String due,String release_date,
                               int duration,String status,FXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
//        ObservableList<String> monthOptions = FXCollections.observableArrayList("JANUARY","FEBRUARY","MARCH","APRIL","MAY",
//                "JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER");

        ObservableList<String> monthOptions = FXCollections.observableArrayList();
        System.out.println(release_date);
        String[] rel_date = release_date.split("-");
        int yr = Integer.parseInt(rel_date[0]);
        int mon = Integer.parseInt(rel_date[1]);
        int day = Integer.parseInt(rel_date[2]);
        LocalDate r_date = LocalDate.of(yr,mon,day);
        for (int i = 0; i < duration; i++) {
            if(r_date.getDayOfMonth()<18){
                monthOptions.add(r_date.plusMonths(i+1).withDayOfMonth(1).minusDays(1).getMonth().toString().concat("/"+
                        r_date.plusMonths(i+1).withDayOfMonth(1).minusDays(1).getYear()));
            }else{
                monthOptions.add(r_date.plusMonths(i+2).withDayOfMonth(1).minusDays(1).getMonth().toString().concat("/"+
                        r_date.plusMonths(i+2).withDayOfMonth(1).minusDays(1).getYear()));
            }
        }
        DatabaseHandler db = new DatabaseHandler();
        String lastPay_month = db.getLastPayMonth(Integer.parseInt(loanId.substring(5)));
        int pos = 0;
        for (int i = 0; i < monthOptions.size(); i++) {
            if(lastPay_month.equals(monthOptions.get(i))){
                pos = i;
            }
        }
        month.setItems(monthOptions);
        month.setValue(monthOptions.get(pos));

        repay_loanId.setText(loanId);
        repay_name.setText(name);
        String month_string = month.getValue().split("/")[0];
        String year_string = month.getValue().split("/")[1];
        String amount_rem = db.getMonthAmount(Integer.parseInt(repay_loanId.getText().substring(5)),month_string,Integer.parseInt(year_string));
        amount.setText(amount_rem);
        date.requestFocus();

        fine_loanId.setText(loanId);
        fine_name.setText(name);

        this.largest_amount = largest_amount;

        fines = db.getFines(Integer.parseInt(loanId.substring(5)));
        System.out.println("fine number "+fines.size());

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

        if(status.equals("done")){
            submitBtn.setDisable(true);
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
            if(Checker.isStringInt(requiredAmounter)&&!amount.getText().equals("0")&&!amount.getText().equals("0.0")){
                if(Integer.parseInt(requiredAmounter)<=largest_amount){
                    DatabaseHandler db = new DatabaseHandler();
                    db.loanPayment(Integer.parseInt(repay_loanId.getText().substring(5)),date.getValue(),
                            Double.parseDouble(requiredAmounter),month.getValue().split("/")[0],Integer.parseInt(month.getValue().split("/")[1]));
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
                    if(!required_amount.getText().equals("0")||!required_amount.getText().isEmpty()){
                        db.loanPaymentFine(Integer.parseInt(fine_loanId.getText().substring(5)),fine_date.getValue(),
                                Double.parseDouble(requiredAmounter),fine_month.getValue().split("/")[0],
                                Integer.parseInt(fine_month.getValue().split("/")[1]));
                    }
                    db.payFine(Integer.parseInt(fine_loanId.getText().substring(5)),fine_date.getValue(),
                            Double.parseDouble(fineAmounter),fine_month.getValue().split("/")[0],
                            Integer.parseInt(fine_month.getValue().split("/")[1]));
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

    public void getUserDetails(JSONObject jsonObject) throws JSONException {
        userObject = jsonObject;
        if(userObject.getString("role").equals("main admin") || userObject.getString("role").equals("admin")){
            delBtn.setDisable(false);
        }else{
            delBtn.setDisable(true);
        }
    }


}
