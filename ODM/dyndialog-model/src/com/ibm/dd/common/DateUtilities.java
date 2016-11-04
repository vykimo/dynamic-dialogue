package com.ibm.dd.common;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * The Date class, found in the java.util package, encapsulates a long value representing
 * a specific moment in time. One useful constructor is Date(), which creates a Date object
 * representing the time the object was created. 
 * As we need some other utilities function to create dates and compare them this utility class aims to 
 * cover all the basic date operations we may need in the rule processing.
 * 
 * @author Jerome boyer
 * @version 2.0
 * @created 10-Jun-2006 2:40:05 PM
 *
 */
public final class DateUtilities {

	/**
	 * Get the current date
	 * @return long value of the time between 01/01/1970 and now
	 */
	 public static Date now() {
	   	  return Calendar.getInstance().getTime();
	   }

	 /**
	  * Present the date in a String format
	  * @param date to present
	  * @return
	  */
	 public static String dateToString(Date d) {
		 //DateFormat class is to create Strings in ways that humans can easily deal with them
		 // getDateInstance() creates an object in the default format or style.
		 DateFormat df = DateFormat.getDateInstance();
		 return df.format(d);
	 }
	 

	 /**
	  * Return a date object for a given year, month, and day 
	  * @param year
	  * @param month is from 1 to 12
	  * @param day 
	  * @return Date
	  */
	   public static Date makeDate(int year, int month, int day) {
	   	  Calendar cal = makeCalendarDate(year,month,day,0,0);
	 	  return cal.getTime();
	   }
	   
	   /**
	    * Generate a XML gregorian date
	    * @param year
	    * @param month
	    * @param day
	    * @param hour
	    * @param minute
	    * @return
	    */
	   public static XMLGregorianCalendar makeXmlDate(int year,int month, int day,int hour, int minute) {
		   GregorianCalendar cal = new GregorianCalendar(year,month-1,day);
		   cal.set(Calendar.MILLISECOND,0);
		   cal.set(Calendar.SECOND,0);
		   cal.set(Calendar.MINUTE,minute);
		   cal.set(Calendar.HOUR_OF_DAY,hour);
			try {
				return  DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
				return null;
			}
			
	   }
	  
	   public static Date getDate(XMLGregorianCalendar d) {
		   return d.toGregorianCalendar().getTime();
	   }
		
	   public static Calendar makeCalendarDate(int year, int month, int day){
		 return makeCalendarDate(year,month,day,0,0);
	   }
	   
	   
	   public static Calendar makeCalendarDate(int year, int month, int day,int hour, int minute){
		   Calendar cal = new GregorianCalendar(year,month-1,day);
		   cal.set(Calendar.MILLISECOND,0);
		   cal.set(Calendar.SECOND,0);
		   cal.set(Calendar.MINUTE,minute);
		   cal.set(Calendar.HOUR_OF_DAY,hour);
		 return cal;
	   }
	   
	   public static Date makeDate(int year, int month, int day, int hour, int minute) {
		   Calendar cal= makeCalendarDate(year,month,day,hour,minute);
 	 	   return cal.getTime();
	   }
	   
	   public static long makeTime(int year, int month, int day, int hour, int minute) {
		   return DateUtilities.makeDate(year,month,day,hour,minute).getTime();
	   }
	   
	   /**
	    * Add days to a date.
	    * @param date
	    * @param days
	    * @return
	    */
	   public static Date addDays(Date date, int days) {
          //One important side effect of the add() method is that it changes the original
	 	  //date. Sometimes it is important to have both the original date and the modified
	 	  //date. So here we give another date
	   	  Calendar cal = Calendar.getInstance();
	 	  cal.setTime(date);
	 	  //With the add() method, you can add such time units as years, months,
	 	  // and days to a date.
	 	  cal.add(Calendar.DATE,days);

	 	  return cal.getTime();
	   }

	   public static boolean withinYear(Date date){
		 Date ayear=addDays(new Date(),-365);
		 return date.after(ayear);
	   }
	   
