 
import bwapi.BWClient;
import bwapi.DefaultBWListener;
import bwapi.*;
import bwem.BWEM;
import bwem.BWMap;

public class Bot extends DefaultBWListener {
    private BWClient bwClient;
    private Game game;
    private Player player;
    private Player enemy;
    private Unit builder;
    private BWMap map;



    @Override
    public void onStart() {
        game = bwClient.getGame();
        BWEM bwem = new BWEM(game);
        map = bwem.getMap();
        bwem.initialize();
        player = game.self();
        enemy = game.enemy();
        builder = null;
    }

    @Override
    public void onFrame() {
        int currentFreeSupply = player.supplyTotal() - player.supplyUsed();


        game.drawTextScreen(25, 10, "SPEAR OF ADUN");
        game.drawTextScreen(25, 20, "CURRENT FREE SUPPLY: " + String.valueOf(currentFreeSupply));
        if(builder != null) {
            game.drawTextScreen(25, 30, "BUILDER ID: " + String.valueOf(builder.getID()));
            game.drawTextScreen(25, 40, "BUILDER IDLE?: " + String.valueOf(builder.isIdle()));
        }
        else {
            game.drawTextScreen(25, 30, "BUILDER ID: " + "NULL");
        }

        //loop through buildings
        for(Unit building : player.getUnits()) {
            //build probes
            if(building.getType() == UnitType.Protoss_Nexus) {
                building.build(UnitType.Protoss_Probe);
            }
        }

        //loop through units
        for(Unit unit : player.getUnits()) {
            //get builder
            if(builder == null) {
                if(unit.getType().isWorker() && unit.isIdle() || unit.isGatheringMinerals()) {
                    builder = unit;
                    break;
                }
                break;
            }

            //send idle workers to mine
            if(unit.getType().isWorker() && unit.isIdle()) {
                Unit closestMineral = null;
                int closestDistance = Integer.MAX_VALUE;
                for (Unit mineral : game.getMinerals()) {
                    int distance = unit.getDistance(mineral);
                    if (distance < closestDistance) {
                        closestMineral = mineral;
                        closestDistance = distance;
                    }
                }
                unit.gather(closestMineral);
            }
        }
        //build pylons if supply blocked
        if(currentFreeSupply < 6) {
            if(builder != null && !builder.isConstructing() && player.minerals() > 100) {
                TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Pylon, player.getStartLocation(), 30);
                builder.build(UnitType.Protoss_Pylon, buildLocation);
                builder = null;
            }
        }
    }

    @Override
    public void onUnitComplete(Unit unit) {
        //send new workers to mine
        if (unit.getType().isWorker()) {
            Unit closestMineral = null;
            int closestDistance = Integer.MAX_VALUE;
            for (Unit mineral : game.getMinerals()) {
                int distance = unit.getDistance(mineral);
                if (distance < closestDistance) {
                    closestMineral = mineral;
                    closestDistance = distance;
                }
            }
            unit.gather(closestMineral);
        }
    }

    public static void main(String[] args) {
        Bot bot = new Bot();
        bot.bwClient = new BWClient(bot);
        bot.bwClient.startGame();
    }
}