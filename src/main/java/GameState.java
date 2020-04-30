package spear_of_adun;

import bwapi.BWClient;
import bwapi.DefaultBWListener;
import bwapi.*;
import bwem.BWEM;
import bwem.BWMap;
import java.util.*;
import bwem.*;



public class GameState {

    private BWEM bwem;

    public int currentFreeSupply;
    public int currentSupply;
    public boolean buildProbes;
    public Player player;
    public BWMap map;
    public List<Base> startLocations = new ArrayList<>();
    public List<Unit> gateways = new ArrayList<>();
    public Unit chosenBuilding = null;
    public UnitType chosenUnit = null;
    public Game game;

    

    public GameState(Game game, BWEM bwem) {
        player = game.self();
        this.bwem = bwem;
        this.game = game;
        
    }

    public void updateSupply() {
        currentSupply = player.supplyUsed();
        currentFreeSupply = player.supplyTotal() - player.supplyUsed();
    }

    public void initMap() {
        map = bwem.getMap();
        map.assignStartingLocationsToSuitableBases();
    }

    public void initStartLocations() {
        for(Base base : map.getBases()) {
            if(base.isStartingLocation() && !base.getLocation().equals(player.getStartLocation())) {
                startLocations.add(base);
            }
        }
    }



}