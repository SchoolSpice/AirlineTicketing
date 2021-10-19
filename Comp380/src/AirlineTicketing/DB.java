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
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.math.BigDecimal;

class DB {
    private static DB single_instance = null;
    private static final String DRIVER =   "com.mysql.cj.jdbc.Driver";
    private static final String URL =      "jdbc:mysql://db4free.net:3306/airlinedb";
    private static final String USERNAME = "coolbob915";
    private static final String PASSWORD = "C0mp380se@ting";
    private Connection conn;
    
    private DB (Connection conn) {
        super();
        this.conn = conn;
    } //end-DB
    
    static DB getInstance() throws Exception {
        Connection new_conn;
        if(single_instance == null) {
            try {
                Class.forName(DRIVER).newInstance();
                new_conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
            catch (Exception e) {
                System.out.println(e);
                return null;
            }
            single_instance = new DB(new_conn);
            return single_instance;
        } //end-if
        return single_instance;
    } //end-initialize
	
    ArrayList<String> searchFlights(final String DEP, String ARR, final int[] MDY) throws Exception {
        PreparedStatement query =
                conn.prepareStatement("SELECT * FROM airlinedb.flights " +
                "WHERE flights.departlocationid = '" +
                DEP + "' AND flights.arrivallocationid = '" +
                ARR + "' AND flights.departdate = '" + MDY[2] +
                "-" + MDY[0] + "-" + MDY[1] + "'");
        return toArrayList(query.executeQuery());
    } //end-searchFlights
    
    int insertCustomer(final String FIRST, final String LAST) {
        ResultSet results;
        Statement stmt;
        BigDecimal id = new BigDecimal(0);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO customers (firstname,lastname)"
                    + "VALUES ('"
                    + FIRST
                    + "','"
                    + LAST
                    + "')",
                    Statement.RETURN_GENERATED_KEYS);
            results = stmt.getGeneratedKeys();
            while(results.next()){
                id = results.getBigDecimal(1);
            } //end-loop
            results.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to insert customer into database.");
            return 0;
        } //end-try-catch
        return id.intValue();
    } //end-insertCustomer
    
    int insertConfirmation(final int FLIGHT_ID) {
        ResultSet results;
        Statement stmt;
        BigDecimal id = new BigDecimal(0);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO confirmations (flightid) VALUES ('"
                    + FLIGHT_ID + "')",
                    Statement.RETURN_GENERATED_KEYS);
            results = stmt.getGeneratedKeys();
            while(results.next()){
                id = results.getBigDecimal(1);
            } //end-loop
            results.close();
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to insert \"flightid\" in confirmations.");
            return 0;
        } //end-try-catch
        return id.intValue();
    } //end-insertConfirmation
    
    boolean insertCustomerConfirmation(final int ID_CUST, final int ID_CONF) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO customerconfirmation VALUES ('"
                    + ID_CUST + "', '" + ID_CONF + "')");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to insert new record into \"customerconfirmation\"");
            return false;
        } //end-try-catch
        return true;        
    } //end-insertCustomerConfirmation
    
    
    ArrayList<String> allFlights() throws Exception {
        ResultSet results;
        PreparedStatement query =
                conn.prepareStatement("SELECT * FROM airlinedb.flights");
        results = query.executeQuery();
        return toArrayList(results);
    } //end-allFlights
	
    ResultSet runQuery(final String s) throws Exception {
        PreparedStatement query = conn.prepareStatement(s);
        ResultSet results = query.executeQuery();
        ResultSetMetaData meta = results.getMetaData();
        System.out.println(results.toString());
        System.out.println("Number of columns: " + meta.getColumnCount());
        return results;
    } //end-runQuery
    
    private ArrayList<String> toArrayList(ResultSet r) throws Exception {
        final String DELIMITER = "; ";
        ArrayList<String> list = new ArrayList<String>();
        while(r.next()) { 
            String flightInfo =
                    r.getString("idflights") +
                    DELIMITER +
                    r.getString("departlocationid") +
                    DELIMITER +
                    r.getString("departtime") +
                    DELIMITER +
                    r.getString("departdate") +
                    DELIMITER +
                    r.getString("arrivallocationid") +
                    DELIMITER +
                    r.getString("arrivaltime") +
                    DELIMITER +
                    r.getString("arrivaldate");
            list.add(flightInfo);
        } //end-while
        return list;
    } //end-toArrayList
} //end-class:DB
