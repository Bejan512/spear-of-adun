package spear_of_adun;

public class Selector extends ParentTask {
  
  public String name;

  public Selector(String name, GameState gamestate) {
    super(name, gamestate);
    this.name = name;
  }

  public Selector(String name, GameState gamestate, Task... tasks) {
    super(name, gamestate);
    this.name = name;
    for(Task task : tasks) {
      this.control.addTask(task);
    }
  }

  public Task chooseNewTask() {
    Task task = null;
    boolean found = false;
    int currentTaskPosition = control.childTasks.indexOf(control.currentTask);

    while(!found) {
      if(currentTaskPosition == (control.childTasks.size() - 1)) {
        found = true;
        task = null;
        break;
      }

      currentTaskPosition++;
      task = control.childTasks.get(currentTaskPosition);
      if(task.checkConditions()) {
        found = true;
      }
    }
    return task;
  }

  @Override
  public void childFailed() {
    control.currentTask = chooseNewTask();
    if(control.currentTask == null) {
      control.finishWithFailure();
    }
  }

  @Override
  public void childSucceeded() {
    control.finishWithSuccess();
    System.out.println(name + " SELECTOR SUCCEEDED");
  }
}

