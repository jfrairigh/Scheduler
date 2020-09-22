package Controller;

import Utils.ControllerUtililty;
import DAO.DBCityCountry;
import DAO.DBCustomer;
import Model.CityCountry;
import Model.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class UpdateCustomerController implements Initializable{
    ControllerUtililty initializer = new ControllerUtililty();
    Customer cust = null;
    
    @FXML
    private TextField name;
    @FXML
    private TextField street;
    @FXML
    private TextField zip;
    @FXML
    private TextField phone;
    @FXML
    private ComboBox<CityCountry> cityCountry;
    
    public void sendCustomer(Customer cust){
        this.cust = cust;
        name.setText(String.valueOf(cust.getCustName()));
        street.setText(String.valueOf(cust.getStreet()));
        zip.setText(String.valueOf(cust.getZip()));
        phone.setText(String.valueOf(cust.getPhone()));
        cityCountry.setValue(DBCityCountry.getCityCountry(cust.getCityId()));
    }
    @FXML
    void handleUpdateCust(ActionEvent event) {
        CityCountry ctyCntry = cityCountry.getValue();
        String custName = name.getText();
        String str = street.getText();
        String phn = phone.getText();
        String pCode = zip.getText();
        if(custName.isEmpty() || str.isEmpty() || phn.isEmpty()|| pCode.isEmpty() || ctyCntry == null){
            String title = "Missing Information";
            String content = "Please make sure you have provided information for every field. All requested information is required.";
            initializer.alertError(title, content);
        } else{
            DBCustomer.updateCustomer(cust.getCustId(), cust.getAddId(), custName, str, pCode, phn, ctyCntry.getCityId());
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
        
    }    
}
