package pact;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import utility.Task;
import utility.Task.TASK_TYPE;

public class DataHandler {
    private final String _fileName = "data.txt";
    
    private final String TASKNAME_KEY = "taskname";
    private final String STARTTIME_KEY = "start";
    private final String ENDTIME_KEY = "end";
    private final String TASKTYPE_KEY = "type";
    private final String ISCOMPLETED_KEY = "complete";
    private final String ISARCHIVED_KEY = "archive";
    
    private final String TASKTYPE_FLOATING = "floating";
    private final String TASKTYPE_DEADLINE = "deadline";
    private final String TASKTYPE_TIMED = "timed";
    
    private ArrayList<Task> _data;
    
    public DataHandler() {
        _data = new ArrayList<Task>();
        loadFile();
    }
    
    /**
     * Appends the specified task to the end of this Data.
     * @param taskToAdd task to be appended to this Data.
     * @return error code. 0 if there is no error
     */
    
    public int addTask(Task taskToAdd) {
        _data.add(taskToAdd);
        saveFile();
        return 0;
    }
    
    /**
     * Set the specified task to be archived.
     * @param taskToArchive task to be archived.
     * @return error code. 0 if there is no error
     */
    public int archiveTask(Task taskToArchive) {
        for (int i = 0; i < _data.size(); ++i) {
            if (_data.get(i).taskName.equals(taskToArchive.taskName) && _data.get(i).isArchived == false) {
                _data.get(i).isArchived = true;
                break;
            }
        }
        saveFile();
        return 0;
    }
    
    /**
     * Remove the specified task completely from the Data.
     * @param taskToDelete task to be deleted.
     * @return error code. 0 if there is no error
     */
    
    public int deleteTask(Task taskToDelete) {
        for (int i = 0; i < _data.size(); ++i) {
            if (_data.get(i).taskName.equals(taskToDelete.taskName) && _data.get(i).isArchived == false) {
                _data.remove(i);
                break;
            }
        }
        saveFile();
        return 0;
    }
    
    private boolean matchWhole(String keyword, String taskName) {
        String thisWord = "";
        for (int i = 0; i < taskName.length(); ++i) {
            if (taskName.charAt(i) == ' ') {
                if (thisWord.equals(keyword)) {
                    return true;
                }
                thisWord = "";
            } else thisWord = thisWord + taskName.charAt(i);
        }
        if (thisWord.equals(keyword)) {
            return true;
        }
        return false;
    }
    
    /**
     * search tasks in Data that matched the specified keyword
     * @param keyword keyword to be search
     * @param searchResult the result will be stored in this arraylist
     * @param searchWhole if true, check for words. if false, check for substring
     * @return error code. 0 if there is no error
     */
    
    public int searchTask(String keyword, ArrayList<Task> searchResult, boolean searchWhole) {
        if (searchWhole) {
            for (int i = 0; i < _data.size(); ++i) {
                if (_data.get(i).isArchived == false) {
                    if (matchWhole(keyword,_data.get(i).taskName)) {
                        searchResult.add(_data.get(i));
                    }
                }
            }
        } else {
            for (int i = 0; i < _data.size(); ++i) {
                if (_data.get(i).taskName.contains(keyword) && _data.get(i).isArchived == false) {
                    searchResult.add(_data.get(i));
                }
            }
        }
        return 0;
    }
    
    /**
     * undo the last command
     * @return error code. 0 if there is no error
     */
    
    public int undo() {
        return 0;
    }
    
    private String convertTypeToString(TASK_TYPE toConvert) {
        if (toConvert.equals(TASK_TYPE.FLOATING)) return TASKTYPE_FLOATING;
        if (toConvert.equals(TASK_TYPE.DEADLINE)) return TASKTYPE_DEADLINE;
        if (toConvert.equals(TASK_TYPE.TIMED))    return TASKTYPE_TIMED;
        return null;
    }
    
