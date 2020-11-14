# Task_Manager

- Task attributes: description, priority, date, completed or not, have alert or not, task details

- Actions: list by due date or by priority, search, show details, add new task, edit task,
           delete task, mark as completed, show completed, show alerts, export as xml (JDOM 2.0.6. parser)

- You can choose whether you want to save tasks in memory or to save them permanently in a Database,
  (HSQLDB) in the directory: <user.home>/db/todo , by commenting the corresponding variable:
  private static final TaskManagerInterface TASK_MANAGER of the main class
  (It is needed to download and save the apropiate HSQLDB driver int lib folder)

- db.properties file has all the needed sql queries as well as the nessesary properties to connect with the db.

- For the unit test it is used JUnit 4.12 library

**Task TaskManager_DB.bat opens Task Manager which is compliled for use with the Database.
