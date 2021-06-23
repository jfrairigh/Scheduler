package DAO;

import Model.Appointment;
import Utils.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBAppointment {
    
    private static Appointment mappingAppointment(ResultSet rs){
        Appointment aptResult = null;
        try{
            int aptId = rs.getInt("appointmentId");
            int custId = rs.getInt("customerId");
            String custName = rs.getString("customerName");
            String username = rs.getString("userName");
            String type = rs.getString("type");
            
            //storing UTC in Local objects
            LocalDateTime utcStart = rs.getTimestamp("start").toLocalDateTime();
            LocalDateTime utcEnd = rs.getTimestamp("end").toLocalDateTime();

            //creating Zoned DateTime objects for UTC
            ZoneId utcZoneId = ZoneId.of("UTC");
            ZonedDateTime utcZonedStart = utcStart.atZone(utcZoneId);
            ZonedDateTime utcZonedEnd = utcEnd.atZone(utcZoneId);

            //converting from UTC to system default time
            ZoneId localZoneId = ZoneId.systemDefault();
            ZonedDateTime localZonedStart = utcZonedStart.withZoneSameInstant(localZoneId);
            ZonedDateTime localZonedEnd = utcZonedEnd.withZoneSameInstant(localZoneId);

            aptResult = new Appointment(aptId, custId, username, custName, localZonedStart, localZonedEnd, type);
            
        } catch(SQLException e){
            System.out.println("Mapping Customer: " + e.getMessage());
        }
        return aptResult;
    }
    
    private static Timestamp toUTCTimestamp(ZonedDateTime time){
        ZonedDateTime utc = time.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldt = utc.toLocalDateTime();
        return Timestamp.valueOf(ldt);
    }
    
    public static boolean addAppointment(String type, int userId, int custId, ZonedDateTime start, ZonedDateTime end){
        if(getAptsByUserTime(start, end, userId).isEmpty()){
            try{
                String statement = "INSERT INTO appointment(customerId, userId, type, start, end, createDate, createdBy, lastUpdateBy) VALUES(?,?,?,?,?,?,?,?)";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);

            //key mapping
                ps.setInt(1, custId);
                ps.setInt(2, userId);
                ps.setString(3, type);
                ps.setTimestamp(4, toUTCTimestamp(start));
                ps.setTimestamp(5, toUTCTimestamp(end)); 
                ps.setTimestamp(6, toUTCTimestamp(ZonedDateTime.now()));
                ps.setString(7, "admin");
                ps.setString(8, "admin");

                ps.execute();
            } catch(SQLException e){
                System.out.println(e.getMessage());
            }
        } else {return false;}
        return true;
    }
    
    public static ObservableList <Appointment> getAptsByUserTime(ZonedDateTime start, ZonedDateTime end, int userId){
        ObservableList <Appointment> userSchedule = FXCollections.observableArrayList();

        try{
            String statement = "SELECT appointmentId, customer.customerId, customerName, userName, type, start, end " +
                               "FROM appointment JOIN customer ON appointment.customerId = customer.customerId " +
                               "JOIN user ON appointment.userId = user.userId " +
                               "WHERE appointment.userId = ? AND start BETWEEN ? AND ? " +
                               "OR end BETWEEN ? AND ? AND appointment.userId = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            
            //key mapper
            Timestamp utcStart = toUTCTimestamp(start);
            Timestamp utcEnd = toUTCTimestamp(end);
            ps.setInt(1, userId);
            ps.setTimestamp(2, utcStart);
            ps.setTimestamp(3, utcEnd);
            ps.setTimestamp(4, utcStart);
            ps.setTimestamp(5, utcEnd);
            ps.setInt(6, userId);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment aptResult = mappingAppointment(rs);
                userSchedule.add(aptResult);
            }
            
        } catch(SQLException e){
            System.out.println("Get Appointment By User And Time: " + e.getMessage());
        }
        return userSchedule;
    }
    public static ObservableList <Appointment> getAptsByUser(int userId){
        ObservableList <Appointment> userSchedule = FXCollections.observableArrayList();

        try{
            String statement = "SELECT appointmentId, customer.customerId, customerName, userName, type, start, end " +
                               "FROM appointment JOIN customer ON appointment.customerId = customer.customerId " +
                               "JOIN user ON appointment.userId = user.userId " +
                               "WHERE appointment.userId = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            
            ps.setInt(1, userId); //key mapper
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment aptResult = mappingAppointment(rs);
                userSchedule.add(aptResult);
            }
            
        } catch(SQLException e){
            System.out.println("Get Appointment By User: " + e.getMessage());
        }
        return userSchedule;
        
    }
    public static ObservableList <Appointment> getAppointmentsByTime(ZonedDateTime start, ZonedDateTime end){
        ObservableList <Appointment> aptResults = FXCollections.observableArrayList();

        try{
            String statement = "SELECT appointmentId, customer.customerId, customerName, userName, type, start, end " +
                               "FROM appointment JOIN customer ON appointment.customerId = customer.customerId " +
                               "JOIN user ON appointment.userId = user.userId " +
                               "WHERE start BETWEEN ? AND ? " +
                               "OR end BETWEEN ? AND ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            
            //key mapper
            Timestamp utcStart = toUTCTimestamp(start);
            Timestamp utcEnd = toUTCTimestamp(end);
            ps.setTimestamp(1, utcStart);
            ps.setTimestamp(2, utcEnd);
            ps.setTimestamp(3, utcStart);
            ps.setTimestamp(4, utcEnd);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment aptResult = mappingAppointment(rs);
                aptResults.add(aptResult);
            }
            
        } catch(SQLException e){
            System.out.println("Get Appointment By Time: " + e.getMessage());
        }
        return aptResults;
    }
    

    public static ObservableList <Appointment> getAllAppointments(){
        ObservableList <Appointment> allAppointments = FXCollections.observableArrayList();
        try{
            String statement = "SELECT appointmentId, customer.customerId, customerName, userName, type, start, end "+
                               "FROM appointment JOIN customer ON appointment.customerId = customer.customerId " +
                               "JOIN user ON appointment.userId = user.userId";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment aptResult = mappingAppointment(rs);
                allAppointments.add(aptResult);
            }
            
        } catch(SQLException e){
            System.out.println("Get All Appointments: " + e.getMessage());
        }
        return allAppointments;
    }
    
    public static void deleteAppointment(Appointment apt){
                String statement = "DELETE FROM appointment WHERE appointmentId = ?";
        try{
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);

            //Key Mapping
            ps.setInt(1, apt.getAptId());

            ps.execute();
        } catch (SQLException e) {
            System.out.println("Delete Customer: " + e.getMessage());
        }
    }

    public static boolean updateAppointment(int aptId, int userId, int custId, String type, ZonedDateTime start, ZonedDateTime end){
        Long overlappingApts = getAptsByUserTime(start, end, userId).stream() 
                               .filter(i -> i.getAptId() != aptId) // do not consider this appointment when determining if there is a time overlap
                               .count();                           
                    
        if(overlappingApts == 0){ 
            String statement = "UPDATE appointment SET type = ?, userId = ?, customerId = ?, start = ?, end = ? WHERE appointmentId = ?";
            try{
             PreparedStatement ps = DBConnection.getConnection().prepareStatement(statement);
             //key mapping
             ps.setString(1, type);
             ps.setInt(2, userId);
             ps.setInt(3, custId);
             ps.setTimestamp(4, toUTCTimestamp(start));  
             ps.setTimestamp(5, toUTCTimestamp(end));
             ps.setInt(6, aptId);
             ps.executeUpdate();
            } catch(SQLException e){
                System.out.println("Update Appointment: " + e.getMessage());
            }
        } else {return false;}
        return true;
    }
}
