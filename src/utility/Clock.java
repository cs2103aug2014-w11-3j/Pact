package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Clock {
    
    public enum TimeType {
        NONE, TIME, DATE
    };
    
    public static String FORMAT_COMMON = "dd/MM/yyyy HH:mm";
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
    
    public String getCurrentTime() {
        Date date = new Date();
        return new SimpleDateFormat(FORMAT_COMMON).format(date);
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
    
}
