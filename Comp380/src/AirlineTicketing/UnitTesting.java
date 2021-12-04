package AirlineTicketing;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class UnitTesting {
    int[] date = {10,22,2022};
    Exception exception;
    
  @Test
  public void SearchbyLocationDate1() throws Exception{
      System.out.println("Date Converts With Slashes = ...");
      assertArrayEquals("Date Converts With Slashes = Fail", date,Data.convert("10/22/22"));
      System.out.println("  --Passed!");
  }
  @Test
  public void SearchbyLocationDate2() throws Exception{
      
      System.out.println("Date Converts With Full Year = ...");
      assertArrayEquals("Date Converts With Full Year = Fail",date,Data.convert("10/22/2022"));
      System.out.println("  --Passed!");
  }
  @Test
  public void SearchbyLocationDate3() throws Exception{
      
      System.out.println("Date Converts Single Digit Month/Day = ...");
      assertArrayEquals("Date Converts Single Digit Month/Day = Fail", new int[]{03, 02, 2022},Data.convert("3/2/22"));
      System.out.println("  --Passed!");
  }
  @Test
  public void SearchbyLocationDate4() throws Exception{
      
      System.out.println("Date Converts With Hyphens = ...");
      assertArrayEquals("Date Converts With Hyphens = Fail",date,Data.convert("10-22-22"));
      System.out.println("  --Passed!");
  }
  @Test
  public void SearchbyLocationDate5() throws Exception{
      
      System.out.println("Date Converts With Spaces = ...");
      assertArrayEquals("Date Converts With Spaces = Fail",date,Data.convert("10 22 22"));
      System.out.println("  --Passed!");
      
  }
  
  @Test
  public void SearchByLocationException1() throws Exception {
      
      Exception exception = assertThrows(Exception.class, () -> Data.convert("12/20/3001"));
      System.out.println("Date Throws Invalid Year Exception = ...");
      assertEquals("Date Throws Invalid Year Exception = Fail", "Invalid DATE: year", exception.getMessage());
      System.out.println("  --Passed!");
  }
  
  @Test
  public void SearchByLocationException2() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("06/31/2025"));
      System.out.println("Date Throws Invalid Day Exception = ...");
      assertEquals("Date Throws Invalid Day Exception = Fail", "Invalid DATE: day", exception.getMessage());
      System.out.println("  --Passed!");
  }
  @Test
  public void SearchByLocationException3() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("-5/15/2010"));
      System.out.println("Date Throws Invalid Month Exception = ...");
      assertEquals("Date Throws Invalid Month Exception = Fail", "Invalid DATE: month",exception.getMessage());
      System.out.println("  --Passed!");
  }
  @Test
  public void SearchByLocationException4() throws Exception {  
      
      exception = assertThrows(Exception.class, () -> Data.convert("4868343927"));
      System.out.println("Date Throws Invalid Year Exception For Random Numbers = ...");
      assertEquals("Date Throws Invalid Year Exception For Random Numbers = Fail", "Invalid DATE: year",exception.getMessage()); //Can pass if expecting "For input string: \"4868343927\""
      System.out.println("  --Passed!");
      
  }
  
  //Additional EC testing limits
  
  @Test
  public void SearchByLocationMonthException() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("13/20/2050"));
      System.out.println("Date Throws Invalid Month Exception = ...");
      assertEquals("Date Throws Invalid Month Exception = Fail", "Invalid DATE: month",exception.getMessage());
      System.out.println("  --Passed!");
  }
  
  @Test
  public void SearchByLocationDayException1() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("05/00/2012"));
      System.out.println("Date Throws Invalid Day Exception = ...");
      assertEquals("Date Throws Invalid Day Exception = Fail", "Invalid DATE: day",exception.getMessage());
      System.out.println("  --Passed!");
  }
  
  @Test
  public void SearchByLocationDayException2() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("08/35/2019"));
      System.out.println("Date Throws Invalid Day Exception = ...");
      assertEquals("Date Throws Invalid Day Exception = Fail", "Invalid DATE: day",exception.getMessage());
      System.out.println("  --Passed!");
  }
  
  @Test
  public void SearchByLocationYearException() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("10/29/1992"));
      System.out.println("Date Throws Invalid Year Exception = ...");
      assertEquals("Date Throws Invalid Year Exception = Fail", "Invalid DATE: year",exception.getMessage());
      System.out.println("  --Passed!");
  }
  
  //Next Test is for rare case -- February and it's leap year (not necessary, just extra)
  
  @Test
  public void SearchByLocationFebruaryDayException() throws Exception {
      
      exception = assertThrows(Exception.class, () -> Data.convert("02/31/2011"));
      System.out.println("Date Throws Invalid Day Exception for February 31st = ...");
      assertEquals("Date Throws Invalid Day Exception = Fail", "Invalid DATE: day",exception.getMessage());
      System.out.println("  --Passed!");
  }
  
  @Test
  public void SearchByLocationLeapYearDay() throws Exception {
      
      System.out.println("Date Throws NO Exception for February 29th on leap year = ...");
      assertArrayEquals("Date Converts With February 29th on Leap Year = Fail", new int[]{02, 29, 2016},Data.convert("02/29/2016"));
      System.out.println("  --Passed!");
  }

}
