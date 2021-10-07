/* Programmer:    Robert Mosier
 * Organization:  CSUN
 * Course:        Comp 380/L
 * Instructor:    Abhishek Verma
 * Date created:  05-OCT-2021
 * Team members:  Lyana Curry, Abraham Sculler, Ji Sun Wu
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
        Object[] records = database.allFlights().toArray();
        for (Object o : records) {
            System.out.println(o.toString());
        }
    }
}
