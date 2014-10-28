package pact;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import jline.SimpleCompletor;
import parser.Parser;
import utility.Keyword;

public class CommandLineInterface {

	private Scanner scanner = new Scanner(System.in);
	private String[] commandList = new String[] { "create", "update", "delete", "search", "display", "undo" };

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CommandLineInterface cli = new CommandLineInterface();
		Parser commandParser = new Parser();
		EventHandler logic = new EventHandler();
		HashMap<Keyword, String> parsedCommand;
		ArrayList<String> result;
		System.out.println("Welome to PACT");
		System.out.println("Available commands : \"create\", \"update\", \"delete\", \"search\", \"display\", \"undo\"");
		while (true) {
			try {
				parsedCommand = commandParser.parse(cli.getUserCommand());
				result = logic.determineCommand(parsedCommand);
				cli.contructTable(result);
			} catch (Exception pe) {
				//System.out.println("Your command could not be processed.Please try again!");
				cli.printErrorMessage(pe.getMessage());
			}

		}

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
	private void contructTable(ArrayList<String> result) {

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
			String[] splitString = result.get(i).split(":", 2);
			addSerialNumber(i, sb);
			addTaskName(splitString, sb);
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
		if (splitString.length == 1) {
			sb.append("-          |-     |-          |-     |");
		} else {
			String temp = splitString[1];
			splitString = temp.split(" ");
			addDateAndTime(splitString, sb);
		}
	}

	/**
	 * print header of table
	 */
	private void printHeader() {
		System.out
				.println("****************************************************************************");
		System.out
				.println("|     |                              |     Start        |      End         |");
		System.out
				.println("| S/N |            TaskName          --------------------------------------|");
		System.out
				.println("|     |                              | Date      |Time  | Date      |Time  |");
		System.out
				.println("|--------------------------------------------------------------------------|");

	}

	/**
	 * construct Date and Time component of the table
	 * @param splitString
	 * @param sb
	 */
	private void addDateAndTime(String[] splitString, StringBuilder sb) {
		if (splitString.length == 1) {
			sb.append("-          |-     |" + splitString[0] + " |-     |");
		} else if (splitString.length == 2) {
			if (splitString[1].length() == 10) {
				sb.append(splitString[0] + " |-     |" + splitString[1]
						+ " |-     |");
			} else {
				sb.append("-          |-     |" + splitString[0] + " |"
						+ splitString[1] + " |");
			}
		} else if (splitString.length == 4) {
			sb.append(splitString[0] + " |" + splitString[1] + " |"
					+ splitString[2] + " |" + splitString[3] + " |");
		}
	}

	/**
	 * construct task name component of the table
	 * @param splitString
	 * @param sb
	 */
	private void addTaskName(String[] splitString, StringBuilder sb) {
		int length = splitString[0].length();
		if (length < 30) {
			int numberOfSpaces = 30 - length;
			sb.append(splitString[0] + "");
			for (int j = 0; j < numberOfSpaces; j++) {
				sb.append(" ");
			}
			sb.append("|");
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
		System.out.println("****************************************************************************");		
	}

	private String readLine(ConsoleReader reader)
            throws IOException {
        String line = reader.readLine("");
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
			command = readLine(reader);
			out.flush();
		} while (command.isEmpty());
		return command;
	}

}