	   public static Calendar addDays(Calendar date, int days) {
		   Calendar newDate = Calendar.getInstance();
		   newDate.setTime(date.getTime());
		   newDate.add(Calendar.DATE, days);
		   return newDate;
	   }
	   
	   /**
	    * Return the duration in days between two dates
	    * @param startDate
	    * @param endDate
	    * @return 
	    */
	   public static int getDuration(Date startDate, Date endDate) {
	   	  long startTime = startDate.getTime();
	   	  long endTime = endDate.getTime();
	   	  return (int)((endTime - startTime) / (24*3600*1000));
	   }

	   /**
	    * Get the age of a person given his birth date and a date to compute his age
	    * @param birthDate
	    * @param nowDate
	    * @return
	    */
	   public static int getAge(Date birthDate, Date nowDate) {	    
		    Calendar birth = Calendar.getInstance();
		    birth.setTime(birthDate);
		    
		    // Create a calendar object with today's date
		    Calendar now = Calendar.getInstance();
		    now.setTime(nowDate);
		    
		    // Get age based on year
		    int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
		    
		    // Add the tentative age to the date of birth to get this year's birthday
		    birth.add(Calendar.YEAR, age);
		    
		    // If this year's birthday has not happened yet, subtract one from age
		    if (now.before(birth)) {
		        age--;
		    }  
		    return age;   	
	   }
	   
	   /**
	    * Compare two dates
	    * @param date1
	    * @param date2
	    * @return
	    */
		public static boolean areEqual(Date date1, Date date2) {
			boolean isEqual = false;
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date1);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(date2);

			if(calendar1.get(Calendar.DATE) == calendar2.get(Calendar.DATE) && 
				calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && 
				 calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) )
			 {
				isEqual = true;
			 }   

				return isEqual;
		}
		
		/**
		 * Compare a date to today
		 * @param date
		 * @return true if the date in argument is today
		 */
		public static boolean isEqualToToday(Date date) {
			Date today = new Date();
			
			return areEqual(date, today);
		}
		
		/**
		 * Return True if date 1 is before date 2
		 * @param date1
		 * @param date2
		 * @return true id date 1 is before date 2
		 */
		public static boolean before(Date date1, Date date2) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date1);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(date2);

			if(calendar1.get(Calendar.YEAR) > calendar2.get(Calendar.YEAR))
					return false;

			else if(calendar1.get(Calendar.YEAR) < calendar2.get(Calendar.YEAR) )
				return true;
			else // same year 
				if (calendar1.get(Calendar.MONTH) > calendar2.get(Calendar.MONTH))
					return false;
				else if (calendar1.get(Calendar.MONTH) < calendar2.get(Calendar.MONTH))
					 return true;
				else // same month
					return (calendar1.get(Calendar.DATE) < calendar2.get(Calendar.DATE) );
			 
		}
		
		/**
		 * Return True if date 1 is after date 2
		 * @param date1
		 * @param date2
		 * @return true id date 1 is before date 2
		 */
		public static boolean after(Date date1, Date date2) {
			return ! before(date1,date2);
		}

		public static boolean after(XMLGregorianCalendar d1, Date d2){
			
			return after(d1.toGregorianCalendar().getTime(),d2);
		}
		/**
		 * Add a number of month to the given date, and return a new date
		 * @param aDate
		 * @param month  between 0 and 11
		 * @return
		 */
		public static Date addMonths(Date aDate, int m) {
			//One important side effect of the add() method is that it changes the original
		 	  //date. Sometimes it is important to have both the original date and the modified
		 	  //date. So here we give another date
		   	  Calendar cal = Calendar.getInstance();
		 	  cal.setTime(aDate);
		 	  //With the add() method, you can add such time units as years, months,
		 	  // and days to a date.
		 	  cal.add(Calendar.MONTH,m);

		 	  return cal.getTime();
		}
		
		
} // class
