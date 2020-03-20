 
import bwapi.BWClient;
import bwapi.DefaultBWListener;
import bwapi.*;
import bwem.BWEM;
import bwem.BWMap;
import java.util.*;
import bwem.*;

public class Bot extends DefaultBWListener {
    private BWClient bwClient;
    private Game game;
    private Player player;
    private Player enemy;
    private Unit builder;
    private BWMap map;
    private boolean buildProbes;
    private int gatewayCount;
    private int pylonCount;
    private boolean nexusTraining;
    private boolean attacking;
    private Base playerBase;
    List<Base> startLocations = new ArrayList<>();

    @Override
    public void onStart() {
        game = bwClient.getGame();
        BWEM bwem = new BWEM(game);
        bwem.initialize();
        map = bwem.getMap();
        map.assignStartingLocationsToSuitableBases();
        player = game.self();
        enemy = game.enemy();
        builder = null;
        buildProbes = true;
        gatewayCount = 0;
        pylonCount = 0;
        nexusTraining = false;

        for(Base base : map.getBases()) {
            if(base.isStartingLocation() && !base.getLocation().equals(player.getStartLocation())) {
                startLocations.add(base);
            }
        }
    }

    @Override
    public void onFrame() {
        int currentFreeSupply = player.supplyTotal() - player.supplyUsed();
        int currentSupply = player.supplyUsed();

        game.drawTextScreen(25, 10, "SPEAR OF ADUN");
        game.drawTextScreen(25, 20, "CURRENT FREE SUPPLY: " + String.valueOf(currentFreeSupply));
        game.drawTextScreen(25, 30, "BUILD PROBES?: " + String.valueOf(buildProbes));
        if(builder != null) {
            game.drawTextScreen(25, 40, "BUILDER ID: " + String.valueOf(builder.getID()));
            game.drawTextScreen(25, 50, "BUILDER IDLE?: " + String.valueOf(builder.isIdle()));
            game.drawTextMap(builder.getPosition(), "CURRENT BUILDER");
        }
        else {
            game.drawTextScreen(25, 40, "BUILDER ID: " + "NULL");
        }
        game.drawTextScreen(25, 60, "GATEWAY COUNT: " + String.valueOf(gatewayCount));
        game.drawTextScreen(25, 70, "PYLON COUNT: " + String.valueOf(pylonCount));
        game.drawTextScreen(25, 80, "NEXUS TRAINING?: " + String.valueOf(nexusTraining));
        game.drawTextScreen(25, 90, "NUM BASES: " + String.valueOf(startLocations.size()));

        List<Unit> workers = new ArrayList<>();
        List<Unit> gasWorkers = new ArrayList<>();
        List<Unit> buildings = new ArrayList<>();
        List<Unit> nexusList = new ArrayList<>();
        List<Unit> gateways = new ArrayList<>();
        List<Unit> zealots = new ArrayList<>();
        List<Unit> dragoons = new ArrayList<>();
        List<Unit> trainableBuildings = new ArrayList<>();
        List<Unit> assimilators = new ArrayList<>();
        List<Unit> cyberCores = new ArrayList<>();

        

        //create lists
        for(Unit unit : player.getUnits()) {
            if(unit.getType().isWorker()) {
                workers.add(unit);
            }
            if(unit.getType().isBuilding()) {
                buildings.add(unit);
            }
            if(unit.getType() == UnitType.Protoss_Zealot) {
                zealots.add(unit);
            }
            if(unit.getType() == UnitType.Protoss_Dragoon) {
                dragoons.add(unit);
            }
        }
        for(Unit building : buildings) {
            if(building.getType() == UnitType.Protoss_Nexus) {
                nexusList.add(building);
            }
            if(building.getType() == UnitType.Protoss_Gateway) {
                gateways.add(building);
            }
            if(building.getType() == UnitType.Protoss_Assimilator) {
                assimilators.add(building);
            }
            if(building.getType() == UnitType.Protoss_Cybernetics_Core) {
                cyberCores.add(building);
            }
            if(building.canTrain()) {
                trainableBuildings.add(building);
            }
        }
        for(Unit worker : workers) {
            if(gasWorkers.size() < 3) {
                gasWorkers.add(worker);
            }
        }



        //build order
        if(currentSupply == 12) {
            buildProbes = false;
            if(pylonCount < 1) {
                TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Pylon, player.getStartLocation(), 30);
                builder.build(UnitType.Protoss_Pylon, buildLocation);
            }
            if(pylonCount > 0) {
                buildProbes = true;
            }
        }
        if(currentSupply == 20 && gatewayCount < 2) {
            buildProbes = false;
            TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Gateway, player.getStartLocation(), 30);
            builder.build(UnitType.Protoss_Gateway, buildLocation);
            if(gatewayCount == 2) {
                buildProbes = true;
            }
        }

        if(currentSupply > 24 && cyberCores.size() < 1) {
            TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Cybernetics_Core, player.getStartLocation(), 30);
            builder.build(UnitType.Protoss_Cybernetics_Core, buildLocation);
        }

        if(gatewayCount >= 2) {
            for(Unit gateway : gateways) {
                if(gateway.isTraining() == false && zealots.size() < 15) {
                    gateway.build(UnitType.Protoss_Zealot);
                }
                if(gateway.isTraining() == false && zealots.size() >= 15) {
                    gateway.build(UnitType.Protoss_Dragoon);
                }
            }
            if(currentFreeSupply < 4) {
                TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Pylon, player.getStartLocation(), 50);
                builder.build(UnitType.Protoss_Pylon, buildLocation);
            }
        }
        if(currentSupply > 30 && gatewayCount < 3) {
            TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Gateway, player.getStartLocation(), 30);
            builder.build(UnitType.Protoss_Gateway, buildLocation);
            buildLocation = game.getBuildLocation(UnitType.Protoss_Assimilator, player.getStartLocation(), 30);
            builder.build(UnitType.Protoss_Assimilator, buildLocation);
            buildProbes = true;
        }
        if(currentSupply > 45 && gatewayCount < 5) {
            TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Gateway, player.getStartLocation(), 30);
            builder.build(UnitType.Protoss_Gateway, buildLocation);
        }
        if(zealots.size() >= 10 && dragoons.size() >= 5) {
            attacking = true;
        }
        if(attacking) {
            for(Unit zealot : zealots) {
                if(zealot.isIdle()){
                    zealot.attack(startLocations.get(0).getCenter());
                }
            }
            for(Unit dragoon : dragoons) {
                if(dragoon.isIdle()){
                    dragoon.attack(startLocations.get(0).getCenter());
                }
            }
            if(zealots.size() <= 10) {
                attacking = false;
            }
        }





        // for(Unit zealot : zealots) {
        //     game.drawTextMap(zealot.getPosition(), "ATTACKING? " + String.valueOf(zealot.isAttacking()));
        // }

        //get builder
        for(Unit worker : workers) {
            if(builder == null) {
                if(worker.getType().isWorker() && worker.isIdle() || worker.isGatheringMinerals()) {
                    builder = worker;
                    break;
                }
            }
        }

        //build probes
        for(Unit nexus : nexusList) {
            nexusTraining = nexus.isTraining();
            if(buildProbes && nexusTraining == false) {
                nexus.build(UnitType.Protoss_Probe);
                nexusTraining = nexus.isTraining();
            }
        }
        //stop building probes at max probes
        if(workers.size() >= 30) {
            buildProbes = false;
        }

        //send idle workers to mine
        if(assimilators.size() > 0) {
            if(assimilators.get(0).isCompleted()) {
                for(Unit worker : gasWorkers) {
                    if(worker.isIdle() || worker.isGatheringMinerals()) {
                        worker.gather(assimilators.get(0));
                    }
                }
            }
        }
        for(Unit worker : workers) {
            if(worker.isIdle()) {
                Unit closestMineral = null;
                int closestDistance = Integer.MAX_VALUE;
                for (Unit mineral : game.getMinerals()) {
                    int distance = worker.getDistance(mineral);
                    if (distance < closestDistance) {
                        closestMineral = mineral;
                        closestDistance = distance;
                    }
                }
                worker.gather(closestMineral);
            }
        }
        //cancel extra units being trained
        for(Unit building : trainableBuildings) {
            if(building.getTrainingQueue().size() > 1 && building.canCancelTrain()) {
                building.cancelTrain(1);
            }
        }
        // //build pylons if supply blocked
        // if(currentFreeSupply < 2) {
        //     if(builder != null && !builder.isConstructing() && player.minerals() > 100) {
        //         TilePosition buildLocation = game.getBuildLocation(UnitType.Protoss_Pylon, player.getStartLocation(), 50);
        //         builder.build(UnitType.Protoss_Pylon, buildLocation);
        //     }
        // }
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

    @Override
    public void onUnitCreate(Unit unit) {
        if(unit.getType() == UnitType.Protoss_Gateway) {
            gatewayCount++;
        }
        if(unit.getType() == UnitType.Protoss_Pylon) {
            pylonCount++;
        }
    }

    public static void main(String[] args) {
        Bot bot = new Bot();
        bot.bwClient = new BWClient(bot);
        bot.bwClient.startGame();
    }
}