package pact;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;
import parser.Parser;
import utility.Clock;
import utility.Keyword;

public class CommandLineInterface {
	public static String[] fullDays = new String[]{ "","Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}; 
	private String[] commandList = new String[] { "create", "update", "delete", "search", "display", "undo", "exit" };

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CommandLineInterface cli = new CommandLineInterface();
		Parser commandParser = new Parser();
		EventHandler logic = new EventHandler();
		Clock clock = new Clock();
		HashMap<Keyword, String> parsedCommand;
		ArrayList<String> result;
		
		String userCommand;
		try {
			System.out.println(clock.getGreeting(clock.getCurrentHour()) +"Welcome to PACT. The date today is " +clock.getDate(clock.getCurrentDateAndTime()) + " " 
					+ fullDays[clock.getDayOfTheWeek(clock.getDate(clock.getCurrentDateAndTime()))]  + " and the time now is "+ clock.getTime(clock.getCurrentDateAndTime()) + ".");
			System.out.println("Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\", \"complete\", \"exit\" ");
		} catch (Exception e) {
		}
		while (true) {
			try {
				userCommand = cli.getUserCommand();
				parsedCommand = commandParser.parse(userCommand);
				result = logic.determineCommand(parsedCommand);
				if(cli.printTable(userCommand)){
					cli.constructTable(result);
				}else{
					for (int i = 0; i <result.size(); i++) {
						System.out.println(result.get(i));
					}
				}
				
				if(Keyword.getMeaning(userCommand).equals(Keyword.EXIT)){
					System.out.println("Are you sure you want to exit? [Y/N]");
					if(cli.getUserCommand().equalsIgnoreCase("Y")){
						System.out.println(clock.getExitGreeting(clock.getCurrentHour()));
						System.exit(0);
					}
				}
			} catch (Exception pe) {
				//System.out.println("Your command could not be processed.Please try again!");
				if (pe.getMessage().equals("exit from program")) {
					System.out.println(clock.getExitGreeting(clock.getCurrentHour()));
				    return;
				}
			    cli.printErrorMessage(pe.getMessage());
			}

		}

	}

	private boolean printTable(String userCommand) {
		String[] arr;
		arr = userCommand.split(" ", 2);
		if(!Keyword.getMeaning(arr[0]).equals(Keyword.EMPTYSLOT)){
			return true;
		}
		return false;
	}

	/**
	 * Print error messages to user
	 * @param message
	 */
	private void printErrorMessage(String message) {
	    System.out.println("\nERROR!");
	    System.out.println("******************************************************************************************");
	    System.out.println("Your command could not be processed.Please try again!");
	    System.out.println("");
	    System.out.println("Cause of Error:");
	    System.out.println(message);
	    System.out.println("******************************************************************************************");
	    
	}
		
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
			System.out.println(sb.toString());
		}

	}

	/**
	 * process the Date And Time and calls addDateAndTime method
	 * @param splitString
	 * @param sb
	 */
	private void processDateAndTime(String[] splitString, StringBuilder sb) {
		if (splitString[2].equals("")) {
			sb.append("-          |-     |-   |-          |-     |-   |");
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

	/**
	 * print header of table
	 */
	private void printHeader() {
		System.out
				.println("*********************************************************************************************");
		System.out
				.println("|     |                              |      |     Start             |      End              |");
		System.out
				.println("| S/N |            TaskName          | Done ------------------------------------------------|");
		System.out
				.println("|     |                              |      | Date      |Time  |Day | Date      |Time  |Day |");
		System.out
				.println("|-------------------------------------------------------------------------------------------|");

	}

	/**
	 * construct Date and Time component of the table
	 * @param splitString
	 * @param sb
	 * @throws Exception 
	 */
	private void addDateAndTime(String[] splitString, StringBuilder sb) throws Exception {
		Clock clock = new Clock();
		String[] array = new String[]{ "","Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; 
		if (splitString.length == 1) {
			sb.append("-          |-     |-   |" + splitString[0] + " |-     |" + array[clock.getDayOfTheWeek(splitString[0])] + " |");
		} else if (splitString.length == 2) {
			if (splitString[1].length() == 10) {
				sb.append(splitString[0] + " |-     |" + array[clock.getDayOfTheWeek(splitString[0])] + " |" + splitString[1]
						+ " |-     |"+ array[clock.getDayOfTheWeek(splitString[1])] + " |");
			} else {
				sb.append("-          |-     |-   |" + splitString[0] + " |"
						+ splitString[1] + " |" + array[clock.getDayOfTheWeek(splitString[0])] + " |" );
			}

		} else if (splitString.length == 4) {
			sb.append(splitString[0] + " |" + splitString[1] + " |"+ array[clock.getDayOfTheWeek(splitString[0])] + " |"
					+ splitString[2] + " |" + splitString[3] + " |"+ array[clock.getDayOfTheWeek(splitString[2])] + " |");
		}
	}

	/**
	 * construct task name component of the table
	 * @param splitString
	 * @param sb
	 */
	private void addTaskNameAndCompleted(String[] splitString, StringBuilder sb) {
		int length = splitString[0].length();
		if (length < 30) {
			int numberOfSpaces = 30 - length;
			sb.append(splitString[0] + "");
			for (int j = 0; j < numberOfSpaces; j++) {
				sb.append(" ");
			}
			sb.append("|");
		}
		if(splitString[1].equals("false")){
			sb.append("      |");
		}else{
			sb.append("  OK  |");
		}
	}

	/**
	 * construct serial number component of the table
	 * @param i
	 * @param sb
	 */
	private void addSerialNumber(int i, StringBuilder sb) {
		int length = String.valueOf(i).length();
		int numberOfSpaces = 4 - length;
		sb.append("|" + i + ".");
		for (int j = 0; j < numberOfSpaces; j++) {
			sb.append(" ");
		}
		sb.append("|");

	}
	
	/**
	 * print bottom line of table
	 */
	private void printEndLine() {
		System.out.println("*********************************************************************************************");		
	}

	private String readLine(ConsoleReader reader)
            throws IOException {
        String line = reader.readLine(">> ");
        return line.trim();
    }
	
	/**
	 * Get the user's command
	 * 
	 * @return command
	 */
	private String getUserCommand() throws IOException { // get from Scanner or something else
		String command;
		
		ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        List<SimpleCompletor> completors = new LinkedList<SimpleCompletor>();
        completors.add(new SimpleCompletor(commandList));
        reader.addCompletor(new ArgumentCompletor(completors));
        PrintWriter out = new PrintWriter(System.out);
		
		do {
		    //System.out.print(">> ");
			command = readLine(reader);
			out.flush();
		} while (command.isEmpty());
		return command;
	}

}