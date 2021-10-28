/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        COMP 380/L
 * Instructor:    Abhishek Verma
 * Date created:  27-SEP-2021
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
import java.util.regex.Pattern;
import java.util.Calendar;
import java.util.Date;

public class KioskTerminal {
	/***** MAIN MENU *****/
	private static final String TITLE_MM = "Airline Reservation Kiosk";
	private static final String[] OPTIONS_MM = { "Search Flights", "View Reservation", "Cancel Reservation"};
	private static final String OPTION_ZERO_MM = "Exit";

	/***** SUB MENU 1 *****/
	private static final String TITLE_SM1 = "Make a Reservation";
	private static final String[] OPTIONS_SM1 = { "View All Flights", "Search Flights by Arrival/Destination" };
	private static final String OPTION_ZERO_SM1 = "Return to Main Menu";

	public static void main(String args[]) throws IOException, InterruptedException {
		// new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		Menu mainMenu = new Menu(TITLE_MM, OPTIONS_MM, OPTION_ZERO_MM);
		while (true) {
			switch (mainMenu.makeSelection()) {
			case 0:
				exitKiosk();
			case 1:
				searchFlights();
				break;
			case 2:
				viewRes();
				break;
			case 3:
				cancelRes();
				break;
			case 4:
				enterSQL();
				break;
			} // end-switch
		} // end-loop
	} // end-main

	private static void searchFlights() {
		Menu subMenu_1 = new Menu(TITLE_SM1, OPTIONS_SM1, OPTION_ZERO_SM1);
		while (true) {
			switch (subMenu_1.makeSelection()) {
			case 0:
				return;
			case 1:
				viewAllFlights();
				break;
			case 2:
				searchFlightsByLoc();
			} // end-switch
		} // end-loop
	} // end-searchFlights

	private static int viewRes() {
            Data data;
            int confirmation = 0;
            String[] customerInfo = getInfo();
            String email = customerInfo[2];
            try {
                data = Data.getInstance();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Unable to get data.");
                return -1;
            } // end-try-catch
            System.out.println("Searching for confirmations by email...");
            try {
                confirmation = data.getConfirmation(email);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Returning to menu...");
            } //end-try-catch
            return confirmation;
	} // end-viewRes

	private static void cancelRes() {
		Data data;
		int confirmationId;
		
		try {
            data = Data.getInstance();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Unable to get data.");
            return;
        } // end-try-catch
		
		confirmationId = viewRes();

		System.out.println("Processing....");
		data.cancelreservation(confirmationId);
		
		System.out.println("Reservation canceled successfully");
		
	} // end-cancelRes

	private static void viewAllFlights() {
            Data data;
            String[] customerInfo;
            int chosenFlight, confirmation;
            try {
                    data = Data.getInstance();
            } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Unable to get data.");
                    return;
            } // end-try-catch
            chosenFlight = data.getFlights();
            if (chosenFlight <= 0) {
                    System.out.println("No flight selected.");
                    return;
            } // end-if
            customerInfo = getInfo();
            confirmation = data.makeRes(customerInfo, chosenFlight);
            if (confirmation == 0) {
                    System.out.println("Reservation failed.");
                    return;
            } else {
                    System.out.println("\nReservation confirmed.");
                    System.out.println("Name: " + customerInfo[0] + " " + customerInfo[1]);
                    System.out.println("Confirmation #: " + confirmation);
            } // end-if-else
	} // end-viewAllFlights

	private static String[] getInfo() {
		Scanner input = new Scanner(System.in);
		String rawFirst, first, rawLast, last, rawEmail, email;
		System.out.println("Please enter your information...");
		while (true) {
			System.out.print("First name: ");
			rawFirst = input.nextLine().trim();
			if (isOnlyLetters(rawFirst)) {
				first = rawFirst;
				break;
			} else {
				invalid("First name");
			} // end-if-else
		} // end-loop
		while (true) {
			System.out.print("Last name: ");
			rawLast = input.nextLine().trim();
			if (isOnlyLetters(rawLast)) {
				last = rawLast;
				break;
			} else {
				invalid("Last name");
			} // end-if-else
		} // end-loop
		while (true) {
			System.out.print("Email: ");
			rawEmail = input.nextLine().trim();
			if (isValidEmail(rawEmail)) {
				email = rawEmail;
				break;
			} else {
				invalid("Email");
			} // end-if-else
		} // end-loop
		return new String[] { first, last, email };
	} // end-getInfo

	private static boolean isOnlyLetters(String s) {
		return s.matches("[ a-zA-Z]+");
	} // end-isOnlyLetters

	private static boolean isValidEmail(String s) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (s == null) {
			return false;
		}
		return pat.matcher(s).matches();
	} // end-isValidEmail

	private static void invalid(String s) {
		System.out.println("\nINVALID [" + s + "]:  Try again...");
	} // end-invalid

	private static void searchFlightsByLoc() {
		/* Variables */
		Scanner input = new Scanner(System.in);
		String departure, arrival, date;
		Data data;
		/* initialize dateValues with today's date */
		Date today = Calendar.getInstance().getTime();
		int[] dateValues = { today.getYear(), today.getMonth(), today.getDay() };
		/* Try to access the data */
		try {
			data = Data.getInstance();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Unable to connect to \"Data\"");
			input.close();
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
		System.out.println("You entered... " + departure + " " + arrival + " " + dateValues[0] + "/" + dateValues[1]
				+ "/" + dateValues[2]);
		data.search(departure, arrival, dateValues);
		input.close();
	} // end-searchFlightsByLoc

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
		input.close();
	} // end-enterSQL

	private static void exitKiosk() {
		System.out.println("\nGoodbye...");
		System.exit(0);
	} // end-exitKiosk

} // end-Class:KioskTerminal