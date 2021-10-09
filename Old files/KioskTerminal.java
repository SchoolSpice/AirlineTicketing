/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        Comp 380/L
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

public class KioskTerminal {
    /***** MAIN MENU *****/
    private static final String TITLE_MM =
        "Airline Reservation Kiosk"; 	
    private static final String[] OPTIONS_MM = {
        "Search Flights",
        "View Reservation",
        "Cancel Reservation"};
    private static final String OPTION_ZERO_MM =
        "Exit";
    
    /***** SUB MENU 1 *****/
    private static final String TITLE_SM1 =
        "Make a Reservation";
    private static final String[] OPTIONS_SM1 = {
        "View All Flights",
        "Search Flights by Destination"};
    private static final String OPTION_ZERO_SM1 = 
        "Return to Main Menu";
        
	
    public static void main(String args[]) throws IOException, InterruptedException{
        // new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 

        Menu mainMenu = new Menu(TITLE_MM, OPTIONS_MM, OPTION_ZERO_MM);
        switch(mainMenu.makeSelection()) {
                case 0: exitKiosk();
                case 1: searchFlights();
                        break;
                case 2: viewRes();
                        break;
                case 3: cancelRes();
                        break;
        }
    }

    private static void searchFlights() {
        Menu subMenu_1 = new Menu(TITLE_SM1, OPTIONS_SM1, OPTION_ZERO_SM1);
        switch(subMenu_1.makeSelection()){
            case 0: return;
            case 1: viewAllFlights();
                    break;
            case 2: searchFlightsByDestination();
        }
    }

    private static void viewRes() {}

    private static void cancelRes() {}

    private static void viewAllFlights() {
        // Data data = new Data();
        // Data data = Data.initialize();
        Data data;
        try {
            data = Data.initialize();
        }
        catch (Exception e) {
            System.out.println(e);
            return;
        }
        data.flightList();
    }
    
    private static void searchFlightsByDestination() {}
    
    private static void exitKiosk() {
        System.out.println("\nGoodbye...");
        System.exit(0);
    }
}
