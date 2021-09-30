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
	private static final String TITLE =
		"Airline Reservation Kiosk"; 
		
	private static final String[] OPTIONS = {
		"Search Flights",
		"View Reservation",
		"Cancel Reservation"};
		 
	private static final String OPTION_ZERO =
		"Exit";
	
	public static void main(String args[]) throws Exception{
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
		
		Menu mainMenu = new Menu(TITLE, OPTIONS, OPTION_ZERO);
		switch(mainMenu.makeSelection()) {
			case 0: exitKiosk();
					break;
			case 1:
				searchFlights();
					break;
			case 2: viewRes();
					break;
			case 3: cancelRes();
					break;
		}
	}
	
	private static void	searchFlights() throws Exception{
		String header = "ID      Departure Time      Departure Date      Departure Location      Arrival Location";
		System.out.println(header);
		try {
			Connection con = getConnection();
			PreparedStatement flights = con.prepareStatement("SELECT * FROM sql3439645.flights");
			
			ResultSet flightarray = flights.executeQuery();
			ArrayList<String> array = new ArrayList<String>();
			
			while(flightarray.next()) {
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
			System.out.println("Which ID Do you want to Reserve?:");
			String flightid = "";
			Scanner input = new Scanner(System.in);
			flightid = input.nextLine();
			System.out.println("You have selected:");
			System.out.println(header);
			PreparedStatement reserve = con.prepareStatement("SELECT * FROM sql3439645.flights Where flights.idflights = '"+flightid+"'");
			ResultSet chosenflight = reserve.executeQuery();
			ArrayList<String> chosen = new ArrayList<String>();
			
			while(chosenflight.next()) {
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
		}catch(Exception e) {System.out.println(e);}
	}
	
	private static void viewRes(){};
	
	private static void cancelRes(){};
	
	private static void exitKiosk() {
		System.out.println("\nGoodbye...");
		System.exit(0);
	}
	public static Connection getConnection() throws Exception {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://sql3.freemysqlhosting.net:3306/sql3439645";
			String username = "sql3439645";
			String pass = "pEdp5FFFLf";
			Class.forName(driver).newInstance();

			Connection conn = DriverManager.getConnection(url, username, pass);
			//System.out.println("Connected");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}