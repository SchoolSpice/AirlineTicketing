
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

import java.sql.ResultSet;
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
            //System.out.println(e);
            throw new Exception("Unable to get database");
        }
        return new Data(temp);
    } //end-getInstance
    
    int getFlights() {
        int flightNo;
        try {
            flightNo = ConsoleTable.pick(database.allFlights(), 'f');
            if(flightNo > 0) {
                System.out.println("You selected flight # " + flightNo);
            }
        } catch (Exception e) {
            //System.out.println(e);
            System.out.println("Flight list unavailable.");
            return 0;
        } //end-try-catch
        return flightNo;
    } //end-getFlights
    
    int search(String departure, String arrival, int[] mdy) {
        ArrayList<String> results = null;
        int flightNum;
        boolean success = false;
        try {
            database = DB.getInstance();
        } catch (Exception e) {
            //System.out.println(e);
            System.out.println("Unable to connect to database.");
            return 0;
        } //end-try-catch
        try {
            results = database.searchFlights(departure, arrival, mdy);
        } catch (Exception e) {
            //System.out.println(e);
            System.out.println("Unable to search database.");
	    return 0;
        } //end-try-catch
         try {
            if(results.isEmpty()) { 
                System.out.println("No records found.");
                return 0;
            } //end-if
            flightNum = ConsoleTable.pick(results, 'f');
            if(flightNum > 0) {
                System.out.println("You selected flight # " + flightNum);
            }
        } catch (Exception e) {
            //System.out.println(e);
            System.out.println("Flights list unavailable.");
            return 0;
        } //end-try-catch
        //TODO: do something with results
        return flightNum;
    } //end-search
    
    int getConfirmation(final String EMAIL) throws Exception {
        ArrayList<String> results;
        int confirmationNo;
        int idcustomers = database.searchCustomers(EMAIL);
        if(idcustomers == 0)
            throw new Exception("Email not found in database.");
        if(idcustomers == -1)
            throw new Exception("Unable to search \"customers\" in database.");
        try {
            results = database.searchConfirmations(idcustomers);
            if(results !=  null) {
                confirmationNo = ConsoleTable.pick(results, 'c');
            } else {
                throw new Exception("No confirmations found matching email.");
            } //end-if-else
        } catch (Exception e) {
            //System.out.println(e);
            throw new Exception("Unable to search \"confirmations\" by \"idcustomer\"");
        } //end-try-catch
        if(confirmationNo > 0)
            System.out.println("Confirmation Number: " + confirmationNo);
        return confirmationNo;
    } //end-getConfirmation
	
    
    int makeRes(final String[] CUSTOMER, final int FLIGHT_ID, final int[] SEATS) {
        int new_idcustomers, new_idconfirmations;
        boolean made_reservation;
        new_idcustomers = database.searchCustomers(CUSTOMER[2]);
        if(new_idcustomers <= 0)
            new_idcustomers = database.insertCustomer(CUSTOMER[0], CUSTOMER[1], CUSTOMER[2]);
        new_idconfirmations = database.insertConfirmation(FLIGHT_ID, SEATS);
        made_reservation = database.insertCustomerConfirmation(new_idcustomers,
                new_idconfirmations);
        if(made_reservation)
            return new_idconfirmations;
        return 0;
    } //end-makeRes

    int[] availableSeats(final int FLIGHT_ID) throws Exception {
        int[] seats = database.reservedSeats(FLIGHT_ID);
        int[] max = database.maxSeats(FLIGHT_ID);
        int[] available = new int[3];
        available[0] = max[0] - seats[0];
        available[1] = max[1] - seats[1];
        available[2] = max[2] - seats[2];
        return available;
    } //end-availableSeats

    boolean cancelreservation(final int CONFIRMATION_ID) {
    	return database.deleteConfirmation(CONFIRMATION_ID);
    }
       
    static int[] convert(String s) throws Exception {
        String[] monthDayYear = new String[3];
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
        if(date[2] < 2000 || date[2] > 2100)
            throw new Exception("Invalid DATE: year");
        switch(date[0]) {
            case 2:
                if (date[1] < 1 || date[1] > 29)
                    throw new Exception("Invalid DATE: day");
                break;
            case 4: case 6: case 9: case 11:
                if(date[1] < 1 || date[1] > 30)
                    throw new Exception("Invalid DATE: day");
                break;
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                if(date[1] < 1 || date[1] > 31)
                    throw new Exception("Invalid DATE: day");
                break;
            default:
                throw new Exception("Invalid DATE: month");
        }
        return date;
    } //end-convert
} //end-Class:Data
