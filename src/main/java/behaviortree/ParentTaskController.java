package spear_of_adun;

import java.util.*;

public class ParentTaskController extends TaskController {

  public ArrayList<Task> childTasks;
  public Task currentTask;
  public int currentTaskPosition;

  public ParentTaskController(Task task) {
    super(task);
    this.childTasks = new ArrayList<Task>();
    this.currentTask = null;
    this.currentTaskPosition = 0;
  }

  public void addTask(Task task) {
    childTasks.add(task);
  }
  
  public void reset() {
    this.currentTask = childTasks.get(0);
    super.reset();
  }


}