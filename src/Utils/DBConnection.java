
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // JDBC URL parts
    private static final String PROTOCOL = "jdbc";
    private static final String VENDORNAME = ":sqlite:";
    private static final String IPADDRESS = "EmbeddedDatabase.db";
    
    // JDBC URL
    private static final String JDBCURL = PROTOCOL + VENDORNAME + IPADDRESS;
    
    // Driver Interface Reference
    private static Connection conn;
    
    public static Connection startConnection() throws SQLException{
        conn = (Connection)DriverManager.getConnection(JDBCURL);
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
