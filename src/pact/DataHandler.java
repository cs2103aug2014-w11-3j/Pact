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
    
    public int addTask(Task task) {
        data.add(task);
        saveFile();
        return 0;
    }
        
    public ArrayList<Task> deleteTask(String keyword, boolean isDeleting) {
        ArrayList<Task> taskToDelete = searchTask(keyword, true, "", "", true);
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < taskToDelete.size(); ++j) {
                if (data.get(i).content.equals(taskToDelete.get(j).content)) {
                    if (isDeleting) {
                        data.remove(i);
                    } else {
                        data.get(i).isArchived = true;
                    }
                    break;
                }
            }
        }
        saveFile();
        return taskToDelete;
    }
        
    public ArrayList<Task> updateTask(String keyword, String newContent, String start, String end) {
        ArrayList<Task> taskToDelete = searchTask(keyword, true, "", "", false);
        for (int i = 0; i < data.size(); ++i) {
            for (int j = 0; j < taskToDelete.size(); ++j) {
                if (data.get(i).content.equals(taskToDelete.get(j).content)) {

                    if (!newContent.isEmpty()) {
                        data.get(i).content = newContent; 
                    }
                    if (data.get(i).type.equals(Keyword.TIMED)) {
                        if (!start.isEmpty()) {
                            data.get(i).start = start;
                        }
                        if (!end.isEmpty()) {
                            data.get(i).end = end;
                        }
                    } else if (data.get(i).type.equals(Keyword.DEADLINE)) {
                        if (!end.isEmpty()) {
                            data.get(i).end = end;
                        }
                    } 
                    break;

                }
            }
        }
        saveFile();
        return taskToDelete;
    }
        
    public ArrayList<Task> searchTask(String keyword, boolean isExact, String start, String end, boolean isArchivedIncluded) {
        ArrayList<Task> result = new ArrayList<Task>();
        for (int i = 0; i < data.size(); ++i) {
            if (!isArchivedIncluded) {
                if (data.get(i).isArchived) {
                    continue;
                }
            }
            if (isExact) {
                if (!data.get(i).content.contentEquals(keyword)) {
                    continue;
                }
            } else {
                if (!data.get(i).content.contains(keyword)) {
                    continue;
                }
            }
            if (!start.isEmpty() || !end.isEmpty()) {
                String taskStart = data.get(i).start;
                String taskEnd = data.get(i).end;
                if (data.get(i).type.equals(Keyword.FLOATING)) {
                    continue;
                }
                if (data.get(i).type.equals(Keyword.DEADLINE)) {
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
    
    public void undo() {
        restoreFile();
    }
    
    private void writeToFile(BufferedWriter writer) throws IOException {
        for (int i = 0; i < data.size(); ++i) {
            writer.write("{\n");
            writer.write(data.get(i).toString());
            writer.write("}\n");
        }
    }
    
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
    
    private void createNewFile() {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            
        }
    }
    
    private void backupFile() {
        previousData = new ArrayList<Task>();
        for (int i = 0; i < data.size(); ++i) {
            previousData.add(data.get(i));
        }
    }
    
    private void restoreFile() {
        data = new ArrayList<Task>();
        for(int i = 0; i < data.size(); ++i) {
            data.add(previousData.get(i));
        }
    }
    
    private void saveFile() {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            backupFile();
            writeToFile(writer);
            writer.close();
        } catch (IOException e) {

        }
    }
    
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
