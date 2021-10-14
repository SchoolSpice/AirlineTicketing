/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        COMP 380/L
 * Instructor:    Abhishek Verma
 * Date created:  5-OCT-2021
 * Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu
 */

/* Class:  KioskTerminal
 * Instances of this class are console
 * applications that act as a user
 * interface for the airline ticketing
 * system.  This "front end" should
 * only handle user input and output. 
 * The KioskTerminal class does not
 * 'know' where the data comes from
 * or why it changes.
 */

package AirlineTicketing;

// import java.lang.Runtime;
import java.io.IOException;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
// import java.sql.ResultSet;

public class KioskTerminal {
    /***** MAIN MENU *****/
    private static final String TITLE_MM =
        "Airline Reservation Kiosk"; 	
    private static final String[] OPTIONS_MM = {
        "Search Flights",
        "View Reservation",
        "Cancel Reservation",
        "Enter SQL query"};
    private static final String OPTION_ZERO_MM =
        "Exit";
    
    /***** SUB MENU 1 *****/
    private static final String TITLE_SM1 =
        "Make a Reservation";
    private static final String[] OPTIONS_SM1 = {
        "View All Flights",
        "Search Flights by Arrival/Destination"};
    private static final String OPTION_ZERO_SM1 = 
        "Return to Main Menu";
        
	
    public static void main(String args[]) throws IOException, InterruptedException{
        // new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
        Menu mainMenu = new Menu(TITLE_MM, OPTIONS_MM, OPTION_ZERO_MM);
        while(true) {            
            switch(mainMenu.makeSelection()) {
                case 0: exitKiosk();
                case 1: searchFlights();
                        break;
                case 2: viewRes();
                        break;
                case 3: cancelRes();
                        break;
                case 4: enterSQL();
                        break;
            } //end-switch
        } //end-loop
    } //end-main

    private static void searchFlights() {
        Menu subMenu_1 = new Menu(TITLE_SM1, OPTIONS_SM1, OPTION_ZERO_SM1);
        while(true) {
            switch(subMenu_1.makeSelection()){
                case 0: return;
                case 1: viewAllFlights();
                    break;
                case 2: searchFlightsByLoc();
            } //end-switch
        } //end-loop
    } //end-searchFlights

    private static void viewRes() {}

    private static void cancelRes() {}

    private static void viewAllFlights() {
        Data data;
        int chosenFlight;
        try {
            data = Data.getInstance();
        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }
        chosenFlight = data.getFlights();
        try {
            data.makeRes(chosenFlight);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to make new resercation.");
        }
    }
    
    private static void searchFlightsByLoc() {
        /* Variables */
        Scanner input = new Scanner(System.in);
        String departure, arrival, date;
        Data data;
        /* initialize dateValues with today's date */
        Date today = Calendar.getInstance().getTime();
        int[] dateValues = {today.getYear(),
                today.getMonth(), today.getDay()};
        /* Try to access the data */
        try {
            data = Data.getInstance();
        }
        catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to connect to \"Data\"");
            return;
        }
        /* Get user input & validate */
        System.out.print("Enter your destination airport: ");
        arrival = input.nextLine();
        System.out.print("Enter your departure airport: ");
        departure = input.nextLine();
        System.out.print("Enter your preferred departure date (MM/DD/YY): ");
        date = input.nextLine();
        /* Changes the date from String to integers */
        try {
            dateValues = Data.convert(date);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Invalid Date: use format MM/DD/YY");
        }
        System.out.println("You entered... " + departure + " " + arrival +
                " " + dateValues[0] + "/" + dateValues[1] + "/" + dateValues[2]);
        data.search(departure, arrival, dateValues);
} //end-searchFlightsByLoc
    
    private static void enterSQL() {
        Scanner input = new Scanner(System.in);
        String query;
        Data data;
        System.out.println("WARNING: Not yet working!");
        System.out.print("\nEnter SQL: ");
        query = input.nextLine();
        try {
            data = Data.getInstance();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to initialize database.");
            return;
        }
        data.runSQL(query);
    }
    
    private static void exitKiosk() {
        System.out.println("\nGoodbye...");
        System.exit(0);
    }
}