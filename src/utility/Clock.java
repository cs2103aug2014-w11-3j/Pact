package utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Clock {
    
    public enum TimeType {
        NONE, TIME, DATE
    };
    
    public static String FORMAT_COMMON = "dd/MM/yyyy HH:mm";
    public static String FORMAT_HOUR = "HH";
    public static String FORMAT_DATE = "dd/MM/yyyy";
    private static String[] DICTIONARY_DATE = { "ddMMyyyy", "ddMMMMyyyy", "MMMMddyyyy"};
    private static String[] DICTIONARY_TIME = { "hhmma", "hha", "HHmm" };
    
    /**
     * Guess the time from input string using a dictionary_date or dictionary_time
     * @param input
     * @param dictionary
     * @return Date
     * @throws Exception
     */
    private Date guess(String input, String[] dictionary) throws Exception {
        for (int i = 0; i < dictionary.length; ++i) {
            SimpleDateFormat formatter = new SimpleDateFormat(dictionary[i]);
            try {
                Date date = formatter.parse(input);
                return date;
            } catch (ParseException e) {
                //not suitable, don't need to do anything
            }
        }
        throw new Exception("Cannot guess this time");
    }
    
    /**
     * Parse and set the time into GregorianCalendar format
     * @param dateString
     * @param timeString
     * @return formatted timeString
     * @throws Exception
     */
    public String parse(String dateString, String timeString) throws Exception {
        GregorianCalendar result = new GregorianCalendar();
        GregorianCalendar tmp = new GregorianCalendar();
        dateString = dateString.trim().replaceAll("\\W+", "");
        timeString = timeString.trim().replaceAll("\\W+", "").toUpperCase();
        if (timeString.length() % 2 != 0) {
            timeString = "0" + timeString;
        }
        Date date;
        if (!dateString.isEmpty()) {
            date = guess(dateString, DICTIONARY_DATE);
            tmp.setTime(date);
            result.set(GregorianCalendar.DATE, tmp.get(GregorianCalendar.DATE));
            result.set(GregorianCalendar.MONTH, tmp.get(GregorianCalendar.MONTH));
            result.set(GregorianCalendar.YEAR, tmp.get(GregorianCalendar.YEAR));
        }
        if (!timeString.isEmpty()) {
            date = guess(timeString, DICTIONARY_TIME);
            tmp.setTime(date);
            result.set(GregorianCalendar.HOUR_OF_DAY, tmp.get(GregorianCalendar.HOUR_OF_DAY));
            result.set(GregorianCalendar.MINUTE, tmp.get(GregorianCalendar.MINUTE));
        }
        return new SimpleDateFormat(FORMAT_COMMON).format(result.getTime());
    }
    
    public String getCurrentDateAndTime() {
        Date date = new Date();
        return new SimpleDateFormat(FORMAT_COMMON).format(date);
    }
    public String getCurrentHour() {
        Date date = new Date();
        return new SimpleDateFormat(FORMAT_HOUR).format(date);
    }
    public String getGreeting(String hour) {
        int time = Integer.parseInt(hour);
        if(time >= 0  && time < 12){
        	return "Good Morning and ";
        }else if(time >=12 && time < 17){
        	return "Good Afternoon and ";
        }else if(time >=17 && time < 19){
        	return "Good Evening and ";
        }else{     	
        	return "";
    	}
	}
    public String getExitGreeting(String hour) {
        int time = Integer.parseInt(hour);
        if(time >= 0  && time < 12){
        	return "Have an awesome day ahead. All the best!";
        }else if(time >=12 && time < 17){
        	return "Have a pleasant afternoon!";
        }else if(time >=17 && time < 19){
        	return "Have a sweet evening!";
        }else if(time >= 20 && time < 24){     	
        	return "Good Night!";
    	}else{
    		return "";
    	}
	}
    public int getDay(String source) {
        return Integer.parseInt(source.substring(0,2));
    }
    
    public String normalize(String toNormalize) throws Exception {
        String array[] = toNormalize.split(" ");
        return parse(array[0],array[1]);
    }
    
    public String getDate(String toSplit) throws Exception {
        String array[] = toSplit.split(" ");
        return array[0];
    }
    
    public String getTime(String toSplit) throws Exception {
        String array[] = toSplit.split(" ");
        return array[1];
    }
    public int getDayOfTheWeek(String dateString)throws Exception {
    	 GregorianCalendar tmp = new GregorianCalendar();
    	 dateString = dateString.replace("/", "");
    	 Date date = guess(dateString, DICTIONARY_DATE);
    	 tmp.setTime(date); 
    	 return tmp.get(GregorianCalendar.DAY_OF_WEEK);
    }

    
    /**
     * Parse source into GregorianCalendar format
     * @param source
     * @return
     */
    public long parseFromCommonFormat(String source) {
        GregorianCalendar result = new GregorianCalendar();
        try {
            result.setTime(new SimpleDateFormat(FORMAT_COMMON).parse(source));
        } catch (Exception e) {
        }
        return result.getTimeInMillis();
    }
    public boolean getDateDifference(HashMap<Keyword,String> parameters){
        String start = parameters.get(Keyword.START);
        String end = parameters.get(Keyword.END);
        String[] starts = start.split("/");
        String[] ends = end.split("/");
        String[] checkStartYear = starts[2].split(" ");
        String[] checkEndYear = ends[2].split(" ");
        int startDay = Integer.parseInt(starts[0]);
        int startMonth = Integer.parseInt(starts[1]);
        
        int startYear = Integer.parseInt(checkStartYear[0]);
        
        int endDay = Integer.parseInt(ends[0]);
        int endMonth = Integer.parseInt(ends[1]);
        int endYear = Integer.parseInt(checkEndYear[0]);
        
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        cal1.set(startYear,startMonth,startDay);
        cal2.set(endYear,endMonth,endDay);
        
        double days =(double) (cal2.getTime().getTime() - cal1.getTime().getTime()) / (1000 * 60 * 60 * 24);
      
        if(days<=7){
            return true;
        }
       
        return false;
    } 
}
