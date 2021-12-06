/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        Comp 380/L
 * Instructor:    Abhishek Verma
 * Date created:  9-OCT-2021
 * Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu
 */

/* Class:  ConsoleTable
 * Instances of this class take a ResultSet
 * and display it neatly on the console.
 * Also helps the user to pick from a list
 * and return the selected flight number
 */
 
package AirlineTicketing; 

import java.util.ArrayList;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.util.Scanner;
 
class ConsoleTable {
    static final int FLIGHT_TABLE_WIDTH = 92;
    static final int LINE_WIDTH = 30;
    static final int CONF_TABLE_WIDTH = 46;
   	
    static int pick(ArrayList<String> list, final char FLAG) {
        /* FLAG:
            Flights header = f
            Confirmations header = c
        */
        Object[] records = {"No records found."};
        int selection = 0;
        int remaining;
        int count = 0;
        try {
            records = list.toArray();
        } catch (Exception e) {
            System.out.println(e);
        } //end-try-catch
        remaining = records.length;
        System.out.println(remaining + " records(s) found:");
        if(remaining == 1) {
		//Start of table
            printHeader(FLAG);  
            printRecord((String) records[0], FLAG);
		//end of table format
            return getFirstField(records[0]);
        } //end-if
        boolean includeHeader = true;
        while(remaining > 0) {
            if(includeHeader) {
                printHeader(FLAG);
                includeHeader = false;
            } //end-if
            System.out.print("(" + (count % 9 + 1) + ") ");
            printRecord((String) records[count], FLAG);
	        count++;
            remaining--;
            if((count % 9 == 0) || (remaining == 0)) {
                includeHeader = true;
                selection = enterSelection();
                int realIndex;
                if(count % 9 == 0)
                    realIndex = count - (10 - selection);
                else
                    realIndex = count - ((count % 9) - selection) - 1;
                switch(selection) {
                    case -1: break;
                    case  0: return 0;
                    case 1: case 2: case 3: case 4:
                    case 5: case 6: case 7: case 8:
                    case 9: return getFirstField(records[realIndex]);
                } //end-switch
                if(remaining > 0)
                    System.out.println("Next page...");
            } //end-if
        } //end-loop
        return selection;
    } //end-pick

    private static void printRecord(final String RECORD, final char FLAG) {
        if (FLAG == 'f')
            printFlightRecord(RECORD);
        if(FLAG == 'c')
            printConfirmationRecord(RECORD);
    } //end-printRecord

    private static void printFlightRecord(final String R) {
        String[] fields = R.split(";");
        fields[0] = "#" + fields[0];
        fields[1] = formatTime(fields[1]);
        fields[3] = formatTime(fields[3]);
        System.out.format("|%5.5s%7s %s%s  >>> %s %s%s   %-10s %-8s %-9s%n", fields[0], 
			      fields[5], fields[2], fields[1], fields[6], fields[4], fields[3], 
			      "First:" + fields[7], 
            		      "Bus:" + fields[8], "Econ:" + fields[9]);
    } //end-printFlightRecord

    private static void printConfirmationRecord(final String R) {
        String[] fields = R.split(";");
        fields[3] = formatTime(fields[3]);
        if(fields[6].equals(" 0"))
            fields[6] = "";
        else
            fields[6] = fields[6] + " First Class";
        if(fields[7].equals(" 0"))
            fields[7] = "";
        else
            fields[7] = fields[7] + " Business Class";
        if(fields[8].equals(" 0"))
            fields[8] = "";
        else
            fields[8] = fields[8] + " Economy Class";
        System.out.format(" #%-5sFlight:%-3s    From%s to%s    Departing%s%s    Seats:%s %s %s%n", fields[0], 
			      fields[1], fields[4], fields[5], fields[2], 
			      fields[3], fields[6], fields[7], 
            		      fields[8]);
    } //end-printConfirmationRecord

    private static void printHeader(final char FLAG) {
        if(FLAG == 'f')
            printHeaderForFlights();
        if(FLAG == 'c')
            printHeaderForConfirmations();
    } //end-printHeader

    private static void printHeaderForFlights() {
        System.out.format("%n    ");
        Menu.printDashedLine(FLIGHT_TABLE_WIDTH);
        System.out.format("%12s %15s %26s %33s%n", "FLIGHT",
                "DEPARTURE", "ARRIVAL", "AVAILABLE SEATS");
        System.out.print("    ");
        Menu.printDashedLine(FLIGHT_TABLE_WIDTH);
    } //end-printHeaderForFlights

    private static void printHeaderForConfirmations() {
        System.out.format("%n    ");
        Menu.printDashedLine(CONF_TABLE_WIDTH);
        System.out.format("%48s%n", "Ticket Reservations by Confirmation Number");
        System.out.print("    ");
        Menu.printDashedLine(CONF_TABLE_WIDTH);
    } //end-printHeaderForConfirmations

    private static String formatTime(String time) {
        time = time.substring(0, 6);
        int hour = Integer.parseInt(time.substring(1, 3));
        if (hour < 12)
            time += "a";
        else
            time += "p";
        return time;
    }
    
    private static int getFirstField(Object o) {
        int flightNo = 0;
        String row = o.toString();
        String[] rowArray = row.split(";");
        try {
            flightNo = Integer.parseInt(rowArray[0]);
        } catch (Exception e) {
            System.out.println("Invalid flightid.");
            System.out.println(e);
        } //end-try-catch
        return flightNo;
    } //end-getFlightID
    
    /* this methods only returns 0 to 9 for a valid selection
     * or -1 for an invalid selection or "next page" choice */
    private static int enterSelection() {
        Scanner input = new Scanner(System.in);
        int selection = -1;
        System.out.format("%n");
        System.out.println("Choose 1 thru 9 (or 0 to Exit)");
        System.out.print("Press ENTER for more records: ");
        String rawInput = input.nextLine();
        try {
            selection = Integer.parseInt(rawInput);
            if(selection < 0 || selection > 9) {
                throw new Exception("integer out of bounds");
            } //end-if
        } catch (Exception e) {
            return -1;
        } finally {
            return selection;
        } //end-try-catch-finally
    } //end-enterSelection
} //end-ConsoleTable