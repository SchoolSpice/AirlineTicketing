import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.sql.ResultSet;

public class Test_File {

	public static void main(String[] args) throws Exception {

		getConnection();
		post();
	}

	public static void post() throws Exception {
		final String var1 = "Joe";
		final String var2 = "Schmoe";
		Random rd = new Random();
		try {
			Connection con = getConnection();
			PreparedStatement posted = con.prepareStatement(
					"INSERT INTO customers (firstname,lastname) VALUES ('"+var1+"','"+var2+"')");
			posted.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			System.out.println("Insert Completed");
		}
	}

	public static Connection getConnection() throws Exception {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/airlinedb";
			String username = "root";
			String pass = "C0mp380se@ting";
			Class.forName(driver).newInstance();

			Connection conn = DriverManager.getConnection(url, username, pass);
			System.out.println("Connected");
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
