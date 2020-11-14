package gr.cup.mathesis.model;

import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * DO NOT MODIFY!
 * 
 */
public class TaskManagerTest {

    private static final int DAYS_BEFORE_ALERT1 = 2;
    private static final LocalDate DATE1 = LocalDate.now().minusDays(DAYS_BEFORE_ALERT1);
    private static final int DAYS_BEFORE_ALERT2 = 1;
    private static final LocalDate DATE2 = LocalDate.now();
    private static final LocalDate DATE3 = LocalDate.now().plusDays(5);

    private static TaskManagerInterface taskManager = null;

    @BeforeClass
    public static void setUpClass() {
        taskManager = TaskManager.getInstance();
//        taskManager = TaskManagerDB.getInstance();
        taskManager.addTask(new Task("Ξ�Ο�Ξ¬Ο„Ξ·ΟƒΞ· ΞΎΞµΞ½ΞΏΞ΄ΞΏΟ‡ΞµΞ―ΞΏΟ…", 1, DATE1, true, DAYS_BEFORE_ALERT1));
        taskManager.addTask(new Task("Ξ‘Ξ³ΞΏΟ�Ξ¬ ΞµΞΉΟƒΞ·Ο„Ξ·Ο�Ξ―ΞΏΟ…", 1, DATE2, true, DAYS_BEFORE_ALERT2));
        taskManager.addTask(new Task("Ξ‘Ξ³ΞΏΟ�Ξ¬ Ξ΄Ο�Ο�ΞΏΟ…", 2, DATE3));        
    }

    @AfterClass
    public static void tearDownClass() {
        taskManager = null;
    }    

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class TaskManager.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        assertNotNull(taskManager);
    }

    /**
     * Test of listAllTasks method, of class TaskManager.
     */
    @Test
    public void testListAllTasks() {
        System.out.println("listAllTasks");
        List<Task> resultByPriority = taskManager.listAllTasks(true);
        List<Task> resultByDueDate = taskManager.listAllTasks(false);
        assertEquals(resultByDueDate.size(), resultByPriority.size());
    }

    /**
     * Test of listTasksWithAlert method, of class TaskManager.
     */
    @Test
    public void testListTasksWithAlert() {
        System.out.println("listTasksWithAlert");
        List<Task> result = taskManager.listTasksWithAlert();
        assertEquals(2, result.size());
    }

    /**
     * Test of listCompletedTasks method, of class TaskManager.
     */
    @Test
    public void testListCompletedTasks() {
        System.out.println("listCompletedTasks");
        int completed = 0;
        List<Task> allTasks = taskManager.listAllTasks(true);
        for (Task task : allTasks) {
            if (task.isCompleted()) {
                completed++;
            }
        }
        List<Task> result = taskManager.listCompletedTasks();
        assertEquals(completed, result.size());
        taskManager.markAsCompleted(1, true);
        result = taskManager.listCompletedTasks();
        assertEquals(completed+1, result.size());
    }

    /**
     * Test of addTask method, of class TaskManager.
     */
    @Test
    public void testAddTask() {
        System.out.println("addTask");
        Task task = new Task("Ξ‘Ξ³ΞΏΟ�Ξ¬ sd card", 1, LocalDate.of(2019, 4, 14), true, 1);
        taskManager.addTask(task);
        assertEquals(4, taskManager.listAllTasks(true).size());
    }

    /**
     * Test of updateTask method, of class TaskManager.
     */
    @Test
    public void testUpdateTask() {
        System.out.println("updateTask");
        Task task = taskManager.findTask(2);
        assertEquals(2, task.getId());
        task.setCompleted(true);
        taskManager.updateTask(task);
    }

    /**
     * Test of removeTask method, of class TaskManager.
     */
    @Test
    public void testRemoveTask() {
        System.out.println("removeTask");
        taskManager.removeTask(4);
        assertEquals(3, taskManager.listAllTasks(false).size());
    }

}