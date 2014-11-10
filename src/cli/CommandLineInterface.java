package cli;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;

import pact.EventHandler;
import parser.Parser;
import utility.Clock;
import utility.Keyword;

public class CommandLineInterface {
    public static String[] fullDays = { "", "Sunday", "Monday", "Tuesday", 
                                        "Wednesday", "Thursday", "Friday", 
                                        "Saturday" };
    private static final String cliName = CommandLineInterface.class.getName();
    private static final Logger logger = Logger.getLogger(cliName);

    //@author A0119656W
    /**
     * main method
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Handler fh = new FileHandler("test.log", true);
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setLevel(Level.FINE);
        //append is true
        FileOutputStream fos = new FileOutputStream("test.log", true);
        PrintStream outPS = new PrintStream(new BufferedOutputStream(fos));  
        
        System.setErr(outPS); // redirect System.err

        CommandLineInterface cli = new CommandLineInterface();
        Parser commandParser = new Parser();
        EventHandler logic = new EventHandler();

        HashMap<Keyword, String> parsedCommand;
        ArrayList<String> result;
        Clock clock = new Clock();
               
        //reader.clearScreen();
        String userCommand;
        cli.printWelcomeMessage();
        while (true) {
            try {
                userCommand = cli.getUserCommand();
                parsedCommand = commandParser.parse(userCommand);
                result = logic.determineCommand(parsedCommand);
                if (cli.printTable(userCommand)) {
                    cli.constructTable(result);
                } else {
                    for (int i = 0; i < result.size(); i++) {
                        System.out.println(result.get(i));
                    }
                }
                if (Keyword.getMeaning(userCommand).equals(Keyword.EXIT)) {
                    cli.confirmExit();
                }
            } catch (Exception pe) {
                if (pe.getMessage().equals("exit from program")) {
                    String msg = clock.getExitGreeting(clock.getCurrentHour());
                    System.out.println(msg);
                    return;
                }
                logger.info("Logging from second try-catch begins"); 
                logger.log(Level.SEVERE, pe.getMessage(), pe);
                logger.info("Logging from second try-catch ends "); 
                cli.printErrorMessage(pe.getMessage());
            }
        }
    }
    
    //@author A0119656W
    private String getTimeMessage() throws Exception {
        Clock clock = new Clock();
        String currentTime = clock.getCurrentDateAndTime();
        return clock.getGreeting(clock.getCurrentHour()) + "Date: " +
               clock.getDay(currentTime) + " " + 
               clock.getMonth(currentTime) + " " + 
               clock.getYear(currentTime) + ", " + 
               fullDays[clock.getDayOfTheWeek(clock.getDate(currentTime))] + 
               "\nTime: " + clock.getTime(currentTime) + "\n";
    }
    
    //@author A0119656W
    private void printWelcomeMessage() {
        try {
            System.out.println("WELCOME TO PERSONAL ASSISTANT " +
                               "COORDINATOR TOOL(PACT)!\n");
            System.out.println(getTimeMessage());
            System.out.println("Commands : \"create\", \"update\", " +
                               "\"delete\", \"search\", \"display\", " +
                               "\"undo\", \"complete\", \"exit\"");
        } catch (Exception e) {
            logger.info("Logging from first try-catch begins"); 
            logger.log(Level.SEVERE, e.getMessage(), e);
            logger.info("Logging from first try-catch ends "); 
        }
    }
    //@author A0101331H
    private void confirmExit() throws Exception {
        Clock clock = new Clock();
        System.out.println("Are you sure you want to exit? [Y/N]");
        if (this.getUserCommand().equalsIgnoreCase("Y")) {
            System.out.println(clock.getExitGreeting(clock.getCurrentHour()));
            System.exit(0);
        }
    }
	//@author A0101331H
    private boolean printTable(String userCommand) {
        String[] arr;
        arr = userCommand.split(" ", 2);
        if (!Keyword.getMeaning(arr[0]).equals(Keyword.EMPTYSLOT)) {
            return true;
        }
        return false;
    }
    //@author A0113012J
    /**
     * Print error messages to user
     * @param message
     */
    private void printErrorMessage(String message) {
        System.out.println("\nERROR!");
        System.out.println(repeat("*", 90));
        System.out.println("Your command could not be processed. " + 
                           "Please try again!");
        System.out.println("");
        System.out.println("Cause of Error:");
        System.out.println(message);
        System.out.println(repeat("*", 90));
        
    }
    //@author A0101331H   
    /**
     * Construct table for the user
     * @param result
     */
    private void constructTable(ArrayList<String> result) {
        if (result.size() >= 2) {
            System.out.println(result.get(0));
            printHeader();
            constructContents(result);
            printEndLine();
        } else {
            System.out.println(result.get(0));
        }
    }
    //@author A0101331H
    /**
     * print header of table
     */
    private void printHeader() {
        System.out.println(repeat("*", 100));
        System.out.println(createRow(new String[] { "", "", "", 
                                                    "Start", "End", "No of" }, 
                                     new int[]{ 5, 30, 6, 23, 23, 6 }) + "|");
        System.out.println(createRow(new String[] { "S/N", "TaskName", "Done", 
                                                    repeat("-", 47), "Days" }, 
                                     new int[]{ 5, 30, 6, 47, 6 }) + "|");
        System.out.println(createRow(new String[] { repeat(" ", 5), 
                                                    repeat(" ", 30), 
                                                    repeat(" ", 6), 
                                                    "Date", "Time", "Day", 
                                                    "Date", "Time", "Day", 
                                                    "Left" }, 
                                     new int[]{ 5, 30, 6, 11, 6, 
                                                4, 11, 6, 4, 6}) + "|");
        System.out.printf("|%s|\n", repeat("-", 98));
    }
    //@author A0101331H
    /**
     * construct contents from result to structured string and print
     * @param result
     */
    private void constructContents(ArrayList<String> result) {
        for (int i = 1; i < result.size(); i++) {
            StringBuilder sb = new StringBuilder();
            String[] splitString = result.get(i).split(":", 3);
            addSerialNumber(i, sb);
            addTaskNameAndCompleted(splitString, sb);
            processDateAndTime(splitString, sb);
            sb.append("|");
            System.out.println(sb.toString());
        }

    }
    //@author A0101331H
    /**
     * construct serial number component of the table
     * @param i
     * @param sb
     */
    private void addSerialNumber(int i, StringBuilder sb) {
        sb.append(createRow(new String[]{ "" + i + "." }, new int[]{ 5 }));
    }
    //@author A0101331H
    /**
     * construct task name component of the table
     * @param splitString
     * @param sb
     */
    private void addTaskNameAndCompleted(String[] splitString, 
                                         StringBuilder sb) {
        if (splitString[1].equals("false")) {
            sb.append(createRow(new String[]{ splitString[0], "" }, 
                                new int[]{ 30, 6 }));
        } else {
            sb.append(createRow(new String[]{ splitString[0], "OK" }, 
                                new int[]{ 30, 6 }));
        }
    }
    //@author A0101331H
    /**
     * process the Date And Time and calls addDateAndTime method
     * @param splitString
     * @param sb
     */
    private void processDateAndTime(String[] splitString, StringBuilder sb) {
        if (splitString[2].equals("")) { //floating tasks
            sb.append(createRow(new String[]{ "-", "-", "-", 
                                              "-", "-", "-", "" }, 
                                new int[]{ 11, 6, 4, 11, 6, 4, 6 }));
        } else {
            String temp = splitString[2];
            splitString = temp.split(" ");
            try {
                addDateAndTime(splitString, sb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //@author A0101331H
    /**
     * construct Date and Time component of the table
     * @param arg
     * @param sb
     * @throws Exception 
     */
    private void addDateAndTime(String[] arg, 
                                StringBuilder sb) throws Exception {
        int[] formatter = new int[]{ 11, 6, 4, 11, 6, 4, 6 };
        String st = "";
        Clock clock = new Clock();
        String[] arr = new String[]{ "", "Sun", "Mon", "Tue", 
                                       "Wed", "Thu", "Fri", "Sat" };
        if (arg.length == 1) {
            st = createRow(new String[]{ "-", "-", "-", 
                                         arg[0], "-", "-", 
                                         addNoOfDaysLeft(arg[0] + " 23:59") }, 
                           formatter);
        } else if (arg.length == 2) {
            if (arg[1].length() == 10) {
                st = createRow(new String[]{ arg[0], "-", 
                                             arr[clock.getDayOfTheWeek(arg[0])], 
                                             arg[1], "-", 
                                             arr[clock.getDayOfTheWeek(arg[1])], 
                                             addNoOfDaysLeft(arg[1] + " 23:59") 
                                           }, formatter);
            } else {
                st = createRow(new String[]{ "-", "-", "-", 
                                             arg[0], arg[1], 
                                             arr[clock.getDayOfTheWeek(arg[0])],
                                             addNoOfDaysLeft(arg[0] + " " + 
                                                             arg[1]) }, 
                               formatter);
            }
        } else if (arg.length == 4) {
            st = createRow(new String[]{ arg[0], arg[1], 
                                         arr[clock.getDayOfTheWeek(arg[0])], 
                                         arg[2], arg[3], 
                                         arr[clock.getDayOfTheWeek(arg[2])], 
                                         addNoOfDaysLeft(arg[2] + " " + 
                                                         arg[3]) },
                           formatter);
        }
        sb.append(st);
    }
    //@author A0101331H
    private String addNoOfDaysLeft(String date) throws Exception {
        Clock clock = new Clock();
        String currentTime = clock.getCurrentDateAndTime();
        String day = clock.getDateDifference(currentTime, date);
        return day;
    }
    //@author A0101331H
    /**
     * print bottom line of table
     */
    private void printEndLine() {
        System.out.println(repeat("*", 100));
    }
    
    //@author A0113012J

    private String readLine(ConsoleReader reader) throws IOException {
        String line = reader.readLine(">> ");
        return line.trim();
    }
    
    //@author A0113012J
    /**
     * Get the user's command
     * 
     * @return command
     */
    private String getUserCommand() throws IOException { 
        // get from Scanner or something else
        String command;
        ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        List<SimpleCompletor> completors = new LinkedList<SimpleCompletor>();
        completors.add(new SimpleCompletor(Keyword.listAllCommands()));
        completors.add(new SimpleCompletor(Keyword.listAllArguments()));
        reader.addCompletor(new ArgumentCompletor(completors));
        PrintWriter out = new PrintWriter(System.out);
        do {
            command = readLine(reader);
            out.flush();
        } while (command.isEmpty());
        return command;
    }
    
    //@author A0119656W
    private String repeat(String st, int times) {
        if (st.isEmpty() || (times == 0)) return "";
        return new String(new char[times]).replace("\0", st);
    }
    
    //@author A0119656W
    private String alignCenter(String st, int counter) {
        int right = (counter - st.length()) / 2;
        int left = counter - st.length() - right;
        return repeat(" ", left) + st + repeat(" ", right);
    }
    
    //@author A0119656W
    private String createRow(String[] content, int[] spaces) {
        String output = "";
        int len = Math.min(content.length, spaces.length);
        for (int i = 0; i < len; ++i) {
            output = output.concat("|");
            output = output.concat(alignCenter(content[i], spaces[i]));
        }
        return output;
    }

}
