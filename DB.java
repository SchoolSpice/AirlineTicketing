/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        Comp 380/L
 * Instructor:    Abhishek Verma
 * Date created:  05-OCT-2021
 * Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu
 */

package AirlineTicketing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

class DB {
    private static final String DRIVER =   "com.mysql.cj.jdbc.Driver";
    private static final String URL =      "jdbc:mysql://db4free.net:3306/airlinedb";
    private static final String USERNAME = "coolbob915";
    private static final String PASSWORD = "C0mp380se@ting";
    private Connection conn;
    
    private DB (Connection conn) {
        super();
        this.conn = conn;
    }
    
    static DB initialize() throws Exception {
        Connection new_conn;
        try {
            Class.forName(DRIVER).newInstance();
            new_conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return new DB(new_conn);
    }
    
    /* TODO: split into two separate methods */
    ArrayList<String> allFlights() {
        ResultSet results;
        ArrayList<String> records = new ArrayList<String>();
        try {
            PreparedStatement query =
                    conn.prepareStatement("SELECT * FROM airlinedb.flights");
            results = query.executeQuery();
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
        /* Refactor to Data class */
        try {
            final String DELIMITER = "; ";
            while(results.next()) {
                String flightInfo =
                        results.getString("idflights") +
                        DELIMITER +
                        results.getString("departtime") +
                        DELIMITER +
                        results.getString("departdate") +
                        DELIMITER +
                        results.getString("departlocationid") +
                        DELIMITER +
                        results.getString("arrivallocationid");
                records.add(flightInfo);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return records;
    }
}
