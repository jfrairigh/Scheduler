package Controller;

import DAO.DBAppointment;
import Utils.ControllerUtililty;
import DAO.DBUser;
import Model.Appointment;
import Model.User;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UserScheduleController implements Initializable {
    ControllerUtililty initializer = new ControllerUtililty();
    int userId;
    
    //Appointment Table
    @FXML
    private TableView<Appointment> aptTable;    
    @FXML
    private TableColumn<Appointment, String> consultant; 
    @FXML
    private TableColumn<Appointment, String> customer;
    @FXML
    private TableColumn<Appointment, LocalDate> date;
    @FXML
    private TableColumn<Appointment, LocalTime> startTime;
    @FXML
    private TableColumn<Appointment, LocalTime> endTime;
    @FXML
    private ComboBox<User> comboUser;
    
    @FXML
    public void handleComboUser(ActionEvent event){
        userId = comboUser.getValue().getUserId();
        aptTable.setItems(DBAppointment.getAptsByUser(userId));
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
    }
    
    @FXML
    public void handleMainMenu(ActionEvent event){
        initializer.stageBuilder(event, "/View/Main.fxml");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<User> allUsers = DBUser.getAllUsers();
        comboUser.setItems(allUsers);
        comboUser.setValue(allUsers.get(0));
        
        userId = comboUser.getValue().getUserId();
        aptTable.setItems(DBAppointment.getAptsByUser(userId));
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
    }    
    
}
