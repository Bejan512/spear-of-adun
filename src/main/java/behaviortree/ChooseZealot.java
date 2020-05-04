package spear_of_adun;
import bwapi.*;

public class ChooseZealot extends Leaf {
  
  private GameState gamestate;
  public String name;
  private Unit gateway;
  public ChooseZealot(String name, GameState gamestate) {
    super(name, gamestate);
    this.gamestate = gamestate;
    this.name = name;
  }

  @Override
  public boolean checkConditions() {
    if(gamestate.gateways.size() > 0) {
      for(Unit gateway : gamestate.gateways) {
        if(gateway.canTrain() && !gateway.isTraining()) {
          return true;
        }
        else continue;
      }
      return false;
    } 
    else return false;
  }

  @Override
  public void doAction() {
      for(Unit gateway : gamestate.gateways) {
        if(gateway.canTrain() && !gateway.isTraining()) {
          gamestate.chosenUnit = UnitType.Protoss_Zealot;
          gamestate.chosenBuilding = gateway;
          System.out.println("CHOOSE ZEALOT SUCCESS");
          this.control.finishWithSuccess(); 
          break;
        }
      }
    }

  @Override
  public void end() {
    System.out.println("CHOOSE ZEALOT ENDED"); 
  }

  @Override
  public void start() {
    System.out.println("CHOOSE ZEALOT STARTED");    
    this.doAction();
    
  }
}