package stonering.game.core.model;

import com.badlogic.gdx.utils.Timer;
import stonering.game.core.model.lists.BuildingContainer;
import stonering.game.core.model.lists.PlantContainer;
import stonering.game.core.model.lists.UnitContainer;
import stonering.game.core.view.tilemaps.LocalTileMap;
import stonering.game.core.view.tilemaps.LocalTileMapUpdater;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.06.2017.
 *
 * Model of game, contains LocalMap and sub-Containers.
 * Time ticks are performed with Timer
 */
public class GameContainer {
    private WorldMap worldMap;
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private List<Unit> units;
    private BuildingContainer buildingContainer;
    private PlantContainer plantContainer;
    private UnitContainer unitContainer;
    private Timer timer;
    private GameCamera camera;

    private boolean paused;

    public GameContainer(LocalGenContainer container) {
        loadFromContainer(container);
        init();
    }

    private void init() {
        localTileMap = new LocalTileMap(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        createTileMapUpdater();
        camera = new GameCamera(this);
        timer = new Timer();
        paused = false;
        startContainer();
    }

    private void loadFromContainer(LocalGenContainer container) {
        this.localMap = container.getLocalMap();
        units = new ArrayList<>(container.getUnits());
        plantContainer = new PlantContainer(container.getTrees(), null);
        plantContainer.setLocalMap(localMap);
        plantContainer.placeTrees();
        buildingContainer = new BuildingContainer(container.getBuildings());
        buildingContainer.setLocalMap(localMap);
        buildingContainer.placeBuildings();
        unitContainer = new UnitContainer(container.getUnits());
        unitContainer.setLocalMap(localMap);
        unitContainer.placeUnits();
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
        unitContainer.turn();
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

    public GameCamera getCamera() {
        return camera;
    }
}