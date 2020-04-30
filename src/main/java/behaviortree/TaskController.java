package spear_of_adun;

public class TaskController {

  private boolean finished;
  private boolean success;
  private boolean started;
  private Task task;
  
  public TaskController(Task task) {
    this.task = task;
    init();
  }

  private void init() {
    this.finished = false;
    this.success = true;
    this.started = false; 
  }

  public void start() {
    this.started = true;
    task.start();
  }

  public void end() {
    this.finished = false;
    this.started = false;
    task.end();
  }

  protected void finishWithSuccess() {
    this.success = true;
    this.finished = true;
    // task.logTask("Finished with Success");
  }

  protected void finishWithFailure() {
    this.success = false;
    this.finished = true;
    // task.logTask("Finished with Failure");
  }

  public boolean succeeded() {
    return this.success;
  }

  public boolean failed() {
    return !this.success;
  }

  public boolean finished() {
    return this.finished;
  }

  public boolean started() {
    return this.started;
  }

  public void reset() {
    this.finished = false;
  }
}