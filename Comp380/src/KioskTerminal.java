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
					break;
			case 1: try {
				searchFlights();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					break;
			case 2: viewRes();
					break;
			case 3: cancelRes();
					break;
		}
	}
	
	private static  ArrayList<String>	searchFlights() throws Exception{
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM sql3439645.flights");
			
			ResultSet result = statement.executeQuery();
			ArrayList<String> array = new ArrayList<String>();
			System.out.println("ID      Departure Time      Departure Date      Departure Location      Arrival Location");
			while(result.next()) {
				System.out.print(result.getString("idflights"));
				System.out.print("         ");
				System.out.print(result.getString("departtime"));
				System.out.print("           ");
				System.out.print(result.getString("departdate"));
				System.out.print("                ");
				System.out.print(result.getString("departlocationid"));
				System.out.print("                    ");
				System.out.println(result.getString("arrivallocationid"));
				array.add(result.getString("arrivallocationid"));
			}
			System.out.println("All availble Flights");
			return array;
		}catch(Exception e) {System.out.println(e);}
		return null;
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