package Controller;

import Utils.ControllerUtililty;
import Model.CityCountry;
import DAO.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


public class AddCustomerController implements Initializable {
    ControllerUtililty initializer = new ControllerUtililty();
    
    @FXML
    private TextField zip;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phone;
    @FXML
    private TextField street;
    @FXML
    private ComboBox<CityCountry> cityCountry;

    @FXML
    void handleAdd(ActionEvent event) {
        String name = firstName.getText() + " " + lastName.getText();
        String str = street.getText();
        String phn = phone.getText();
        String pCode = zip.getText();
        CityCountry ctyCntry = cityCountry.getValue();
        if(name.isEmpty() || str.isEmpty() || phn.isEmpty()|| pCode.isEmpty() || ctyCntry == null){
            String title = "Missing Information";
            String content = "Please make sure you have provided information for every field. All requested information is required.";
            initializer.alertError(title, content);
        } else{
            DBCustomer.addCustomer(name, str, pCode, phn, ctyCntry.getCityId());
            initializer.stageBuilder(event, "/View/Main.fxml");
        }   
    }

    @FXML
    void handleCancel(ActionEvent event) {
        initializer.stageBuilder(event, "/View/Main.fxml");
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<CityCountry> allCityCountry = DBCityCountry.getAllCityCountry();
        cityCountry.setItems(allCityCountry);
        cityCountry.setValue(allCityCountry.get(0));
    }    
    
}
