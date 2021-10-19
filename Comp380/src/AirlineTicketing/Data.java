/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        Comp 380/L
 * Instructor:    Abhishek Verma
 * Date created:  05-OCT-2021
 * Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu
 */

/* Class:  Data
 * Instances of this class are used to handle
 * our business logic and data processing.
 * The Data class does not 'know' how the data
 * is stored in the database or displayed on
 * the screen.
 */

package AirlineTicketing;

import java.util.ArrayList;

class Data {
    DB database;
    
    private Data (DB database) {
        super();
        this.database = database;
    } //end-Data
    
    static Data getInstance() throws Exception {
        DB temp;
        try {
            temp = DB.getInstance();
        }
        catch (Exception e) {
            System.out.println(e);
            throw new Exception("Unable to get database");
        }
        return new Data(temp);
    } //end-getInstance
    
    int getFlights() {
        Object[] records = {"Search failed: "};
        int flightNo;
        try {
            records = database.allFlights().toArray();
        } catch (Exception e) {
            System.out.println(records);
            System.out.println(e);
        } //end-try-catch
        /*
        for (Object o : records) {
            System.out.println(o);
        }
        */
        try {
            flightNo = ConsoleTable.pick(database.allFlights());
            if(flightNo > 0) {
                System.out.println("You selected flight # " + flightNo);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Flight list unavailable.");
            return 0;
        } //end-try-catch
        return flightNo;
    } //end-getFlights
    
	//edit return type to int
    int search(String departure, String arrival, int[] mdy) {
        ArrayList<String> results = null;
        int flightNum;
        boolean success = false;
        try {
            database = DB.getInstance();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to connect to database.");
            return 0;
        } //end-try-catch
        try {
            results = database.searchFlights(departure, arrival, mdy);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to search database.");
        } //end-try-catch
        flightNum = ConsoleTable.pick(results);
        System.out.println("The flight no. you picked is... " + flightNum);
        return flightNum; //edit
    } //end-search
	
    
    int makeRes(final String[] CUSTOMER, final int FLIGHT_ID) {
        int new_idcustomers, new_idconfirmations;
        boolean made_reservation;
        try {
            database = DB.getInstance();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to connect to database.");
        } //end-try-catch
        new_idcustomers = database.insertCustomer(CUSTOMER[0], CUSTOMER[1]);
        new_idconfirmations = database.insertConfirmation(FLIGHT_ID);
        made_reservation = database.insertCustomerConfirmation(new_idcustomers,
                new_idconfirmations);
        if(made_reservation)
            return new_idconfirmations;
        return 0;
    } //end-makeRes
    
    void runSQL(final String S) {
        /*
        ResultSet results;
        ConsoleTable table;
        try {
            results = database.runQuery(S);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("SQL query unsuccessful.");
            return;
        }
        try {
            table = ConsoleTable.makeTable(results);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to retrieve meta data.");
            return;
        }
        try {
            table.displayTable();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to display results.");
        }
        */
    } //end-runSQL
    
    static int[] convert(String s) throws Exception {
        String[] monthDayYear;
        int[] date = {0, 0, 0};
        try {
            monthDayYear = s.split("/");
        } catch (Exception e) {
            try {
                monthDayYear = s.split("-");
            } catch (Exception e2) {
                monthDayYear = s.split(" ");
            }
        }
        date[0] = Integer.parseInt(monthDayYear[0]);
        date[1] = Integer.parseInt(monthDayYear[1]);
        date[2] = Integer.parseInt(monthDayYear[2]);
        if(date[2] < 100) {date[2] += 2000;} // convert YY to YYYY
        return date;
    } //end-convert
} //end-Class:Data
