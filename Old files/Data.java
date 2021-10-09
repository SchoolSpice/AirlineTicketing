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
import java.util.ArrayList;

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
}
