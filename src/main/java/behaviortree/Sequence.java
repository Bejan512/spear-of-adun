package spear_of_adun;

public class Sequence extends ParentTask {

  public String name;
  public Sequence(String name, GameState gamestate) {
    super(name, gamestate);
    this.name = name;
  }
  public Sequence(String name, GameState gamestate, Task... tasks) {
    super(name, gamestate);
    for(Task task : tasks) {
      this.control.addTask(task);
    }
  }

  @Override
  public void childFailed() {
    control.finishWithFailure();
  }

  @Override
  public void childSucceeded() {
    int currentTaskPosition = control.childTasks.indexOf(control.currentTask);
    if(currentTaskPosition == control.childTasks.size() - 1) {
      //TRYING TO FIGURE OUT WHY NAME OF THE CURRENT CHILD TASK IS "SYMBOL NOT FOUND"
      // System.out.println(control.currentTask.name + " SUCCEEDED");
      System.out.println(name + " SEQUENCE SUCCEEDED");
      control.finishWithSuccess();
    }
    else {
      control.currentTaskPosition += 1;
      // System.out.println(control.currentTask.name + " SUCCEEDED");
    }

  }

}