package pact;

import java.util.ArrayList;
//import java.util.Date;
import java.text.ParseException;
//import java.text.SimpleDateFormat; 

import pact.Task.TASK_TYPE;

public class EventHandler {
		public static ArrayList<String> array;
		private static Task task;
		private static int value;
		private static java.text.SimpleDateFormat formatter;
		private static ArrayList<Task> searchResult;
		private static DataHandler datahandler;
		
		public static void main(String[] args) throws ParseException {
			array = new ArrayList<String>();
			datahandler = new DataHandler();
			task = new Task();
			searchResult = new ArrayList<Task>();
			formatter = new java.text.SimpleDateFormat("ddMMyyy HHmm");
			determineCommand(array);
		}
		public static int determineCommand(ArrayList<String> parameters) throws ParseException{
			
			int commandType = Integer.parseInt(parameters.get(1));
			int status; 
			switch(commandType){			
			case 0:
				status = addTask(parameters);
				break;
			case 1:
				status = readFile(parameters);
				break;
			case 2:
				status = updateFile(parameters);
				break;
			case 3: 
				status = archiveTask(parameters);
				break;
			case 4:
				status = searchTask(parameters);
				break;
			default:
				status = -1;
				break;
			}
			searchResult.clear();
						
			return status;			
		}
		private static int addTask(ArrayList<String> parameters) throws ParseException {
			
			for (int index = 2; index < 10; index+=2) {
				value = index;
				switch(parameters.get(value)){
				case "taskName":
					task.taskName = parameters.get(value++);
					break;
				case "startTime":
					task.startTime = formatter.parse(parameters.get(value++));
					break;
				case "endTime":
					task.endTime = formatter.parse(parameters.get(value++));
					break;			
				case "type":
					task.type = TASK_TYPE.valueOf(parameters.get(value++));
					break;
				default:
					return -1;
				}
			}
			datahandler.addTask(task);
			return 0;
			
		}
		private static int readFile(ArrayList<String> parameters) {
			String display = "";
			datahandler.searchTask(display, searchResult);
			return 0;
			
		}
		private static int updateFile(ArrayList<String> parameters) throws ParseException {
			String field = null;
			String changeToValue = null;
			String nameOfTaskToBeUpdated = "";
			for (int index = 2; index < 5; index+=2) {
				value = index;
				
				switch(parameters.get(value)){
				case "taskName":
					nameOfTaskToBeUpdated = parameters.get(value++);
				case "field":
					field = parameters.get(value++);
					break;
				case "changeToValue":
					changeToValue = parameters.get(value++);
					break;
				default:
					return -1;
				}
				
			}
			
			datahandler.searchTask(nameOfTaskToBeUpdated, searchResult);
			
			Task editedTask = new Task(searchResult.get(0));
			
			
			switch(field){
			case "taskName":
				editedTask.taskName = changeToValue;
				break;
			case "startName":
				editedTask.startTime = formatter.parse(changeToValue);
				break;
			case "endTime":
				editedTask.endTime = formatter.parse(changeToValue);
				break;
			case "isCompleted":
				editedTask.isCompleted = Boolean.parseBoolean(changeToValue);
				break;
			case "isArchived":
				editedTask.isArchived = Boolean.parseBoolean(changeToValue);
				break;
			default:
				return -1;
			}
			datahandler.archiveTask(searchResult.get(0));
			datahandler.addTask(editedTask);
			return 0;
			
		}
		private static int archiveTask(ArrayList<String> parameters) {
	
			String nameOfTaskToBeDeleted = parameters.get(3);		
			datahandler.searchTask(nameOfTaskToBeDeleted, searchResult);
			datahandler.archiveTask(searchResult.get(0));
			return 0;
			
		}
		private static int searchTask(ArrayList<String> parameters) {
			
			String keyword = parameters.get(3);
			datahandler.searchTask(keyword, searchResult);
			return 0;
			
		}

		
}
