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
    static final int LINE_WIDTH = 36;
    // ResultSet data;
    // ResultSetMetaData meta;
    // private int num_of_columns;
    // private int num_of_results;
    // private String[] columnNames;
    // private int[] columnWidths;
	
    static int pick(ArrayList<String> list) {
        Object[] records = {"No records found."};
        int selection = 0;
        int remaining;
        int count = 0;
        try {
            records = list.toArray();
        } catch (Exception e) {
            System.out.println(records);
            System.out.println(e);
        }
        remaining = records.length;
        Menu.printDashedLine(LINE_WIDTH);
        while(remaining > 0) {
            System.out.println("(" + (count % 9 + 1) + ")  " + records[count]);
            count++;
            remaining--;
            if((count % 9 == 0) || (remaining == 0)) {
                selection = enterSelection();
                int realIndex;
                if(count % 9 == 0)
                    realIndex = count - (10 - selection);
                else
                    realIndex = count - ((count % 9) - selection);
                switch(selection) {
                    case -1: break;
                    case  0: return 0;
                    case 1: case 2: case 3: case 4:
                    case 5: case 6: case 7: case 8:
                    case 9: return getFlightID(records[realIndex]);
                } //end-switch
            } //end-if
        } //end-loop
        return selection;
    } //end-pick
    
    private static int getFlightID(Object o) {
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
    
    /* this methods only returns 1 to 9 for a valid selection
     * or -1 for an invalid selection or "next page" choice */
    private static int enterSelection() {
        Scanner input = new Scanner(System.in);
        int selection = -1;
        Menu.printDashedLine(LINE_WIDTH);
        System.out.println("Choose flight 1 thru 9 (or 0 to Exit)");
        System.out.print("Press ENTER for more flights: ");
        String rawInput = input.nextLine();
        try {
            selection = Integer.parseInt(rawInput);
            if(selection < 0 || selection > 9) {
                throw new Exception("integer out of bounds");
            }
        } catch (Exception e) {
            System.out.println("Next page...");
            Menu.printDashedLine(LINE_WIDTH);
            return -1;
        } finally {
            return selection;
        } //end-try-catch
    } //end-enterSelection
    
    /*
    private ConsoleTable(ResultSet r, ResultSetMetaData m_d) {
        super();
        data = r;
	meta = m_d;
    } //end-ConsoleTable
    */
    
    /*
    static ConsoleTable makeTable(ResultSet r) throws Exception {
        ResultSetMetaData m_d = r.getMetaData();
        ConsoleTable newTable = new ConsoleTable(r, m_d);
        newTable.num_of_columns = m_d.getColumnCount();
        System.out.println("Number of columns: " + newTable.num_of_columns);
        // newTable.num_of_results = newTable.data.getFetchSize();
        newTable.columnNames = new String[newTable.num_of_columns];
        System.out.println(newTable.columnNames.toString());
        newTable.columnWidths = new int[newTable.num_of_columns];
        System.out.println(newTable.columnWidths.toString());
        newTable.fillColumnLabels();
        newTable.fillColumnWidths();
        return newTable;
    } //end-makeTable
    */

    /*
    static void displayTable(ResultSet results) throws Exception {
        Scanner input = new Scanner(System.in);
        ResultSetMetaData m_d = r.getMetaData();
        int count = 0;
        boolean header = true;
        data.beforeFirst();
        while(data.next()) {
            if(header) {
                printColumnLabels();
                header = false;
            }
            for (int i = 0; i < num_of_columns; i++) {
                String cell = data.getString(i);
                final int DIFF = columnWidths[i] - cell.length();
                System.out.print(cell + space(DIFF));
            }
            System.out.println();
            if(count % 15 == 0) {
                header = true;
                pause();
            }
            count++;
        }
    } //end-displayTable
    
    private void printColumnLabels() {
        int diff;
        for (int i = 0; i < num_of_columns; i++) {
            diff = columnWidths[i] - columnNames[i].length();
            System.out.print(columnNames[i]);
            System.out.print(space(diff));
        } //end-loop
        System.out.println();
    } //end-printColumnLabels
    
    private String space(final int SIZE) {
        String gap = "";
        for (int i = SIZE; i > 0; i--)
            gap += " ";
        return gap;
    } //end-space
    
    private void fillColumnLabels() throws Exception {
        for (int i = 0; i < num_of_columns; i++) {
            try {
                columnNames[i] = meta.getColumnLabel(i);
            } catch (Exception e) {
                columnNames[i] = meta.getColumnName(i);
            }
        } //end-loop
    } //end-getColumnLabels
    
    private void fillColumnWidths() throws Exception {
        for (int i = 0; i < num_of_columns; i++) 
            columnWidths[i] = meta.getColumnDisplaySize(i);
    } //end-getColumnWidth
    
    private void pause() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            System.out.println(e);
        }
    } //end-pause
    */
} //end-class:Table