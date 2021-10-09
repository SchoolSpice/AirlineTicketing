/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        Comp 380/L
 * Instructor:    Abhishek Verma
 * Date created:  05-OCT-2021
 * Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu
 */

/* Class:  DB
 * Instances of the DB class connect to a
 * mySQL database that is hosted online.
 * The DB class executes SQL statements 
 * and returns the results inside a
 * a data structure called "ResultSet"
 * from the JDBC library.
 * The DB class is responsible for the SQL
 * used to manage the database, as well as
 * wrapping/unwrapping the data.
 * The DB class does not 'know' where the
 * data is going to or how it's being used.
 */

 // SELECT * FROM airlinedb.flights
 // for SQL:
 // user input == SQL statement
 // SQL output == ResultSet*
 // *from JDBC library

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
    
    /* TODO: method to inset new customer into database */
	void insertCustomer(String firstName, String lastName) throws Exception {
		PreparedStatement insert =
		conn.prepareStatement("INSERT INTO customers (firstname,lastname) VALUES ('"
				+ firstName
				+ "','"
				+ lastName
				+ "')");
		insert.executeUpdate();
	}
	
	
	/* TODO: split into two separate methods */
    ArrayList<String> allFlights() throws Exception {
        ResultSet results;
        ArrayList<String> records = new ArrayList<String>();
        PreparedStatement query =
                    conn.prepareStatement("SELECT * FROM airlinedb.flights");
        results = query.executeQuery();
        
        /* separate method */
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
