package spear_of_adun;

import bwapi.BWClient;
import bwapi.DefaultBWListener;
import bwapi.*;
import bwem.BWEM;
import bwem.BWMap;
import java.util.*;
import bwem.*;



public class GameState {


    public int currentFreeSupply;
    public int currentSupply;
    public boolean buildProbes;
    public Player player;




    public GameState(Game game, BWEM bwem) {
        player = game.self();
        
        
    }

    public void updateSupply() {
        currentSupply = player.supplyUsed();
        currentFreeSupply = player.supplyTotal() - player.supplyUsed();
    }






}