package gr.cup.mathesis.model.db;

import gr.cup.mathesis.model.Task;
import gr.cup.mathesis.model.TaskManagerInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

/**
 * 3. Implement this class
 *
 */
public final class TaskManagerDB implements TaskManagerInterface {

    private Connection con;
    private static TaskManagerDB INSTANCE;
    private final Properties properties;

    private TaskManagerDB() {
        properties = readProperties("db.properties");
        connect();
    }

    private static Properties readProperties(String propertiesFile) {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
        }
        return prop;
    }

    public static TaskManagerDB getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskManagerDB();
        }
        return INSTANCE;
    }

    private String getDatabase() {
        return System.getProperty("user.home") + File.separator + properties.getProperty("defaultDatabase");
    }

    private String getJdbcUrl() {
        return properties.getProperty("defaultUrl") + getDatabase() + properties.getProperty("defaultOptions");
    }

    private void connect() {
        try {
            Class.forName(properties.getProperty("jdbcDriver"));
            con = DriverManager.getConnection(getJdbcUrl(),
                    properties.getProperty("dbAdmin"),
                    properties.getProperty("dbPassword"));
            if (!checkTables()) {
                createTables();
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private boolean checkTables() { // TODO 
    	try {
			final DatabaseMetaData metadata = con.getMetaData();
			final ResultSet res = metadata.getTables(null, null, null, new String[]{"TABLE"});
			if (res.next()) {
				int result = 0;
	    		try (PreparedStatement pstmt = this.con.prepareStatement(properties.getProperty("sql.count"));
	            		ResultSet rs = pstmt.executeQuery()) {
	    			while (rs.next()) {
	            		result = rs.getInt(1);
	            	}
	    			if (result >= 0) {
	                    System.out.println("Number of Tasks: " + result);
	                    return true;
	                } else return false;
	    			
	    		} catch (SQLException e) {
	            	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
	            	return false;
	    		}
			} else {
				return false;
			}
		} catch (SQLException e1) {
			Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e1);
			return false;
		}
    }
    
    private void createTables() { // TODO 
    	try (PreparedStatement pstmt = this.con.prepareStatement(properties.getProperty("sql.createTable"))) {
    		int createdRes = pstmt.executeUpdate();
    		if(createdRes == 0) {
                System.out.println("New table created successfully");
            }
        } catch (SQLException e) {
        	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void disconnect() {
        try {
            if (con != null) {
                con.close();
            }
            con = null;
        } catch (SQLException e) {
            // ignores the exception
        }
    }
    
    /**
     * Update task with the given {@code id}
     *
     * @param sql update/delete sql query
     * @param id task id
     */
    private void update(String sql, int id) { // TODO
    	try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
    		pstmt.setInt(1, id);
    		pstmt.executeUpdate();
        } catch (SQLException e) {
        	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private List<Task> query(String where, String orderBy) { // TODO
        List<Task> result = new ArrayList<>();
        String sqlQuery;
        if (where == null && !(orderBy == null)) {
        	sqlQuery = properties.getProperty("sql.select") + " ORDER BY " + orderBy; 
        }else if (where != null && orderBy == null) {
        	sqlQuery = properties.getProperty("sql.select") + " WHERE " + where;
        }else {
        	sqlQuery = properties.getProperty("sql.select") + " WHERE " + where + " ORDER BY " + orderBy;
        }
        try (Statement stmt = this.con.createStatement();
        		ResultSet rs = stmt.executeQuery(sqlQuery)) {
        	while (rs.next()) {
        		int id = rs.getInt(1);
        		String desc = rs.getString(2);
        		int priority = rs.getInt(3);
        		Date date = rs.getDate(4);
        		LocalDate dueDate = null;
        		if (!(date == null)) {
        			dueDate = date.toLocalDate();
        		}
        		boolean alert = rs.getBoolean(5);
        		int daysBefore = rs.getInt(6);
        		String comments = rs.getString(7);
        		boolean completed = rs.getBoolean(8);
        		Task task = new Task(id, desc, priority, dueDate, alert, daysBefore, comments, completed);
        		result.add(task);
        	}
        } catch (SQLException | NullPointerException e) {
        	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    private void modify(String sql, Task task, boolean isUpdate) { // TODO
    	if (isUpdate==false) {
    		try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            	pstmt.setString(1, task.getDescription());
            	pstmt.setInt(2, task.getPriority());
            	Date date = null;
            	if (task.getDueDate() != null) {
            		date = java.sql.Date.valueOf(task.getDueDate());
            	}
            	pstmt.setDate(3, date);
            	pstmt.setBoolean(4, task.getAlert());
            	pstmt.setInt(5, task.getDaysBefore());
            	pstmt.setString(6, task.getComments());
            	pstmt.setBoolean(7, task.isCompleted());
            	pstmt.executeUpdate();
            	System.out.println("New task inserted");
            } catch (SQLException e) {
            	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
            }
    	}else {
    		try (PreparedStatement pstmt = this.con.prepareStatement(sql)) {
            	pstmt.setString(1, task.getDescription());
            	pstmt.setInt(2, task.getPriority());
            	Date date = null;
            	if (task.getDueDate() != null) {
            		date = java.sql.Date.valueOf(task.getDueDate());
            	}
            	pstmt.setDate(3, date);
            	pstmt.setBoolean(4, task.getAlert());
            	pstmt.setInt(5, task.getDaysBefore());
            	pstmt.setString(6, task.getComments());
            	pstmt.setBoolean(7, task.isCompleted());
            	pstmt.setInt(8, task.getId());
            	pstmt.executeUpdate();
            	System.out.println("Task updated");
            } catch (SQLException | NullPointerException e) {
            	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.SEVERE, null, e);
            }
    	}
    }
    
    @Override
    public List<Task> listAllTasks(boolean priorityOrDate) {
        return query(null, priorityOrDate
                ? properties.getProperty("sql.alertsOrderByPriority")
                : properties.getProperty("sql.alertsOrderByDueDate"));
    }

    @Override
    public List<Task> listTasksWithAlert() {
        return query(properties.getProperty("sql.alerts"),
                properties.getProperty("sql.alertsOrderByDueDate"));
    }

    @Override
    public List<Task> listCompletedTasks() {
        return query("completed = true", null);
    }

    @Override
    public void addTask(Task task) {
        if (validate(task)) {
            modify(properties.getProperty("sql.insert"), task, false);
        } else {
            Logger.getLogger(TaskManagerDB.class.getName()).log(Level.WARNING, "Validation failed. DB not updated.");
        }

    }

    @Override
    public void updateTask(Task task) {
        if (validate(task)) {
            modify(properties.getProperty("sql.update"), task, true);
        } else {
            Logger.getLogger(TaskManagerDB.class.getName()).log(Level.WARNING, "Validation failed. DB not updated.");
        }
    }
    
    @Override
    public void markAsCompleted(int id, boolean completed) { // TODO
    	try (PreparedStatement pstmt = this.con.prepareStatement(properties.getProperty("sql.updateCompleted"))) {
    		// Ξ�Ξ­Ο„ΞΏΟ…ΞΌΞµ Ο„ΞΉΟ‚ Ξ±Ξ½Ο„Ξ―ΟƒΟ„ΞΏΞΉΟ‡ΞµΟ‚ Ο„ΞΉΞΌΞ­Ο‚ ΟƒΟ„Ξ± Ξ±Ξ½Ο„Ξ―ΟƒΟ„ΞΏΞΉΟ‡Ξ± ? Ο„Ξ·Ο‚ ΞµΞ½Ο„ΞΏΞ»Ξ®Ο‚ SQL.
    		pstmt.setBoolean(1, completed);
    		pstmt.setInt(2, id);
    		pstmt.executeUpdate();
        } catch (SQLException e) {
        	Logger.getLogger(TaskManagerDB.class.getName()).log(Level.WARNING, "Validation failed. DB not updated.");
        }
    }

    @Override
    public void removeTask(int id) {
        update(properties.getProperty("sql.delete"), id);
    }

    @Override
    public Task findTask(final int id) {
        List<Task> tasks = query("id = " + id, null);
        return tasks.isEmpty() ? null : tasks.get(0);
    }

    private boolean validate(Task task) {
        return !task.getDescription().isEmpty();
    }

}
