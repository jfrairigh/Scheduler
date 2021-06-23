package Controller;

import DAO.DBAppointment;
import Utils.ControllerUtililty;
import DAO.DBCustomer;
import Model.Appointment;
import Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MainController implements Initializable {
    ControllerUtililty initializer = new ControllerUtililty();
    
// Customer Table 
    @FXML
    private TableView<Customer> custTable;    
    @FXML
    private TableColumn<Customer, Integer> custId; 
    @FXML
    private TableColumn<Customer, String> city;
    @FXML
    private TableColumn<Customer, String> custName;
    @FXML
    private TableColumn<Customer, String> phone;
    
    @FXML
    private void handleAddCust(ActionEvent event){
        initializer.stageBuilder(event, "/View/AddCustomer.fxml");
    }
    
    @FXML
    private void handleModifyCust(ActionEvent event){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/UpdateCustomer.fxml"));
        loader.load();
        
        UpdateCustomerController UCC = loader.getController();
        UCC.sendCustomer(custTable.getSelectionModel().getSelectedItem());
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent parent = loader.getRoot();
        stage.setScene(new Scene(parent));
        stage.show();
        } catch(IOException e){
            System.out.println("Handle Modify Customer: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDeleteCust(ActionEvent e){
        if(DBCustomer.deleteCustomer(custTable.getSelectionModel().getSelectedItem())){
            custTable.setItems(DBCustomer.getAllCustomers());
            initializer.initializeCustTable(custId, custName, city, phone);
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setResizable(true);
            alert.setTitle("Unable to Delete Customer");
            alert.setContentText("We are unable to delete the customer you selected. This may be because the customer has an appointment scheduled. " + 
                                "A customer can only be deleted if they do not have an appointment sheduled.");
            alert.showAndWait();
        }
        
    }
    
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
    private void handleViewByAll(ActionEvent event){
        aptTable.setItems(DBAppointment.getAllAppointments());
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
    }
    @FXML
    private void handleViewByMonth(ActionEvent event){
        aptTable.setItems(DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusDays(30)));
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
    }
    @FXML
    private void handleViewByWeek(ActionEvent event){
        aptTable.setItems(DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusDays(7)));
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
    }
    
    @FXML
    private void handleDeleteApt(ActionEvent e){
        DBAppointment.deleteAppointment(aptTable.getSelectionModel().getSelectedItem());
        aptTable.setItems(DBAppointment.getAllAppointments());
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
    }
    @FXML
    private void handleAddApt(ActionEvent e){
        initializer.stageBuilder(e, "/View/AddAppointment.fxml");
    }
    @FXML
    private void handleModifyApt(ActionEvent event){
        try{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/UpdateAppointment.fxml"));
        loader.load();
        
        UpdateAppointmentController UAC = loader.getController();
        UAC.sendAppointment(aptTable.getSelectionModel().getSelectedItem());
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent parent = loader.getRoot();
        stage.setScene(new Scene(parent));
        stage.show();
        } catch(IOException e){
            System.out.println("Handle Modify Customer: " + e.getMessage());
        }
    }
    
//Reports
    @FXML
    private ChoiceBox reportChoices;
    @FXML
    private Label typeCounter;
    
    @FXML
    public void generateReport(ActionEvent event){
        
        //Stream weekApts = DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusWeeks(1)).stream();
        
        int chosenReport = reportChoices.getSelectionModel().getSelectedIndex();
        switch (chosenReport) {
            case 0:
                long monthAptsOfHour = DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusMonths(1)).stream() // This line creates stream of appointments this month. The stream makes the code shorter and clearer.
                        .filter(i->i.getType().equals("hour")).count();                                                      // This line counts the appointment that are a hour in length. Lambda Expression Reason: The lambda allowed me to pass a predicate to the filter method in a very concise way. 
                long monthAptsOfHalfHour = DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusMonths(1)).stream() // This line creates stream of appointments this month. The stream makes the code shorter and clearer.
                        .filter(i->i.getType().equals("half hour")).count();                                                 // This line counts the appointment that are a half hour in length. Lambda Expression Reason: The lambda allowed me to pass a predicate to the filter method in a very concise way.
                typeCounter.setText("There are " + monthAptsOfHalfHour + " half hour and " + monthAptsOfHour + " hour appointments remaining this month.");
                break;
            case 1:
                long weekAptsOfHour = DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusWeeks(1)).stream()
                        .filter(i->i.getType().equals("hour")).count();
                long weekAptsOfHalfHour = DBAppointment.getAppointmentsByTime(ZonedDateTime.now(), ZonedDateTime.now().plusWeeks(1)).stream()
                        .filter(i->i.getType().equals("half hour")).count();
                typeCounter.setText("There are " + weekAptsOfHalfHour + " half hour and " + weekAptsOfHour + " hour appointments remaining this month.");
                break;
            case 2:
                initializer.stageBuilder(event, "/View/UserSchedule.fxml");
                break;
            default:
                break;
        }
    }
    
    @FXML
    private void handleExit(ActionEvent event){
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Initializing Customer Table
        custTable.setItems(DBCustomer.getAllCustomers());
        initializer.initializeCustTable(custId, custName, city, phone);
    
    //Initializing Appointment Table
        aptTable.setItems(DBAppointment.getAllAppointments());
        initializer.initializeAppointmentTable(consultant, customer, date, startTime, endTime);
        
    //Initializing Choice Box
        reportChoices.setItems(FXCollections.observableArrayList("number of appointment types by month", "number of appointment types by week", "schedule for each consultant"));
        
        
    }    
    
}