    private TASK_TYPE convertStringToType(String toConvert) {
        if (toConvert.equals(TASKTYPE_FLOATING)) return TASK_TYPE.FLOATING;
        if (toConvert.equals(TASKTYPE_DEADLINE)) return TASK_TYPE.DEADLINE;
        if (toConvert.equals(TASKTYPE_TIMED))    return TASK_TYPE.TIMED;
        return null;
    }
    
    private String convertTaskToString(Task toConvert) {
        String result = "";
        result = result + TASKNAME_KEY + ":" + toConvert.taskName + "\n";
        result = result + STARTTIME_KEY + ":" + toConvert.startTime + "\n";
        result = result + ENDTIME_KEY + ":" + toConvert.endTime + "\n";
        result = result + TASKTYPE_KEY + ":" + convertTypeToString(toConvert.type) + "\n";
        result = result + ISCOMPLETED_KEY + ":" + toConvert.isCompleted + "\n";
        result = result + ISARCHIVED_KEY + ":" + toConvert.isArchived + "\n";
        return result;
    }
    
    private Date convertStringToDate(String toConvert) {
        Date result = new Date();
        try {
            DateFormat d = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
            result = d.parse(toConvert);
            return result;
        } catch (ParseException e) {
        }
        return null;
    }
    
    private void operate(String keyword, String value, Task t) {
        if (keyword.equals(TASKNAME_KEY)) {
            t.taskName = value;
        } else if (keyword.equals(STARTTIME_KEY)) {
            t.startTime = convertStringToDate(value);
        } else if (keyword.equals(ENDTIME_KEY)) {
            t.endTime = convertStringToDate(value);
        } else if (keyword.equals(TASKTYPE_KEY)) {
            t.type = convertStringToType(value);
        } else if (keyword.equals(ISCOMPLETED_KEY)) {
            t.isCompleted = Boolean.parseBoolean(value);
        } else if (keyword.equals(ISARCHIVED_KEY)) {
            t.isArchived = Boolean.parseBoolean(value);
        }
    }
    
    private Task convertStringToTask(String toConvert) {
        Task result = new Task();
        Scanner scanner = new Scanner(toConvert);
        while (scanner.hasNextLine()) {
            String thisLine = scanner.nextLine();
            int colonPosition = thisLine.indexOf(':');
            String keyword = thisLine.substring(0,colonPosition);
            String value = thisLine.substring(colonPosition + 1, thisLine.length());
            operate(keyword,value,result);
        }
        scanner.close();
        return result;
    }
    
    private void writeToFile(BufferedWriter writer) throws IOException {
        for (int i = 0; i < _data.size(); ++i) {
            writer.write("{\n");
            writer.write(convertTaskToString(_data.get(i)));
            writer.write("}\n");
        }
    }
    
    private void moveFileToData(BufferedReader read) {
        try {
            //while have not reached EOF
            String text = read.readLine();
            while (text != null) { 
                String operation = "";
                while (!text.equals("}")) {
                    text = read.readLine();
                    if (!text.equals("}")) {
                        operation = operation + text + "\n";
                    }
                }
                _data.add(convertStringToTask(operation));
                text = read.readLine();
            }
        } catch (IOException e) {
            
        }
    }
    
    private void createNewFile() {
        File file = new File(_fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            
        }
    }
    
    private void saveFile() {
        try {
            FileWriter fileWriter = new FileWriter(_fileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writeToFile(writer);
            writer.close();
        } catch (IOException e) {
            
        }
    }
    
    private void loadFile() {
        try{
            FileReader fr = new FileReader(_fileName);
            BufferedReader read = new BufferedReader(fr);
            moveFileToData(read);
            read.close();
        } catch (FileNotFoundException e) {
            createNewFile();
        } catch (IOException e) {
            
        }
    }

    /*public static void main(String[] args) {
        Task t = new Task();
        DataHandler d = new DataHandler();
        t.taskName = "gajah";
        t.type = TASK_TYPE.FLOATING;
        t.startTime = new Date(2014-1900,8,22,17,50,30);
        d.addTask(t);
        for (int i = 0; i < d._data.size(); ++i) {
            System.out.println(d.convertTaskToString(d._data.get(i)));
        }
    }*/
}
