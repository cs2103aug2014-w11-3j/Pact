package pact;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utility.Clock;
import utility.Keyword;
import utility.Task;

public class DataHandler {
    private final String fileName = "data.txt";
            
    private ArrayList<Task> data;
    private ArrayList<Task> previousData;
    private ArrayList<Task> temp;
      
    /**
     * Initialize the DataHandler
     */
    public DataHandler() {
        data = new ArrayList<Task>();
        previousData = new ArrayList<Task>();
        loadFile();
    }
   
    /*
    public static void main(String[] args) {
        Task t = new Task();
        DataHandler d = new DataHandler();
        t.taskName = "gajah";
        t.type = TASK_TYPE.FLOATING;
        t.startTime = new Date(2014-1900,8,22,17,50,30);
        d.addTask(t);
        for (int i = 0; i < d._data.size(); ++i) {
            System.out.println(d.convertTaskToString(d._data.get(i)));
        }
    }
    */
    
    /**
     * Add a task to file
     * @param task
     * @return status
     */
    public void createTask(Task task) {
    	backupFile();
        data.add(task);
        saveFile();
      
    }
    
    /**
     * Archive/delete task
     * @param keyword
     * @param isDeleting
     * @return Task that are deleted
     */
    public ArrayList<Task> deleteTask(String keyword, boolean isDeleting) {
    	backupFile();
        ArrayList<Task> taskToDelete = readTask(keyword, false, "", "", false, true);
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < taskToDelete.size(); ++j) {
                if (data.get(i).getValue(Keyword.CONTENT).equals(taskToDelete.get(j).getValue(Keyword.CONTENT))) {
                    if (isDeleting) {
                        data.remove(i);
                        --i;
                    } else {
                        data.get(i).setValue(Keyword.ARCHIVED, "true");
                    }
                    break;
                }
            }
        }
        saveFile();
        return taskToDelete;
    }
        
    /**
     * Updates Task
     * @param keyword
     * @param newContent
     * @param start
     * @param end
     * @return Task that are updated
     */
    public ArrayList<Task> updateTask(String keyword, String newContent, String start, String end, String isCompleted) {
    	backupFile();
        ArrayList<Task> taskToDelete = readTask(keyword, false, "", "", false, true);
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < taskToDelete.size(); ++j) {
                if (data.get(i).getValue(Keyword.CONTENT).equals(taskToDelete.get(j).getValue(Keyword.CONTENT))) {
                    if (!newContent.isEmpty()) {
                        data.get(i).setValue(Keyword.CONTENT, newContent); 
                    }
                    if(!isCompleted.isEmpty()){
                    	data.get(i).setValue(Keyword.COMPLETED, isCompleted);
                    }
                    /*if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.TIMED)) {
                        if (!start.isEmpty()) {
                            data.get(i).setValue(Keyword.START, start);
                        }
                        if (!end.isEmpty()) {
                            data.get(i).setValue(Keyword.END, end);
                        }
                    } else if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.DEADLINE)) {
                        if (!end.isEmpty()) {
                            data.get(i).setValue(Keyword.END, end);
                        }
                    } */
                    if(!start.isEmpty() && !end.isEmpty()){
                    	if (!Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.DEADLINE)){
                    		data.get(i).setValue(Keyword.TYPE, String.valueOf(Keyword.DEADLINE));
                   		}
                   		data.get(i).setValue(Keyword.START, start);
                   		data.get(i).setValue(Keyword.END, end);
                   	}else if (!start.isEmpty()) {
                       	if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.DEADLINE)) {
                       		data.get(i).setValue(Keyword.TYPE, String.valueOf(Keyword.TIMED));
                       	}
                       	data.get(i).setValue(Keyword.START, start);
                   	}else if (!end.isEmpty()) {
                       	if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.FLOATING)){
                       		data.get(i).setValue(Keyword.TYPE, String.valueOf(Keyword.DEADLINE));
                       	}
                       	data.get(i).setValue(Keyword.END, end);
                    }else {
                        if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.DEADLINE)){
                            data.get(i).setValue(Keyword.TYPE, String.valueOf(Keyword.FLOATING));
                        }
                    }
                    break;

                }
            }
        }
        saveFile();
        return taskToDelete;
    }
        
    /**
     * Get task from database
     * @param keyword
     * @param isExact
     * @param start
     * @param end
     * @param isArchivedIncluded
     * @return Tasks to be read
     */
    public ArrayList<Task> readTask(String keyword, boolean isExact, String start, String end, boolean isArchivedIncluded, boolean isCompletedIncluded) {
        ArrayList<Task> result = new ArrayList<Task>();
        for (int i = 0; i < data.size(); ++i) {
            if (!isArchivedIncluded) {
                if (data.get(i).getValue(Keyword.ARCHIVED).equals("true")) {
                    continue;
                }
            }
            if (!isCompletedIncluded) {
                if (data.get(i).getValue(Keyword.COMPLETED).equals("true")) {
                    continue;
                }
            }
            if (isExact) {
                if (!data.get(i).getValue(Keyword.CONTENT).contentEquals(keyword)) {
                    continue;
                }
            } else {
                if (!data.get(i).getValue(Keyword.CONTENT).contains(keyword)) {
                    continue;
                }
            }
            if (!start.isEmpty() || !end.isEmpty()) {
                String taskStart = data.get(i).getValue(Keyword.START);
                String taskEnd = data.get(i).getValue(Keyword.END);
                /*
                if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.FLOATING)) {
                    continue;
                }
                */
                if (Keyword.getMeaning(data.get(i).getValue(Keyword.TYPE)).equals(Keyword.DEADLINE)) {
                    taskStart = taskEnd;
                }
                Clock clock = new Clock();
                if (!start.isEmpty()) {
                    if (clock.parseFromCommonFormat(start) > clock.parseFromCommonFormat(taskEnd)) {
                        continue;
                    }
                }
                if (!end.isEmpty()) {
                    if (clock.parseFromCommonFormat(end) < clock.parseFromCommonFormat(taskStart)) {
                        continue;
                    }
                }
            }
            result.add(data.get(i));
        }
        return result;
    }
    
    /**
     * Undo to the previous state
     * @return status
     */
    public void undo() {
        restoreFile();
       
    }
    
    /**
     * Write to file with appropriate formatting
     * @param writer
     * @throws IOException
     */
    private void writeToFile(BufferedWriter writer) throws IOException {
        for (int i = 0; i < data.size(); ++i) {
            writer.write("{\n");
            writer.write(data.get(i).toString());
            writer.write("}\n");
        }
    }
    
    /**
     * Get contents of file
     * @param read
     */
    private void getData(BufferedReader read) {
        try {
            //while have not reached EOF
            String text = read.readLine(); //read {
            while (text != null) {
                text = text.trim();
                String operation = "";
                while (!text.equals("}")) {
                    text = read.readLine();
                    if (!text.equals("}")) {
                        operation = operation + text + "\n";
                    }
                }
                data.add(Task.parseTask(operation));
                text = read.readLine(); //read {
            }
        } catch (IOException e) {
        }
    }
    
    /**
     * Create a new file
     */
    private void createNewFile() {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Backup existing file
     */
    private void backupFile() {
        previousData = new ArrayList<Task>();
        for (int i = 0; i < data.size(); ++i) {
        	Task toAdd = new Task(data.get(i));
            previousData.add(toAdd);
        }
    }
    
    /**
     * Restore previous file
     */
    private void restoreFile() {
    	temp = new ArrayList<Task>();
        for(int i = 0; i < data.size(); ++i) {
        	Task toAdd = new Task(data.get(i));
            temp.add(toAdd);
        }
        data = new ArrayList<Task>();
        for(int i = 0; i < previousData.size(); ++i) {
            data.add(previousData.get(i));
        }
        previousData = new ArrayList<Task>();
        for(int i = 0; i < temp.size(); ++i) {
            previousData.add(temp.get(i));
        }
        saveFile();
    }
    
    /**
     * Save file
     */
    private void saveFile() {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);           
            writeToFile(writer);
            writer.close();
        } catch (IOException e) {

        }
    }
    
    /**
     * load file to be read
     */
    private void loadFile() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader read = new BufferedReader(fr);
            getData(read);
            read.close();
        } catch (FileNotFoundException e) {
            createNewFile();
        } catch (IOException e) {
        }
    }

}
