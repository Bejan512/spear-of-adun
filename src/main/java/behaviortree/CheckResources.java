package spear_of_adun;
import bwapi.*;

public class CheckResources extends Leaf {
  
  private GameState gamestate;
  public String name;
  public CheckResources(String name, GameState gamestate) {
    super(name, gamestate);
    this.gamestate = gamestate;
    this.name = name;
  }

  @Override
  public boolean checkConditions() {
    if(gamestate.player.minerals() >= 100 && gamestate.currentFreeSupply >= 4) {
      return true;
    }
    else {
      // System.out.println("CHECK RESOURCES FAILURE");
      return false;
    }
  }

  @Override
  public void doAction() {
    System.out.println("CHECK RESOURCES SUCCESS");
    this.control.finishWithSuccess();
  }

  @Override
  public void end() {
    System.out.println("CHECK RESOURCES ENDED");    
    
  }

  @Override
  public void start() {
    System.out.println("CHECK RESOURCES STARTED");    
  }
}