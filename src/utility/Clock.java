package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class Clock {
    
    public enum TimeType {
        NONE, TIME, DATE
    };
    
    public static String FORMAT_COMMON = "dd/MM/yyyy HH:mm";
    public static String FORMAT_DATE = "dd/MM/yyyy";
    private static String[] DICTIONARY_DATE = { "ddMMyyyy", "ddMMMMyyyy", "MMMMddyyyy"};
    private static String[] DICTIONARY_TIME = { "HHmm", "hhmma", "hha" };
    
    private Date guess(String input, String[] dict) throws Exception {
        HashSet<Date> result = new HashSet<Date>();
        for (int i = 0; i < dict.length; ++i) {
            SimpleDateFormat formatter = new SimpleDateFormat(dict[i]);
            try {
                Date date = formatter.parse(input);
                result.add(date);
            } catch (ParseException e) {
                //not suitable, don't need to do anything
            }
        }
        if (result.isEmpty()) {
            throw new Exception("Cannot guess this time");
        } else {
            return result.iterator().next();
        }
    }
    
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
    
    public long parseFromCommonFormat(String source) {
        GregorianCalendar result = new GregorianCalendar();
        try {
            result.setTime(new SimpleDateFormat(FORMAT_COMMON).parse(source));
        } catch (Exception e) {
        }
        return result.getTimeInMillis();
    }
    
}
