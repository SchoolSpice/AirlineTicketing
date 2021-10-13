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

class Data {
    DB database;
    
    private Data (DB database) {
        super();
        this.database = database;
    }
    
    static Data initialize() throws Exception {
        DB temp;
        try {
            temp = DB.initialize();
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return new Data(temp);
    }
    
    void flightList() {
        Object[] records = {"Search failed: "};
        try {
            records = database.allFlights().toArray();
        } catch (Exception e) {
            System.out.println(records);
            System.out.println(e);
        }
        for (Object o : records) {
            System.out.println(o);
        }
    }
    
	
	void search(String departure, String arrival, Calendar cal) {
		DB data;
		ResultSet results;
		try {
			data.initialize();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Unable to connect to database.");
                        return;
		}
		results = data.searchFlights(departure, arrival, cal);
		//TODO: do something with results
	}
	
    void runSQL(final String S) {
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
    } //end-runSQL
    
    static int[] convert(String s) throws Exception {
        String[] yearMonthDay;
        int[] date = {0, 0, 0};
        try {
            yearMonthDay = s.split("/");
        } catch (Exception e) {
            try {
                yearMonthDay = s.split("-");
            } catch (Exception e2) {
                yearMonthDay = s.split(" ");
            }
        }
        date[0] = Integer.parseInt(yearMonthDay[0]);
        date[1] = Integer.parseInt(yearMonthDay[1]);
        date[2] = Integer.parseInt(yearMonthDay[2]);
        if(date[0] < 100) {date[0] += 2000;} // convert YY to YYYY
        return date;
    } //end-convert
}
