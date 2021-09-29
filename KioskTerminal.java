/* Programmer:  Robert Mosier */
/* Organization:  CSUN */
/* Course:  COMP 380/L */
/* Instructor:  Abhishek Verma */
/* Created:  27-SEP-2021 */
/* Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu */

import java.lang.Runtime;
import java.io.IOException;

public class KioskTerminal {
	private static final String TITLE =
		"Airline Reservation Kiosk"; 
		
	private static final String[] OPTIONS = {
		"Search Flights",
		"View Reservation",
		"Cancel Reservation"};
		 
	private static final String OPTION_ZERO =
		"Exit";
	
	public static void main(String args[]) throws IOException, InterruptedException{
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
		
		Menu mainMenu = new Menu(TITLE, OPTIONS, OPTION_ZERO);
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
	
	private static void searchFlights(){};
	
	private static void viewRes(){};
	
	private static void cancelRes(){};
	
	private static void exitKiosk() {
		System.out.println("\nGoodbye...");
		System.exit(0);
	}
}