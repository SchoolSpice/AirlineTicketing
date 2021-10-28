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
    } //end-getInstance
	
    ArrayList<String> searchFlights(final String DEP, String ARR, final int[] MDY) throws Exception {
        PreparedStatement query =
                conn.prepareStatement("SELECT * FROM airlinedb.flights " +
                "WHERE flights.departurelocationid = '" +
                DEP + "', flights.arrivallocationid = '" +
                ARR + "', flights.departdate = '" + MDY[0] +
                "-" + MDY[1] + "-" + MDY[2] + "'");
        return toArrayList(query.executeQuery());
    } //end-searchFlights
    
    int searchCustomers(final String EMAIL) {
        PreparedStatement query;
        ResultSet results;
        String idcustomerAsString;
        int idcustomer;
        try {
            query = conn.prepareStatement("SELECT idcustomers from " +
                    "airlinedb.customers WHERE email = ('" +
                    EMAIL + "')");
            results = query.executeQuery();
            if(results.next()) {
                idcustomerAsString = results.getString("idcustomers");
                idcustomer = Integer.parseInt(idcustomerAsString);
            } else {
                return 0;
            } //end-if-else       
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to search CUSTOMERS by EMAIL");
            return -1;
        } //end-try-catch
        return idcustomer;
    } //end-searchCustomers
    
    int insertCustomer(final String FIRST, final String LAST, final String EMAIL) {
        ResultSet results;
        Statement stmt;
        BigDecimal id = new BigDecimal(0);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO customers (firstname,lastname,email)"
                    + "VALUES ('"
                    + FIRST + "','"
                    + LAST + "','"
                    + EMAIL + "')",
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
    
    
    int deleteConfirmation(final int FLIGHT_ID) {
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

    } //end-deleteConfirmation
    
    boolean deleteCustomerConfirmation(final int ID_CUST, final int ID_CONF) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO customerconfirmation VALUES ('"
                    + ID_CUST + "', '" + ID_CONF + "')");
        } catch (Exception e) {
            System.out.println(e);

            System.out.println("Unable to insert new record into " +
                    "\"customerconfirmation\"");
            return false;
        } //end-try-catch
        return true;        
    } //end-deleteCustomerConfirmation
    
    
    ArrayList<String> searchConfirmations(final int CUST_ID) throws Exception {
        ResultSet results;
        PreparedStatement query =
                conn.prepareStatement("Select confirmations.idconfirmations as " + 
                        "'Confirmation Number', flights.idflights as " + 
                        "'Flight Number', flights.departdate as 'Date', " + 
                        "flights.departtime as 'Time', " + 
                        "departurelocations.idlocations as 'Departure Location', " + 
                        "arrivallocations.idlocations as 'Arrival Location' " + 
                        "From airlinedb.customers, airlinedb.confirmations, " + 
                        "airlinedb.locations as departurelocations, " + 
                        "airlinedb.locations as arrivallocations, " + 
                        "airlinedb.customerconfirmation, airlinedb.flights " + 
                        "Where customers.idcustomers=customerconfirmation.customerid " + 
                        "And customerconfirmation.confirmationid = confirmations.idconfirmations " + 
                        "And confirmations.flightid = flights.idflights " + 
                        "And flights.departlocationid = departurelocations.idlocations " + 
                        "And flights.arrivallocationid = arrivallocations.idlocations " + 
                        "And customers.idcustomers = '" + 
                        CUST_ID + "'");
        results = query.executeQuery();
        return toArrayList(results);
    } //end-searchConfirmations
    

       
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
        ResultSetMetaData meta = r.getMetaData();
        int columnCount = meta.getColumnCount();
        final String DELIMITER = "; ";
        ArrayList<String> list = new ArrayList<String>();
        while(r.next()) { 
            String flightInfo = "";
            for(int i = 1; i <= columnCount; i++){
                flightInfo += r.getString(i) + DELIMITER;
            } //end-loop
            /*
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
            */
            list.add(flightInfo);
        } //end-while
        return list;
    } //end-toArrayList
} //end-class:DB