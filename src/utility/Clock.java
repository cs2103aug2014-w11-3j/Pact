package utility;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Clock {
    
    private final String WELCOME_MORNING = "Good Morning!\n";
    private final String WELCOME_AFTERNOON = "Good Afternoon!\n";
    private final String WELCOME_EVENING = "Good Evening!\n";
    
    private final String GOODBYE_MORNING = "Have an awesome day ahead. All the best!";
    private final String GOODBYE_AFTERNOON = "Have a pleasant afternoon!";
    private final String GOODBYE_EVENING = "Have a sweet evening!";
    private final String GOODBYE_NIGHT = "Good Night!";
    
    public enum TimeType {
        NONE, TIME, DATE
    };
    
    public static String FORMAT_COMMON = "dd/MM/yyyy HH:mm";
    public static String FORMAT_HOUR = "HH";
    public static String FORMAT_DATE = "dd/MM/yyyy";
    private static String[] DICTIONARY_DATE = { "ddMMyyyy", "ddMMMMyyyy", 
                                                "MMMMddyyyy"};
    private static String[] DICTIONARY_TIME = { "hhmma", "hha", "HHmm" };

    //@author A0119656W
    /**
     * Guess the time from input string using a dictionary_date 
     * or dictionary_time
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

    //@author A0119656W
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
            result.set(GregorianCalendar.DATE, 
                       tmp.get(GregorianCalendar.DATE));
            result.set(GregorianCalendar.MONTH, 
                       tmp.get(GregorianCalendar.MONTH));
            result.set(GregorianCalendar.YEAR, 
                       tmp.get(GregorianCalendar.YEAR));
        }
        if (!timeString.isEmpty()) {
            date = guess(timeString, DICTIONARY_TIME);
            tmp.setTime(date);
            result.set(GregorianCalendar.HOUR_OF_DAY, 
                       tmp.get(GregorianCalendar.HOUR_OF_DAY));
            result.set(GregorianCalendar.MINUTE, 
                       tmp.get(GregorianCalendar.MINUTE));
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
    
    //@author A0101331H
    public String getGreeting(String hour) {
        int time = Integer.parseInt(hour);

        if (time >= 0  && time < 12) {
            return WELCOME_MORNING;
        } else if (time >=12 && time < 17) {
            return WELCOME_AFTERNOON;
        } else if (time >=17 && time < 19) {
            return WELCOME_EVENING;
        } else {        
            return "";
        }
    }

    //@author A0101331H
    public String getExitGreeting(String hour) {
        int time = Integer.parseInt(hour);
        if (time >= 0  && time < 12) {
            return GOODBYE_MORNING;
        } else if (time >=12 && time < 17) {
            return GOODBYE_AFTERNOON;
        } else if (time >=17 && time < 19) {
            return GOODBYE_EVENING;
        } else if (time >= 20 && time < 24) {       
            return GOODBYE_NIGHT;
        } else {
            return "";
        }
    }
    
    public int getDay(String source) {
        return Integer.parseInt(source.substring(0, 2));
    }
    
    public int getYear(String source) {
        return Integer.parseInt(source.substring(6, 10));
    }
    
    public String normalize(String toNormalize) throws Exception {
        String array[] = toNormalize.split(" ");
        return parse(array[0],array[1]);
    }
    
    public String getDate(String dateAndTime) throws Exception {
        String array[] = dateAndTime.split(" ");
        return array[0];
    }
    public String getMonth(String dateString)throws Exception {
        GregorianCalendar tmp = new GregorianCalendar();
        dateString = dateString.replace("/", "");
        Date date = guess(dateString, DICTIONARY_DATE);
        tmp.setTime(date); 
        String[] monthArray = new String[]{"JAN", "FEB", "MAY","APR", "MAY", 
                                           "JUN", "JUL", "AUG","SEP", "OCT", 
                                           "NOV", "DEC"};
        return monthArray[tmp.get(GregorianCalendar.MONTH)];
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

    
    //@author A0119656W
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
    //@author A0101331H
    public String getDateDifference(String start,String end)throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_COMMON);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(start);
            endDate = formatter.parse(end);
        } catch (ParseException e) {
            throw new Exception("error in getting date difference");
        }
  
        double days = (double) ((endDate.getTime() - startDate.getTime()) / 
                                (1000.0* 60.0 * 60.0 * 24.0));
        DecimalFormat df = new DecimalFormat("#.##");
        String dateDifference = df.format(days);
        if (days > 365 ) {
            dateDifference = "";
        }
        return dateDifference;
    }

}
