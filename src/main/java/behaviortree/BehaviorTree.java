package spear_of_adun;

public class BehaviorTree extends ParentTask {
  
  public String name;
  public BehaviorTree(String name, GameState gamestate) {
    super(name, gamestate);
    this.name = name;
  }
  public BehaviorTree(String name, GameState gamestate, Task... tasks) {
    super(name, gamestate);
    this.name = name;
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
    control.finishWithSuccess();
  }
  @Override
  public void end() {
  }
  
}