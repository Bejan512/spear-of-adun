package spear_of_adun;

public abstract class Task {

  protected GameState gs;
  
  
  public Task(String name, GameState gamestate) {
    this.gs = gamestate;
  }
  
  public abstract boolean checkConditions();

  public abstract void start();

  public abstract void end();

  public abstract void doAction();

  public abstract TaskController getControl();
}

