package spear_of_adun;
import bwapi.*;

public class TrainUnit extends Leaf {
  
  private GameState gamestate;
  public String name;
  public TrainUnit(String name, GameState gamestate) {
    super(name, gamestate);
    this.gamestate = gamestate;
    this.name = name;
  }

  @Override
  public boolean checkConditions() {
    if(gamestate.chosenUnit == null || gamestate.chosenBuilding == null) {
      return false;
    }
    else if(gamestate.chosenBuilding.canTrain()){
      return true;
    }
    else return false;
  }

  @Override
  public void doAction() {
    gamestate.chosenBuilding.train(gamestate.chosenUnit);
    if(!gamestate.chosenBuilding.isTraining()) {
      System.out.println("TRAIN UNIT " + gamestate.chosenUnit + " SUCCEEDED");
      this.control.finishWithSuccess();
    }
  }

  @Override
  public void end() {
    gamestate.chosenBuilding = null;
    gamestate.chosenUnit = null;
    System.out.println("TRAIN UNIT ENDED");
  }

  @Override
  public void start() {
    System.out.println("TRAIN UNIT STARTED");
    this.doAction();
  }
}