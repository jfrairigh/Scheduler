package Controller;

import DAO.DBAppointment;
import Utils.ControllerUtililty;
import DAO.DBUser;
import Model.User;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController implements Initializable{
    ControllerUtililty initializer = new ControllerUtililty();
    ResourceBundle rb2 = ResourceBundle.getBundle("Utils/Scheduler", Locale.getDefault()); // The text on this screen is available in English and French.
    
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label userLoginLabel;
    @FXML
    private Label errorMessage;
    @FXML
    private TextField userText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button submitButton;
    
    @FXML
    public void handleSubmit(ActionEvent event) throws IOException{
        String username = userText.getText();
        String password = passwordText.getText();

        User user = DBUser.getUser(username);
        if(user == null)
            errorMessage.setText(rb2.getString("invalidUserName"));
        else if(!(password.equals(user.getPassword())))
            errorMessage.setText(rb2.getString("matchNotFound"));
        else{
            // Notifies user if they have an appointment within the next 15 minutes
            Long appointmentCount = DBAppointment.getAptsByUserTime(ZonedDateTime.now(), ZonedDateTime.now().plusMinutes(15), user.getUserId())
                                    .stream().count();
            if(appointmentCount > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setResizable(true);
                alert.setTitle(rb2.getString("approachingAppointment"));
                alert.setContentText(rb2.getString("approachingAptMessage"));
                alert.showAndWait();
            }
 
            initializer.stageBuilder(event, "/View/Main.fxml"); // takes user to Main screen
            
            //Records timestamp and username for every log-in to the system
            try(FileWriter fw = new FileWriter("src/Utils/ActivityLog.txt", true);
                PrintWriter activityLog = new PrintWriter(fw);){
                activityLog.println("User, " + username + ", logged into system at " + LocalDateTime.now() + ".");
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userLoginLabel.setText(rb2.getString("userLogin"));
        usernameLabel.setText(rb2.getString("username"));
        passwordLabel.setText(rb2.getString("password"));
        submitButton.setText(rb2.getString("submit"));
    }    
}
