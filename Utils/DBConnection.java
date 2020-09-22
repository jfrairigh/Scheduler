
package Utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // JDBC URL parts
    private static final String PROTOCOL = "JDBC";
    private static final String VENDORNAME = ":mysql:";
    private static final String IPADDRESS = "//3.227.166.251/U06ZUt";
    
    // JDBC URL
    private static final String JDBCURL = PROTOCOL + VENDORNAME + IPADDRESS;
    
    // Driver Interface Reference
    private static Connection conn;
    
    //User Name and Password
    private static final String USERNAME = "U06ZUt";
    private static final String PASSWORD = "53688913073";
    
    public static Connection startConnection() throws SQLException{
        conn = (Connection)DriverManager.getConnection(JDBCURL, USERNAME, PASSWORD);
        System.out.println("Connection Succesful");
            
        return conn;
    }
    
    public static void closeConnection() throws SQLException{
            conn.close();
            System.out.println("Connection Closed");
    }
    
    public static Connection getConnection(){
        return conn;
    }
}
