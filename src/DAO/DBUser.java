package DAO;

import Utils.DBConnection;
import Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBUser {
    
    public static User getUser(String username){
        User user = null;
        try{
        //Query to and Parsing from DB
            String statement = "SELECT userId, password FROM user WHERE userName = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            ps.setString(1, username);//key mapping
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userId = rs.getInt("userId");
                String password = rs.getString("password");
                user = new User(username, password, userId);
            }
        } catch(SQLException e){
            System.out.println("Get User: " + e.getMessage());
        }
        
        return user;
    }
    
        public static ObservableList <User> getAllUsers(){
        User user = null;
        ObservableList <User> allUsers = FXCollections.observableArrayList();
        
        try{
        //Query to and Parsing from DB
            String statement = "SELECT userId, userName, password FROM user";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userId = rs.getInt("userId");
                String username = rs.getString("userName");
                String password = rs.getString("password");
                user = new User(username, password, userId);
                allUsers.add(user);
            }
        } catch(SQLException e){
            System.out.println("Get User: " + e.getMessage());
        }
        
        return allUsers;
    }
    
}
