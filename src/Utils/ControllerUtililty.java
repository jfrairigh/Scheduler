package Utils;

import DAO.DBCustomer;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerUtililty {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    
    public void initializeCustTable(TableColumn id, TableColumn name, TableColumn city,TableColumn phone){
        id.setCellValueFactory(new PropertyValueFactory<>("custId"));       
        name.setCellValueFactory(new PropertyValueFactory<>("custName"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }
    
    public void initializeAppointmentTable(TableColumn consultant, TableColumn customer, TableColumn date, TableColumn startTime, TableColumn endTime){
        consultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));       
        customer.setCellValueFactory(new PropertyValueFactory<>("custName"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("end"));
    }
    
    public void stageBuilder(ActionEvent event, String resourceFile){  
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(resourceFile));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println("Stage Builder Error:" + ex.getMessage());
        }
    }

    public void alertError(String title, String content){   
        // Checking to see if requested time is with business hours. If it isn't then it will trigger an alert message.
            alert.setTitle(title);
            alert.setContentText(content);
            alert.setHeight(200);
            alert.showAndWait();
    }
    
    public boolean alertCustomerName(String customerName){
        //Checking to see if customer entered matches a customer in the database. If it doesn't then it will trigger an alert message.
        if(DBCustomer.getCustomer(customerName) == null){
            alert.setTitle("Customer was not found.");
            alert.setContentText("Customer was not found. This is either because the customer's name was spelled incorrectly"+  
                                ",or the customer has not been added to the system. To add the customer to the system "+
                                "please go to the add customer page.");
            alert.setResizable(true);
            alert.showAndWait();
            return true;
        }
        else{
            return false;
        }
    }
}
