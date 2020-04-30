package spear_of_adun;

public abstract class Leaf extends Task {
  
  protected TaskController control;
  public String name;

  public Leaf(String name, GameState gamestate) {
    super(name, gamestate);
    this.name = name;
    CreateTaskController();
  }

  private void CreateTaskController() {
    this.control = new TaskController(this);
  }
  
  @Override
  public TaskController getControl() {
    return this.control;
  }
}
