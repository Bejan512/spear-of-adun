package spear_of_adun;
import bwapi.*;

public class ChooseZealot extends Leaf {
  
  private GameState gamestate;
  public String name;
  public ChooseZealot(String name, GameState gamestate) {
    super(name, gamestate);
    this.gamestate = gamestate;
    this.name = name;
  }

  @Override
  public boolean checkConditions() {
    if(gamestate.gateways.size() > 0) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public void doAction() {

    for(Unit gateway : gamestate.gateways) {
          gamestate.chosenUnit = UnitType.Protoss_Zealot;
          gamestate.chosenBuilding = gateway;
          this.control.finishWithSuccess();
      
    }

  }

  @Override
  public void end() {
    
  }

  @Override
  public void start() {
    System.out.println("choose zealot started");
    this.doAction();
    
  }
}