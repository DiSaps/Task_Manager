package gr.cup.mathesis.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
	private static int autoIndex = 1;
	final private int id; 			  
	private String description;
	private int priority;
	private LocalDate dueDate;
	private boolean alert = false;
	private int daysBefore;
	private String comments="";
	private boolean completed = false;
	
	
	// Constructor No1
	public Task(String desc, int prio, LocalDate dueDate, boolean alert, 
			int daysBefore, String comments, boolean completed) { 
		this.id = autoIndex++;
		this.description = desc;
		this.priority = prio;
		this.dueDate = dueDate;
		this.alert = alert;
		this.daysBefore = daysBefore;
		this.comments = comments;
		this.completed = completed;
	}
	
	// Constructor No2 (for TaskManagerDB)
	public Task(int id, String desc, int prio, LocalDate dueDate, boolean alert, 
			int daysBefore, String comments, boolean completed) {
		this.id = id;
		this.description = desc;
		this.priority = prio;
		this.dueDate = dueDate;
		this.alert = alert;
		this.daysBefore = daysBefore;
		this.comments = comments;
		this.completed = completed;
	}
	
	// Constructor No3 (for junit test)
	public Task(String desc, int prio, LocalDate dueDate, boolean alert, int daysBefore) {
		this.id = autoIndex++;
		this.description = desc;
		this.priority = prio;
		this.dueDate = dueDate;
		this.alert = alert;
		this.daysBefore = daysBefore;
	}
	
	// Constructor No4 (for junit test)
	public Task(String desc, int prio, LocalDate dueDate) {
		this.id = autoIndex++;
		this.description = desc;
		this.priority = prio;
		this.dueDate = dueDate;
	}
	
	/* Calculates and return the difference of task's date with the today date */
	public int isLate() {
		LocalDate today = LocalDate.now();
		LocalDate taskDate = this.dueDate;
		int lateDays = (int) ChronoUnit.DAYS.between(today, taskDate); 
		return lateDays;
	}
	
	public boolean hasAlert() {
		if (this.alert && (this.isLate() <= this.daysBefore)) {
    		return true;
    	} else {
    		return false;
    	}
	}
	
	public void setDescription(String newdesc) {
		if (newdesc.equals("")) {
			throw new IllegalArgumentException();
		} else {
			this.description = newdesc;
		}	
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setPriority(int newPrio) {
		if (newPrio <= 0 || newPrio > 10) {
			throw new IllegalArgumentException();
		} else {
			this.priority = newPrio;
		}
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public void setDueDate(LocalDate newDueDate) {
		this.dueDate = newDueDate;
	}
	
	public LocalDate getDueDate() {
		return this.dueDate;
	}
	
	public void setAlert(boolean newAlert) {
		this.alert = newAlert;
	}
	
	public boolean getAlert() {
		return this.alert;
	}
	
	public void setDaysBefore(int newDaysBefore) {
		if (newDaysBefore < 0 || newDaysBefore > 365) {
			throw new IllegalArgumentException();
		} else {
			this.daysBefore = newDaysBefore;
		}
	}
	
	public int getDaysBefore() {
		return this.daysBefore;
	}
	
	public void setComments(String newComments) {
		this.comments = newComments;
	}
	
	public String getComments() {
		return this.comments;
	}
	
	public void setCompleted(boolean newCompleted) {
		this.completed = newCompleted;
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.description);
        hash = 53 * hash + this.priority;
        hash = 53 * hash + Objects.hashCode(this.dueDate);
        hash = 53 * hash + (this.alert ? 1 : 0);
        hash = 53 * hash + this.daysBefore;
        hash = 53 * hash + Objects.hashCode(this.comments);
        hash = 53 * hash + (this.completed ? 1 : 0);
        return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
      
        if (this.id != other.id) {
            return false;
        }
        
        if (this.priority != other.priority) {
            return false;
        }
        if (this.alert != other.alert) {
            return false;
        }
        if (this.daysBefore != other.daysBefore) {
            return false;
        }
        if (this.completed != other.completed) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.comments, other.comments)) {
            return false;
        }
        if (!Objects.equals(this.dueDate, other.dueDate)) {
            return false;
        }
        return true;
	}
	
	public String toShortString() {
		if (!this.alert) {
			return String.format("ID: %d%nPriority: %d%nDescription: %s%nDue Date: %s%nAlert? %b%n",
					this.id, this.priority, this.description, this.dueDate, this.alert);
		} else {
			return String.format("ID: %d%nPriority: %d%nDescription: %s%nDue Date: %s%nAlert? %b%nDays Before: %d%n",
					this.id, this.priority, this.description, this.dueDate, this.alert, this.daysBefore);
		}
	}
	
	@Override
	public String toString() {
		if (!this.alert) {
			return String.format("ID: %d%nPriority: %d%nDescription: %s%nDue Date: %s%nAlert? %b%nComments: %s%nCompleted? %b%n",
					this.id, this.priority, this.description, this.dueDate, this.alert, this.comments, this.completed);
		}else {
			return String.format("ID: %d%nPriority: %d%nDescription: %s%nDue Date: %s%nAlert? %b%nDays Before: %d%nComments: %s%nCompleted? %b%n",
					this.id, this.priority, this.description, this.dueDate, this.alert, this.daysBefore, this.comments, this.completed);
		}
	}

}
