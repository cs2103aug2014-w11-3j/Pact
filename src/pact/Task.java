package pact;

import java.util.Date;

public class Task {
	public enum TASK_TYPE {
        FLOATING, DEADLINE, TIMED
    };
    
    public Task() {
        this.taskName = "";
        this.startTime = new Date();
        this.endTime = new Date();
        this.isCompleted = false;
        this.isArchived = false;
    }
    
    public Task(Task copy) {
    	this.taskName = copy.taskName;
    	this.startTime = copy.startTime;
    	this.endTime = copy.endTime;
    	this.isCompleted = copy.isCompleted;
    	this.isArchived = copy.isArchived;
    	
    	
    }
	public String taskName;
	public Date startTime;
	public Date endTime;
	public TASK_TYPE type;		
	public boolean isCompleted;
	public boolean isArchived;
};

