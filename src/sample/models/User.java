package sample.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Apix on 12/07/2017.
 */
public class User {
    public final StringProperty user_no;
    public final StringProperty name;
    public final StringProperty status;
    public final StringProperty role;

    public User(String user_no, String name, String status,String role) {
        this.user_no = new SimpleStringProperty(user_no);
        this.name = new SimpleStringProperty(name);
        this.status = new SimpleStringProperty(status);
        this.role = new SimpleStringProperty(role);
    }

    public String getUser_no() {
        return user_no.get();
    }

    public StringProperty user_noProperty() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no.set(user_no);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

}
