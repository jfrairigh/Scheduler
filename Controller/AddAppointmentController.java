package Controller;

import Utils.ControllerUtililty;
import DAO.DBAppointment;
import DAO.DBCustomer;
import DAO.DBUser;
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
import javafx.scene.control.TextField;


public class AddAppointmentController implements Initializable {
    ControllerUtililty initializer = new ControllerUtililty();
    
    private Boolean hour = true;
//    private String type;
    
    @FXML
    private ComboBox <Customer> custName;
    @FXML
    private TextField date;
    @FXML
    private ComboBox <User> consultant;
    @FXML
    private TextField startTime;
    @FXML
    private Label dateFormatMessage;
    @FXML
    private Label timeFormatMessage;

    @FXML
    void handleRadio30Min(ActionEvent event) {
        hour = false;
    }
    @FXML
    void handle1Hr(ActionEvent event){
        hour = true;
    }

    @FXML
    void handleAdd(ActionEvent event){
        String type;
        dateFormatMessage.setText("");
        timeFormatMessage.setText("");
        // Parsing and Converting Time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDate localStartDate = null;
        LocalTime localStartTime= null;
        try{
            localStartDate = LocalDate.parse(date.getText(), dateFormatter);
        } catch(DateTimeParseException e){
            dateFormatMessage.setText("The date should be typed in the format M-d-yyyy.");
        }
        try{
            localStartTime = LocalTime.parse(startTime.getText(), timeFormatter);
        } catch(DateTimeParseException e){
            timeFormatMessage.setText("The time should be in 24 hour time and typed in the format H:mm.");
        }
        
        if(localStartDate == null || localStartTime == null) return;
        
        LocalDateTime localDateTime = LocalDateTime.of(localStartDate, localStartTime);
        ZonedDateTime start = localDateTime.atZone(ZoneId.systemDefault());                            
        ZonedDateTime end;
        LocalTime localEndTime;
        
        if(hour){
            localEndTime = localStartTime.plusMinutes(59).plusSeconds(59);
            end = start.plusMinutes(59).plusSeconds(59);
            type = "hour";
        } else{
            localEndTime = localStartTime.plusMinutes(29).plusSeconds(59);
            end = start.plusMinutes(29).plusSeconds(59);
            type = "half hour";
        }
            
        String customerName = custName.getValue().getCustName(); 
        int userId = consultant.getValue().getUserId();
        
        //Validating Entires
        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(17, 0);
        if(localStartTime.isBefore(open) || localEndTime.isAfter(close)){
            String title = "Outside of Business Hours";
            String content = "The time you selected is outside of Business Hours. Business Hours are from 08:00 to 17:00.(8:00 AM to 5:00 PM.)";
            initializer.alertError(title, content);
        }
        else if (initializer.alertCustomerName(customerName)){}
        else if(!(DBAppointment.addAppointment(type, userId, DBCustomer.getCustomer(customerName).getCustId(), start, end))){
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
        ObservableList<User> allUsers = DBUser.getAllUsers();
        consultant.setItems(allUsers);
        consultant.setValue(allUsers.get(0));
        
        ObservableList<Customer> allCusts =  DBCustomer.getAllCustomers();
        custName.setItems(allCusts);
        custName.setValue(allCusts.get(0));
    }    
    
}
