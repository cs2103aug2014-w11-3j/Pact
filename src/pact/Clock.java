package pact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Scanner;

public class Clock {
    
    private static String[] dateDictionary = { "ddMMyyyy", "ddMMMMyyyy", "MMMMddyyyy"};
    private static String[] timeDictionary = { "HHmm", "hhmmaa", "hhaa" };
    
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
        if (result.size() != 1) {
            throw new Exception("zzzz");
        } else {
            return result.iterator().next();
        }
    }
    
    public GregorianCalendar parse(String dateString, String timeString) throws Exception {
        GregorianCalendar result = new GregorianCalendar();
        GregorianCalendar tmp = new GregorianCalendar();
        dateString = dateString.replaceAll("\\W+", "");
        timeString = timeString.replaceAll("\\W+", "");
        System.out.println(dateString);
        System.out.println(timeString);
        Date date;
        if (!dateString.isEmpty()) {
            System.out.println("date detected");
            date = guess(dateString, dateDictionary);
            tmp.setTime(date);
            result.set(GregorianCalendar.DATE, tmp.get(GregorianCalendar.DATE));
            result.set(GregorianCalendar.MONTH, tmp.get(GregorianCalendar.MONTH));
            result.set(GregorianCalendar.YEAR, tmp.get(GregorianCalendar.YEAR));
        }
        if (!timeString.isEmpty()) {
            System.out.println("time detected");
            date = guess(timeString, timeDictionary);
            tmp.setTime(date);
            System.out.println(date.toString());
            result.set(GregorianCalendar.HOUR, tmp.get(GregorianCalendar.HOUR));
            result.set(GregorianCalendar.MINUTE, tmp.get(GregorianCalendar.MINUTE));
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String st1 = sc.nextLine();
        String st2 = sc.nextLine();
        new Clock().parse(st1, st2);
        sc.close();
    }

}
