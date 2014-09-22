package pact;

import java.util.Date;

public class Task {
	public enum TASK_TYPE {
        FLOATING, DEADLINE, TIMED
    };
	
	public String taskName;
	public Date startTime;
	public Date endTime;
	public TASK_TYPE type;		
	public boolean isCompleted;
	public boolean isArchived;
};

