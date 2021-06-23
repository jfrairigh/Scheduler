package DAO;

import Utils.DBConnection;
import Model.CityCountry;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBCityCountry {
    
    public static ObservableList <CityCountry> getAllCityCountry(){
        ObservableList <CityCountry> allCityCountry = FXCollections.observableArrayList();
        try{
        String statement = "SELECT city, cityId, country, country.countryId FROM city, country WHERE city.countryId = country.countryId";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String city = rs.getString("city");
            int cityId = rs.getInt("cityId");
            String country = rs.getString("country");
            int countryId = rs.getInt("countryId");
            CityCountry cityCountry = new CityCountry(city, country, cityId, countryId);
            allCityCountry.add(cityCountry);
        }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allCityCountry;
    }
    
    public static CityCountry getCityCountry(int cityId){
        CityCountry cityCountry = null;
        try{
            String statement = "SELECT city, country, country.countryId FROM city, country WHERE city.countryId = country.countryId AND city.cityId = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            ps.setInt(1, cityId); //key mapping
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String city = rs.getString("city");
                String country = rs.getString("country");
                int countryId = rs.getInt("countryId");
                cityCountry = new CityCountry(city, country, cityId, countryId);
        }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return cityCountry;
    }
    
}
