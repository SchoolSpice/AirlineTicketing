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

 package AirlineTicketing;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.Statement;
 import java.sql.PreparedStatement;
 import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import javax.sound.sampled.SourceDataLine;

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
                 conn.prepareStatement("SELECT flights.idflights, flights.departtime, flights.departdate,flights.arrivaltime,flights.arrivaldate," + 
                 		"flights.departlocationid,flights.arrivallocationid," + 
                 	"ifnull(flights.firstseats-sum(confirmations.firstseats),20),ifnull(flights.buiseats-sum(confirmations.buiseats),20)," + 
                 		"ifnull(flights.econseats-sum(confirmations.econseats),60) FROM flights left join confirmations on flights.idflights = confirmations.flightid " +
                 "WHERE flights.departlocationid = '" +
                 DEP + "'AND flights.arrivallocationid = '" +
                 ARR + "'AND flights.departdate = '" + MDY[2] +
                 "-" + MDY[0] + "-" + MDY[1] + "'" + "Group By idflights");
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
             
             System.out.println("Unable to search CUSTOMERS by EMAIL");
             return -1;
         } //end-try-catch
         return idcustomer;
     } //end-searchCustomers

     int[] reservedSeats(final int FLIGHT_ID) throws Exception {
        String[] sql = {
            "SELECT firstseats FROM airlinedb.confirmations WHERE flightid ='" + FLIGHT_ID + "'",
            "SELECT buiseats FROM airlinedb.confirmations WHERE flightid ='" + FLIGHT_ID + "'",
            "SELECT econseats FROM airlinedb.confirmations WHERE flightid ='" + FLIGHT_ID + "'"
        };
        /* For "searchSeats" array:
         * First class = index 0
         * Busines class = index 1
         * Economy class = index 2
         */
        int[] searchSeats = new int[sql.length];
        PreparedStatement query;
        ResultSet results;
        for (int i = 0; i < sql.length; i++) {
            query = conn.prepareStatement(sql[i]);
            results = query.executeQuery();
            if(results.next()) {
                searchSeats[i] = sumSeats(results);
            } else {
                searchSeats[i] = 0;
            }
        } //end-for
        return searchSeats;
     } //end-reservedSeats

     int[] maxSeats(final int FLIGHT_ID) throws Exception {

        PreparedStatement query =
                conn.prepareStatement("SELECT firstseats, buiseats, econseats FROM airlinedb.flights WHERE idflights='"
                        + FLIGHT_ID + "'");
        ResultSet results = query.executeQuery(); 
        int[] seats = {0, 0, 0};
        if (results.next()) {
            for (int i = 1; i <= 3; i++) {
                seats[i - 1] = results.getInt(i);
            } //end-for
        } else {
            throw new Exception("Flight #" + FLIGHT_ID + " not found.");
        } //end-if-else
        return seats;
     } //end-maxSeats

    private int sumSeats(ResultSet r) throws Exception {
        int accum = 0;
        do {
            accum += r.getInt(1);
        } while (r.next());
        return accum;
    } //end-sumSeats
     
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
             
             System.out.println("Unable to insert customer into database.");
             return 0;
         } //end-try-catch
         return id.intValue();
     } //end-insertCustomer
     
     int insertConfirmation(final int FLIGHT_ID, final int[] SEATS) {
         ResultSet results;
         Statement stmt;
         BigDecimal id = new BigDecimal(0);		
         try {
             stmt = conn.createStatement();
             stmt.executeUpdate("INSERT INTO confirmations (flightid, firstseats, buiseats, econseats) VALUES ('"
                     + FLIGHT_ID + "', '" + SEATS[0] + "', '" + SEATS[1] + "', '" + SEATS[2] + "')",
                     Statement.RETURN_GENERATED_KEYS);
             results = stmt.getGeneratedKeys();
             while(results.next()){
                 id = results.getBigDecimal(1);
             } //end-loop
             results.close();
             stmt.close();
         } catch (Exception e) {
             
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
             
             System.out.println("Unable to insert new record into \"customerconfirmation\"");
             return false;
         } //end-try-catch
         return true;        
     } //end-insertCustomerConfirmation
     
     boolean deleteConfirmation(final int CONFIRM_ID) {
         Statement stmt;
         try {
             stmt = conn.createStatement();    
             stmt.executeUpdate("DELETE from airlinedb.customerconfirmation where confirmationid = ('"
                     + CONFIRM_ID + "')");
             stmt.executeUpdate("DELETE from airlinedb.confirmations where idconfirmations = ('"
                     + CONFIRM_ID + "')");
             stmt.close();
         } catch (Exception e) {
            System.out.println("Unable to delete \"confirmationid\" in confirmations.");
             return false;
         } //end-try-catch
         return true;
     } //end-deleteConfirmation
     
     ArrayList<String> searchConfirmations(final int CUST_ID) throws Exception {
         ResultSet results;
         PreparedStatement query =
                 conn.prepareStatement("Select confirmations.idconfirmations as " + 
                         "'Confirmation Number', flights.idflights as " + 
                         "'Flight Number', flights.departdate as 'Date', " + 
                         "flights.departtime as 'Time', " + 
                         "departurelocations.idlocations as 'Departure Location', " + 
                         "arrivallocations.idlocations as 'Arrival Location', " +
                         "confirmations.firstseats as 'First Class Seats', " +
                         "confirmations.buiseats as 'Business Class Seats', " +
                         "confirmations.econseats as 'Economy Class Seats' " +
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
                 conn.prepareStatement("SELECT flights.idflights, flights.departtime, flights.departdate,flights.arrivaltime,flights.arrivaldate, \n" + 
                 		"flights.departlocationid,flights.arrivallocationid, \n" + 
                 		"ifnull(flights.firstseats-sum(confirmations.firstseats),20),ifnull(flights.buiseats-sum(confirmations.buiseats),20),\n" + 
                 		"ifnull(flights.econseats-sum(confirmations.econseats),60) from flights left join confirmations \n" + 
                 		"on flights.idflights = confirmations.flightid group by idflights");
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
             list.add(flightInfo);
         } //end-while
         return list;
     } //end-toArrayList
 } //end-class:DB
