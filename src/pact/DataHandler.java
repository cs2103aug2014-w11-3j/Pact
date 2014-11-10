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
import utility.StringMatching;
import utility.Task;

public class DataHandler {
    private final String fileName = "data.txt";
            
    private ArrayList<Task> data;
    private ArrayList<Task> previousData;
    private ArrayList<Task> temp;
    
    private String nearMatchString = "";
     
    //@author A0113012J
    /**
     * Initialize the DataHandler
     */
    public DataHandler() {
        data = new ArrayList<Task>();
        previousData = new ArrayList<Task>();
        loadFile();
    }
    
    //@author A0113012J
    /**
     * Add a task to file
     * @param task task to be added
     */
    public void createTask(Task task) {
        backupFile();
        data.add(task);
        saveFile();
    }
    
    //@author A0113012J
    /**
     * Archive/delete task
     * @param keyword the keyword of the tasks to be deleted
     * @param isDeleting true if the task is to be deleted completely
     * 					false if the task is to be archived only
     * @return Task that are deleted
     */
    public ArrayList<Task> deleteTask(String keyword, boolean isDeleting) {
        backupFile();
        ArrayList<Task> taskToDelete = readTaskTolerance(keyword, true, "", "", 
                                                         false, true, 0);
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < taskToDelete.size(); ++j) {
                if (data.get(i).getValue(Keyword.CONTENT)
                        .equals(taskToDelete.get(j)
                                            .getValue(Keyword.CONTENT))) {
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
        if (taskToDelete.size() == 0) {
            //show the tolerance message if there's any
            readTask(keyword, true, "", "", false, true);
        }
        saveFile();
        return taskToDelete;
    }
    
    //@author A0113012J
    private void updateContent(Task task, String newContent) {
        if (!newContent.isEmpty()) {
            task.setValue(Keyword.CONTENT, newContent); 
        }
    }
    
    //@author A0113012J
    private void updateCompleted(Task task, String isCompleted) {
        if (!isCompleted.isEmpty()) {
            task.setValue(Keyword.COMPLETED, isCompleted);
        }
    }
    
    //@author A0113012J
    private void updateTime(Task task, String start, String end) {
        if (!start.isEmpty() && !end.isEmpty()) {
            if (!Keyword.getMeaning(task.getValue(Keyword.TYPE))
                                        .equals(Keyword.TIMED)) {
                task.setValue(Keyword.TYPE, String.valueOf(Keyword.TIMED));
            }
            task.setValue(Keyword.START, start);
            task.setValue(Keyword.END, end);
        } else if (!start.isEmpty()) {
            if (Keyword.getMeaning(task.getValue(Keyword.TYPE))
                       .equals(Keyword.DEADLINE)) {
                task.setValue(Keyword.TYPE, String.valueOf(Keyword.TIMED));
            }
            task.setValue(Keyword.START, start);
        } else if (!end.isEmpty()) {
            if (Keyword.getMeaning(task.getValue(Keyword.TYPE))
                       .equals(Keyword.FLOATING)) {
                task.setValue(Keyword.TYPE, String.valueOf(Keyword.DEADLINE));
            }
            task.setValue(Keyword.END, end);
        }
    }
      
    //@author A0113012J
    /**
     * Updates Task
     * @param keyword the keyword of the tasks to be updated
     * @param newContent the new task name
     * @param start the new start time
     * @param end the new end time
     * @param isCompleted the new value of isCompleted
     * @return Task that are updated
     */
    public ArrayList<Task> updateTask(String keyword, String newContent, 
                                      String start, String end, 
                                      String isCompleted) {
        backupFile();
        ArrayList<Task> taskToUpdate = readTask(keyword, false, "", "", 
                                                false, true);
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < taskToUpdate.size(); ++j) {
                if (data.get(i).getValue(Keyword.CONTENT)
                        .equals(taskToUpdate.get(j)
                        .getValue(Keyword.CONTENT))) {
                    updateContent(data.get(i),newContent);
                    updateCompleted(data.get(i),isCompleted);
                    updateTime(data.get(i),start,end);
                    break;
                }
            }
        }
        saveFile();
        return taskToUpdate;
    }
    
    //@author A0113012J
    /**
     * Get the list of tasks
     * @param keyword Keyword of a task to be search
     * @param isExact true if the keyword have to be a word of the taskname
     * 				false if the keyword can be any substring of the taskname
     * @param start all returned task will be the task after start.
     * 			leave it blank if you don't want to filter by time
     * @param end all returned task will be the task before end
     * @param isArchivedIncluded true if the archived task is included
     * @param isCompletedIncluded true if the completed task is included
     * @return
     */
    public ArrayList<Task> readTask(String keyword, boolean isExact, 
                                    String start, String end, 
                                    boolean isArchivedIncluded, 
                                    boolean isCompletedIncluded) {
        ArrayList<Task> noTolerance = readTaskTolerance(keyword, isExact, start, 
                                                        end, isArchivedIncluded, 
                                                        isCompletedIncluded, 0);
        if (noTolerance.size() > 0) {
            return noTolerance;
        }
       
        for (int tolerance = 1; tolerance <= 2; ++tolerance) {
            nearMatchString = "";
            ArrayList<Task> result = readTaskTolerance(keyword, isExact, start, 
                                                       end, isArchivedIncluded, 
                                                       isCompletedIncluded, 
                                                       tolerance);
            
            if (result.size() > 0) {
                return result;
            }
        }
        return new ArrayList<Task>();
    }
    
    /**
     * Get the value of nearMatchString because of the tollerance after search
     * @return
     */
    public String getNearMatchString() {
        return nearMatchString;
    }
    
    private boolean isPassArchiveCheck(Task task, boolean isArchivedIncluded) {
        return (isArchivedIncluded || task.getValue(Keyword.ARCHIVED)
                                          .equals("false"));
    }
    
    private boolean isPassCompleteCheck(Task task, boolean isCompleteIncluded) {
        return (isCompleteIncluded || task.getValue(Keyword.COMPLETED)
                                          .equals("false"));
    }
    
    private boolean isPassKeywordCheck(Task task, String key, int tolerance, 
                                       boolean isExact) {
        if (tolerance == 0 && !isExact) {
            nearMatchString = "";
            return task.getValue(Keyword.CONTENT).contains(key);
        }
        StringMatching sm = new StringMatching();
        String[] listOfWords = task.getValue(Keyword.CONTENT).split(" ");
        boolean wordFound = false;
        if (key.equals("")) {
            nearMatchString = "";
            return true;
        }
        for (int j = 0; j < listOfWords.length; ++j) {
            if (wordFound) {
                continue;
            }
            if (sm.computeEditDistance(listOfWords[j], key) <= tolerance) {
                if (tolerance == 0) {
                    nearMatchString = "";
                } else {
                    nearMatchString = listOfWords[j];
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean isPassTimeCheck(Task task, String start, String end) {
        if (start.isEmpty() && end.isEmpty()) {
            return true;
        }
        String taskStart = task.getValue(Keyword.START);
        String taskEnd = task.getValue(Keyword.END);
        if (Keyword.getMeaning(task.getValue(Keyword.TYPE))
                   .equals(Keyword.DEADLINE)) {
            taskStart = taskEnd;
        }
        Clock clock = new Clock();
        if (!start.isEmpty()) {
            if (clock.parseFromCommonFormat(start) > 
                clock.parseFromCommonFormat(taskEnd)) {
                return false;
            }
        }
        if (!end.isEmpty()) {
            if (clock.parseFromCommonFormat(end) < 
                clock.parseFromCommonFormat(taskStart)) {
                return false;
            }
        }
        return true;
    }
    
    //@author A0113012J
    private ArrayList<Task> readTaskTolerance(String keyword, boolean isExact, 
                                              String start, String end, 
                                              boolean isArchivedIncluded, 
                                              boolean isCompleteIncluded, 
                                              int tolerance) {
        ArrayList<Task> result = new ArrayList<Task>();
        for (int i = 0; i < data.size(); ++i) {
            if (!isPassArchiveCheck(data.get(i), isArchivedIncluded)) {
                continue;
            }
            if (!isPassCompleteCheck(data.get(i), isCompleteIncluded)) {
                continue;
            }
            if (!isPassKeywordCheck(data.get(i), keyword, tolerance, isExact)) {
                continue;
            }
            if (!isPassTimeCheck(data.get(i), start, end)) {
                continue;
            }
            result.add(data.get(i));
        }
        return result;
    }
    
    //@author A0113012J
    /**
     * Restore previous data.
     * Basically swap the previous data(before last operation) and current data
     */
    public void undo() {
    	//create a temporary arraylist of task
        temp = new ArrayList<Task>();
        //move everything in current data to the temporary data
        for(int i = 0; i < data.size(); ++i) {
            Task toAdd = new Task(data.get(i));
            temp.add(toAdd);
        }
        //move everything from previous data to current data
        data = new ArrayList<Task>();
        for(int i = 0; i < previousData.size(); ++i) {
            data.add(previousData.get(i));
        }
        //move everyting from temporary data to previous data
        previousData = new ArrayList<Task>();
        for(int i = 0; i < temp.size(); ++i) {
            previousData.add(temp.get(i));
        }
        saveFile();
    }
    
    //@author A0113012J
    /**
     * Write to file with appropriate formatting
     * @param writer BufferedWriter to write to the file
     * @throws IOException
     */
    private void writeToFile(BufferedWriter writer) throws IOException {
        for (int i = 0; i < data.size(); ++i) {
            writer.write("{\n");
            writer.write(data.get(i).toString());
            writer.write("}\n");
        }
    }
    
    //@author A0113012J
    /**
     * Get contents of file and put it to data arraylist
     * @param read BufferedReader to read from the file
     */
    private void getData(BufferedReader read) {
        try {
            String text = read.readLine();
            //while have not reached EOF
            while (text != null) { 
                text = text.trim();
                String operation = "";
                //each tasks are separated with {} like JSON file
                //read until reach '}' which means the end of a task
                while (!text.equals("}")) {
                    text = read.readLine();
                    if (!text.equals("}")) {
                        operation = operation + text + "\n";
                    }
                }
                data.add(Task.parseTask(operation));
                text = read.readLine();
            }
        } catch (IOException e) {
        }
    }
    
    //@author A0113012J
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
    
    //@author A0113012J
    /**
     * Backup existing file for undo purpose
     * Move everything from current data to previous data
     */
    private void backupFile() {
        previousData = new ArrayList<Task>();
        for (int i = 0; i < data.size(); ++i) {
            Task toAdd = new Task(data.get(i));
            previousData.add(toAdd);
        }
    }
    
    //@author A0113012J
    /**
     * Save file
     * Move everything in the data arraylist to the textfile
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
    
    //@author A0113012J
    /**
     * Load file
     * Move everything in the textfile to the arraylist
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
