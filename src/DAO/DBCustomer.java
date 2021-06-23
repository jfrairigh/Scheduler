package DAO;

import Utils.DBConnection;
import Model.Customer;
//import com.mysql.jdbc.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;


public class DBCustomer {
    private static final String STREET2 = "Not Applicable";
    private static final String CREATED_BY = "admin";       
    private static final String LAST_UPDATED_BY = "admin";    
    private static Customer mappingCustomer(ResultSet rs){
        Customer custResult = null;
        try{
            int custId = rs.getInt("customerId");
            int addId = rs.getInt("addressId");
            int cityId = rs.getInt("cityId");
            String custName = rs.getString("customerName");
            String street = rs.getString("address");
            String zip = rs.getString("postalCode");
            String phone = rs.getString("phone");
            String city = rs.getString("city");
            String country = rs.getString("country");
            custResult = new Customer(custId, addId, cityId, custName, street, zip, phone, city, country);
        } catch(SQLException e){
            System.out.println("Mapping Customer: " + e.getMessage());
        }
        return custResult;
    }
    
    private static int addAddress(String str, String zip, String phn, int ctyId){
        int addId = 0;
        int lastSpace = str.lastIndexOf(" ");
        String street = str.substring(0, lastSpace+3).toLowerCase();
        try{
        //Checking if address already exists in database
            String statement = "SELECT addressId, address, cityId, postalCode, phone FROM address WHERE lower(address) LIKE '" + street + "%'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);           
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(rs.getInt("cityId") == ctyId && rs.getString("postalCode").equals(zip) && rs.getString("phone").equals(phn)){
                    return rs.getInt("addressId");
                }
            }
        //Adding Address
            String createDate = LocalDateTime.now().toString().replace("T", " ");
            statement = "INSERT INTO address VALUES(null,?,?,?,?,?,?,?,null,?)";
            ps = DBConnection.getConnection().prepareStatement(statement);
            
            //Key Mapping
            ps.setString(1, str);
            ps.setString(2, STREET2);
            ps.setInt(3, ctyId);
            ps.setString(4, zip);
            ps.setString(5, phn);
            ps.setString(6, createDate);
            ps.setString(7, CREATED_BY);
            ps.setString(8, LAST_UPDATED_BY);
            
            ps.execute();
            
            // Get addressId
            rs = ps.getGeneratedKeys();
            rs.next();
            addId = rs.getInt(1);     
        }catch(SQLException e){
            System.out.println("Adding Address: " + e.getMessage());
        }
        return addId;
    }
    
    public static void addCustomer(String custName, String str, String zip, String phn, int ctyId){
        String createDate = LocalDateTime.now().toString().replace("T", " ");
        int addId = addAddress(str, zip, phn, ctyId);
        String customerName = custName.toLowerCase();
        try {
        //Checking if customer already exists in database
            String statement = "SELECT customerId, customerName, addressId FROM customer WHERE lower(customerName) = '" + customerName + "'";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);           
            ResultSet rs = ps.executeQuery();
            if(rs.next() && rs.getInt("addressId") == addId){} 
            else{
                //Adding Customer
                statement = "INSERT INTO customer VALUES(null, ?, ?, 1, ?, ?, null, ?)";
                ps = DBConnection.getConnection().prepareStatement(statement);

                //Key Mapping
                ps.setString(1,custName);
                ps.setInt(2,addId);
                ps.setString(3, createDate);
                ps.setString(4,CREATED_BY);
                ps.setString(5,LAST_UPDATED_BY);

                ps.execute();
            }
        } catch (SQLException e) {
            System.out.println("Add Customer: " + e.getMessage());
        }
    }    
    
    public static Customer getCustomer(String custName){
        Customer custResult = null;
        ResultSet rs = null;
        try{
            //Prepare Query Statement
            String statement = "SELECT customerId, address.addressId,city.cityId, customerName, address, postalCode, phone, city, country " +
                    "FROM customer JOIN address ON customer.addressId = address.addressId JOIN city ON address.cityId = city.cityId JOIN country ON city.countryId = country.countryId " +
                    "WHERE customerName = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            ps.setString(1, custName); //key mapping
            
            //Parse Query Result and Create new Customer
            rs = ps.executeQuery();
            if(rs.next()){
                custResult = mappingCustomer(rs);
            }
        } catch (SQLException e) {
            System.out.println("Get Customer: "+ e.getMessage());
        }
        return custResult;
    }
    
    public static ObservableList <Customer> getAllCustomers(){
        ObservableList <Customer> allCustomers = FXCollections.observableArrayList();
        try{
            //Prepare Query Statement
            String statement = "SELECT customerId, address.addressId,city.cityId, customerName, address, postalCode, phone, city, country" +
                " FROM customer JOIN address ON customer.addressId = address.addressId JOIN city ON address.cityId = city.cityId JOIN country ON city.countryId = country.countryId";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            
            //Parse Query Result and Create List of Customers
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Customer custResult = mappingCustomer(rs);
                allCustomers.add(custResult);
            }
        } catch (SQLException e) {
            System.out.println("Get All Customers: "+ e.getMessage());
        }
        return allCustomers;
    }
    
    public static boolean deleteCustomer(Customer cust){
        String statement = "DELETE FROM customer WHERE customerId = ?";
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);

            //Key Mapping
            ps.setInt(1, cust.getCustId());

            ps.execute();  
        } catch (SQLException e) {
            System.out.println("Delete Customer: " + e.getMessage());
            return false;
        }
        return true;
    }
    
    public static void updateCustomer(int custId, int addId, String custName, String str, String zip, String phn, int ctyId){
        String statement = "SELECT addressId FROM customer WHERE addressId = ?";
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            ps.setInt(1, addId);//Key Mapping
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if(count > 1){
                int newAddId = addAddress(str, zip, phn, ctyId); // add new address and get new generated key
                
            //update customer record w/ new addId and other customer changes
                statement = "UPDATE customer SET customerName = ?, addressId = ? WHERE customerId = ?";
                ps = DBConnection.getConnection().prepareStatement(statement);
                
                //key mapping
                ps.setString(1, custName);
                ps.setInt(2, newAddId);
                ps.setInt(3, custId);
                ps.execute();
            }else{
            //update address
                statement = "UPDATE address SET address = ?, cityId = ?, postalCode = ?, phone = ? WHERE customerId = ?";
                ps = DBConnection.getConnection().prepareStatement(statement);
                
                //key mapping
                ps.setString(1, str);
                ps.setInt(2, ctyId);
                ps.setString(3, zip);
                ps.setString(4, phn);
                ps.execute();
                
            //update customer record (no update needed to addId)
                statement = "UPDATE customer SET customerName = ? WHERE customerId = ?";
                ps = DBConnection.getConnection().prepareStatement(statement);
                ps.setString(1, custName);
                ps.setInt(2, custId);
                ps.execute();
            }
        }catch(SQLException e){
            System.out.println("Update Customer: " + e.getMessage());
        }
    }
}
