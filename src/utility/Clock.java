package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Scanner;

public class Clock {
    
    private static String commonFormat = "dd/MM/yyyy HH:mm";
    private static String[] dateDictionary = { "ddMMyyyy", "ddMMMMyyyy", "MMMMddyyyy"};
    private static String[] timeDictionary = { "HHmm", "hhmma", "hha" };
    
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
            throw new Exception("zzzz");
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
            date = guess(dateString, dateDictionary);
            tmp.setTime(date);
            result.set(GregorianCalendar.DATE, tmp.get(GregorianCalendar.DATE));
            result.set(GregorianCalendar.MONTH, tmp.get(GregorianCalendar.MONTH));
            result.set(GregorianCalendar.YEAR, tmp.get(GregorianCalendar.YEAR));
        }
        if (!timeString.isEmpty()) {
            date = guess(timeString, timeDictionary);
            tmp.setTime(date);
            result.set(GregorianCalendar.HOUR_OF_DAY, tmp.get(GregorianCalendar.HOUR_OF_DAY));
            result.set(GregorianCalendar.MINUTE, tmp.get(GregorianCalendar.MINUTE));
        }
        return new SimpleDateFormat(commonFormat).format(result.getTime());
    }
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String st1 = sc.nextLine();
        String st2 = sc.nextLine();
        new Clock().parse(st1, st2);
        sc.close();
    }

}
