package Controller;

import DAO.DBAppointment;
import DAO.DBCustomer;
import Utils.ControllerUtililty;
import DAO.DBUser;
import Model.Appointment;
import Model.Customer;
import Model.User;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class UpdateAppointmentController implements Initializable{
    ControllerUtililty initializer = new ControllerUtililty();
    Appointment apt = null;
    boolean hr;
            
    @FXML
    private ComboBox <Customer> name;       
    @FXML
    private TextField date;
    @FXML
    private ComboBox<User> consultant;
    @FXML
    private TextField start;
    @FXML  
    private RadioButton halfHour;
    @FXML
    private RadioButton hour;
    @FXML
    private Label dateFormatMessage;
    @FXML
    private Label timeFormatMessage;
    
    @FXML
    void handle1hr(ActionEvent event) {
         hr = true;
    }

    @FXML
    void handle30Min(ActionEvent event) {
        hr = false;
    }
 
    public void sendAppointment(Appointment apt){
        this.apt = apt;
        name.setValue(DBCustomer.getCustomer(apt.getCustName()));
        date.setText(String.valueOf(apt.getDate()));
        consultant.setValue(DBUser.getUser(apt.getConsultant()));
        start.setText(String.valueOf(apt.getStart()));
        
        if(apt.getType().equals("hour")){
            hour.setSelected(true);
            //hr = true;
        }
        else{
            halfHour.setSelected(true);
            //hr = false;
        }
    }
    
    @FXML
    void handleUpdate(ActionEvent event) {
        String type;
        dateFormatMessage.setText("");
        timeFormatMessage.setText("");
        // Parsing and Converting Time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        LocalDate localStartDate = null;
        LocalTime localStartTime = null;
            try{
                localStartDate = LocalDate.parse(date.getText(), dateFormatter);
            }catch(DateTimeParseException e) {
                dateFormatMessage.setText("The date should be typed in the format M-d-yyyy.");
            }
            
            try{
             localStartTime= LocalTime.parse(start.getText(), timeFormatter);
            } catch(DateTimeParseException e){
                timeFormatMessage.setText("The time should be in 24 hour time and typed in the format H:mm.");
            }
            
            if(localStartDate == null || localStartTime == null) return;

            LocalDateTime localDateTime = LocalDateTime.of(localStartDate, localStartTime);
            ZonedDateTime startTime = localDateTime.atZone(ZoneId.systemDefault());                            
            ZonedDateTime end;
            LocalTime localEndTime;

            if(hr){
                localEndTime = localStartTime.plusMinutes(59).plusSeconds(59);
                end = startTime.plusMinutes(59).plusSeconds(59);
                type = "hour";
            } else{
                localEndTime = localStartTime.plusMinutes(29).plusSeconds(59);
                end = startTime.plusMinutes(29).plusSeconds(59);
                type = "half hour";
            }

            String customerName = name.getValue().getCustName(); 
            int userId = consultant.getValue().getUserId();

            //Validating Entires
            LocalTime open = LocalTime.of(8, 0);
            LocalTime close = LocalTime.of(17, 0);
            if(localStartTime.isBefore(open) || localEndTime.isAfter(close)){
                String title = "Outside of Business Hours";
                String content = "The time you selected is outside of Business Hours. Business Hours are from 08:00 to 17:00.(8:00 AM to 5:00 PM.)";
                initializer.alertError(title, content);
            }
            else if(initializer.alertCustomerName(customerName)){}
            else if(!(DBAppointment.updateAppointment(apt.getAptId(), userId, DBCustomer.getCustomer(customerName).getCustId(), type, startTime, end))){
                String title = "Appointment Overlap";
                String content = "The consultant you chose is not available at that time. Please choose another time or consultant.";
                initializer.alertError(title, content);
            }
            else{initializer.stageBuilder(event, "/View/Main.fxml");}
    }

    @FXML
    void handleCancel(ActionEvent event) {
        initializer.stageBuilder(event, "/View/Main.fxml");
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<User> allConsultants = DBUser.getAllUsers();
        consultant.setItems(allConsultants);
        
        ObservableList<Customer> allCustomers = DBCustomer.getAllCustomers();
        name.setItems(allCustomers);
        
        
    }    
}
