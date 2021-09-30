import java.lang.Runtime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class KioskTerminal {
	private static final String TITLE = "Airline Reservation Kiosk";

	private static final String[] OPTIONS = { "Search Flights", "View Reservation", "Cancel Reservation" };

	private static final String OPTION_ZERO = "Exit";

	public static void main(String args[]) throws Exception {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

		Menu mainMenu = new Menu(TITLE, OPTIONS, OPTION_ZERO);
		switch (mainMenu.makeSelection()) {
		case 0:
			exitKiosk();
			break;
		case 1:
			searchFlights();
			break;
		case 2:
			viewRes();
			break;
		case 3:
			cancelRes();
			break;
		}
	}

	private static void searchFlights() throws Exception {
		String header = "ID      Departure Time      Departure Date      Departure Location      Arrival Location";
		System.out.println(header);
		try {
			Connection con = getConnection();
			PreparedStatement flights = con.prepareStatement("SELECT * FROM sql3439645.flights");

			ResultSet flightarray = flights.executeQuery();
			ArrayList<String> array = new ArrayList<String>();

			while (flightarray.next()) { // this outputs the flight table from the database
				System.out.print(flightarray.getString("idflights"));
				System.out.print("         ");
				System.out.print(flightarray.getString("departtime"));
				System.out.print("           ");
				System.out.print(flightarray.getString("departdate"));
				System.out.print("                ");
				System.out.print(flightarray.getString("departlocationid"));
				System.out.print("                    ");
				System.out.println(flightarray.getString("arrivallocationid"));
				array.add(flightarray.getString("arrivallocationid"));
			}
			System.out.println("All availble Flights");
			System.out.println("Which ID Do you want to Reserve?:"); // This is a little clunky right now, open to
																		// suggestions on how to make better
			String flightid = "";
			Scanner input = new Scanner(System.in);
			flightid = input.nextLine();
			System.out.println("You have selected:");
			System.out.println(header);
			PreparedStatement reserve = con
					.prepareStatement("SELECT * FROM sql3439645.flights Where flights.idflights = '" + flightid + "'");
			ResultSet chosenflight = reserve.executeQuery();
			ArrayList<String> chosen = new ArrayList<String>();

			while (chosenflight.next()) { // Outputs the 1 row that the customer is choosing
				System.out.print(chosenflight.getString("idflights"));
				System.out.print("         ");
				System.out.print(chosenflight.getString("departtime"));
				System.out.print("           ");
				System.out.print(chosenflight.getString("departdate"));
				System.out.print("                ");
				System.out.print(chosenflight.getString("departlocationid"));
				System.out.print("                    ");
				System.out.println(chosenflight.getString("arrivallocationid"));
				chosen.add(chosenflight.getString("arrivallocationid"));
			}
			System.out.println("Is this correct?");
			String answer = input.nextLine();
			if (answer != "y") { // currently always assume yes (Could not get this to function properly)
				System.out.println("Then lets get you reserved");
				System.out.println("What is your first name?");
				String first = input.nextLine();
				System.out.println("What is your last name?");
				String last = input.nextLine();

				// Want to add a check here if there is already a user with that name and if so
				// then ask for an identifier.

				PreparedStatement postedcust = con.prepareStatement(
						"INSERT INTO customers (firstname,lastname) VALUES ('" + first + "','" + last + "')");
				postedcust.executeUpdate();
				PreparedStatement postedconf = con
						.prepareStatement("INSERT INTO confirmations (flightid) VALUES ('" + flightid + "')");
				postedconf.executeUpdate();
				PreparedStatement postedcustconf = con.prepareStatement(
						"INSERT INTO customerconfirmation VALUES ((Select Max(idcustomers) From sql3439645.customers Where firstname = '"
								+ first + "' And lastname = '" + last
								+ "'), (Select Max(idconfirmations) From sql3439645.confirmations Where flightid = '"
								+ flightid + "'))");
				postedcustconf.executeUpdate();
				PreparedStatement confirmation = con
						.prepareStatement("Select Max(idconfirmations) From sql3439645.confirmations Where flightid = '"
								+ flightid + "'");
				ResultSet confirmationset = confirmation.executeQuery();
				confirmationset.next();
				System.out.println("Congrats " + first + " " + last + " you have sucsesfully booked your flight.");
				System.out.println("Your Confirmation number is: " + confirmationset.getString("Max(idconfirmations)"));
// Not sure if we want to do anything else in this class other than go back to the main menu
			} else {
				System.out.println("If failed");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void viewRes() {
	};

	private static void cancelRes() {
	};

	private static void exitKiosk() {
		System.out.println("\nGoodbye...");
		System.exit(0);
	}

	public static Connection getConnection() throws Exception { // Needed to connect to the server
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://sql3.freemysqlhosting.net:3306/sql3439645";
			String username = "sql3439645";
			String pass = "pEdp5FFFLf";
			Class.forName(driver).newInstance();

			Connection conn = DriverManager.getConnection(url, username, pass);

			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}