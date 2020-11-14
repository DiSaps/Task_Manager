package gr.cup.mathesis.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


import java.util.logging.Level;

/**
 * 2. Implement this class
 * 
 */
public final class TaskManager implements TaskManagerInterface {
	protected List<Task> tasks = new ArrayList<>();
    private static TaskManager INSTANCE;

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskManager();
        }
        return INSTANCE;
    }
    
    @Override
    public List<Task> listAllTasks(final boolean priorityOrDate) {  // TODO
        if (priorityOrDate) {
    		Collections.sort(this.tasks, priorityComparator);
    		return Collections.unmodifiableList(tasks);
        }else {
        	List<Task> datePriority = new ArrayList<>();
        	List<Task> noDate = new ArrayList<>();
        	Iterator<Task> iter = this.tasks.iterator();
        	while (iter.hasNext()) {
        	  Task t = iter.next();
        	  if (t.getDueDate()!=null) {
        		  datePriority.add(t);
        	  }else noDate.add(t);
        	}
    		Collections.sort(datePriority, dueDateComparator);
    		datePriority.addAll(noDate);
    		return Collections.unmodifiableList(datePriority);
        }
    }
    
    @Override
    public List<Task> listTasksWithAlert() {  // TODO
        List<Task> tasksWithAlert = new ArrayList<>();
        for (Task t: this.listAllTasks(false)) {
        	if (t.hasAlert()) {
        		tasksWithAlert.add(t);
        	}
        }
        return tasksWithAlert;
    }
    
    @Override
    public List<Task> listCompletedTasks() {  // TODO
    	List<Task> tasksCompleted = new ArrayList<>();
    	List<Task> noDate = new ArrayList<>();
        for (Task t: this.listAllTasks(false)) {
        	if (t.isCompleted()) {
        		if (t.getDueDate() != null) {
        			tasksCompleted.add(t);
        		} else {
        			noDate.add(t);
        		}
        	}
        }
        tasksCompleted.addAll(noDate);
        return tasksCompleted;
    }
    
    @Override
    public void addTask(final Task task) { // TODO
        if (validate(task)) {
           this.tasks.add(task); 
        } else {
            Logger.getLogger(TaskManager.class.getName()).log(Level.WARNING, "Task validation failed.");
        }
    }
    
    @Override
    public void updateTask(final Task task) {  // TODO
        if (validate(task)) {
            int index = this.tasks.indexOf(task);
            this.tasks.set(index, task);
        } else {
            Logger.getLogger(TaskManager.class.getName()).log(Level.WARNING, "Task validation failed.");
        }
    }
    
    @Override
    public void markAsCompleted(final int id, final boolean completed) {  // TODO
    	for (Task t: this.tasks) {
        	if (t.getId() == id) {
        		t.setCompleted(completed);
        	}
        }
    }
    
    @Override
    public void removeTask(final int id) {  // TODO
    	Iterator<Task> iter = this.tasks.iterator();
    	while (iter.hasNext()) {
    	  Task t = iter.next();
    	  if (t.getId()==id) iter.remove();
    	}
    }

    private boolean validate(final Task task) {
        return !task.getDescription().isEmpty();
    }
    
    @Override
    public Task findTask(final int id) {  // TODO
    	Task result=null;
    	for (Task t: this.listAllTasks(false)) {
        	if (t.getId() == id) {
        		result = t;
        	}
        }
    	return result;
    }
    
    // The 2 comparators as inner anonymous classes
    
    Comparator<Task> priorityComparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			if(t1.getPriority()==t2.getPriority())
				return 0;
				else if(t1.getPriority()>t2.getPriority())  
				return 1;  
				else 
				return -1;
		}
	};
	
	Comparator<Task> dueDateComparator = new Comparator<Task>() {
		public int compare(Task t1, Task t2) {
			return t1.getDueDate().compareTo(t2.getDueDate());
		}
	};
}
