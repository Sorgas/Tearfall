package stonering.game.core.model;

import com.badlogic.gdx.utils.Timer;
import stonering.game.core.model.lists.PlantContainer;
import stonering.game.core.model.tilemaps.LocalTileMap;
import stonering.game.core.model.tilemaps.LocalTileMapUpdater;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.local_actors.building.Building;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameContainer {
    private WorldMap worldMap;
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private List<Unit> units;
    private ArrayList<Building> buildings;
    private PlantContainer plantContainer;
    private Timer timer;

    private boolean gameStarted;
    private boolean paused;

    public GameContainer(LocalGenContainer container) {
        this.localMap = container.getLocalMap();
        units = new ArrayList<>(container.getUnits());
        buildings = new ArrayList<>(container.getBuildings());
        plantContainer = new PlantContainer(container.getTrees(), null);
        localTileMap = new LocalTileMap(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        createTileMapUpdater();
        timer = new Timer();
        gameStarted = true;
        paused = false;
        startContainer();
    }

    private void startContainer() {
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                performTick();
            }
        }, 0, 1f / 60);
    }

    private void createTileMapUpdater() {
        LocalTileMapUpdater localTileMapUpdater = new LocalTileMapUpdater(this);
        localMap.setLocalTileMapUpdater(localTileMapUpdater);
        localTileMapUpdater.flushLocalMap();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public LocalTileMap getLocalTileMap() {
        return localTileMap;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    private synchronized void performTick() {
//        System.out.println("tick");
        for (Unit unit : units) {
            Position old = unit.getPosition();
            unit.turn();
            localMap.updateBlock(old.getX(), old.getY(), old.getZ());
            localMap.updateBlock(unit.getPosition().getX(), unit.getPosition().getY(), unit.getPosition().getZ());
        }
    }

    public PlantContainer getPlantContainer() {
        return plantContainer;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void pauseGame() {
        pauseGame(!paused);
    }

    public void pauseGame(boolean pause) {
        if (pause) {
            timer.stop();
            paused = true;
        } else {
            timer.start();
            paused = false;
        }
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }
}