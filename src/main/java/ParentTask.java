package spear_of_adun;

public abstract class ParentTask extends Task {

  ParentTaskController control;

  public ParentTask(String name, GameState gamestate) {
    super(name, gamestate);
    CreateController();
  }

  private void CreateController() {
    this.control = new ParentTaskController(this);
  }

  @Override
  public TaskController getControl() {
    return control;
  }

  @Override public boolean checkConditions() {
    return control.childTasks.size() > 0;
  }

  public abstract void childSucceeded();

  public abstract void childFailed();

  @Override
  public void doAction() {
    if(control.finished()) {
      return;
    }
    if(control.currentTask == null) {
      return;
    }
    if(!control.currentTask.getControl().started()) {
      control.currentTask.getControl().start();
    }
    else if(control.currentTask.getControl().finished()) {
      control.currentTask.getControl().end();
      if(control.currentTask.getControl().succeeded()) {
        System.out.println("SEQUENCE CHILD SUCCESS");
        this.childSucceeded();
      }
      if(control.currentTask.getControl().failed()) {
        System.out.println("SEQUENCE CHILD FAILURE");
        this.childFailed();
      }
    }
    else {
     control.currentTask.doAction();
   }
  }

  @Override
  public void end() {

  }
  
  @Override
  public void start() {
    control.currentTask = control.childTasks.get(control.currentTaskPosition);
    this.doAction();
  }

}