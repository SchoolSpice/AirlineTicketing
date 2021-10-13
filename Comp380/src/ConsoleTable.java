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
 */
 
package AirlineTicketing; 

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;
 
class ConsoleTable {
    ResultSet data;
    ResultSetMetaData meta;
    private int num_of_columns;
    // private int num_of_results;
    private String[] columnNames;
    private int[] columnWidths;
	
    private ConsoleTable(ResultSet r, ResultSetMetaData m_d) {
        super();
        data = r;
	meta = m_d;
    } //end-ConsoleTable

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
    
    void displayTable() throws Exception {
        Scanner input = new Scanner(System.in);
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
} //end-class:Table