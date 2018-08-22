package stonering.game.core.model;

import com.badlogic.gdx.utils.Timer;
import stonering.game.core.model.lists.*;
import stonering.game.core.view.tilemaps.LocalTileMap;
import stonering.game.core.view.tilemaps.LocalTileMapUpdater;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;

/**
 * Model of game, contains LocalMap and sub-Containers.
 * Time ticks are performed with Timer
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameContainer {
    private WorldMap worldMap;
    private LocalMap localMap; //local map is created during localgeneration.
    private LocalTileMap localTileMap;
    private BuildingContainer buildingContainer;
    private PlantContainer plantContainer;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;

    private LiquidContainer liquidContainer;

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

        plantContainer = new PlantContainer(this);
        plantContainer.placePlants(container.getPlants());

        buildingContainer = new BuildingContainer(container.getBuildings());
        buildingContainer.setLocalMap(localMap);
        buildingContainer.placeBuildings();

        unitContainer = new UnitContainer(container.getUnits(), this);
        unitContainer.setLocalMap(localMap);
        unitContainer.placeUnits();
        unitContainer.initUnits();

        itemContainer = new ItemContainer(container.getItems(), this);
        itemContainer.initItems();

        taskContainer = new TaskContainer(this);

        liquidContainer = new LiquidContainer(container);
        liquidContainer.setLocalMap(localMap);
        liquidContainer.initLiquidsToMap();

        //TODO commented for fast localgen
//        localMap.initAreas();
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

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    private synchronized void performTick() {
        unitContainer.turn();
        plantContainer.turn();
        buildingContainer.turn();
        itemContainer.turn();
        liquidContainer.turn();
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

    public TaskContainer getTaskContainer() {
        return taskContainer;
    }

    public ItemContainer getItemContainer() {
        return itemContainer;
    }

    public BuildingContainer getBuildingContainer() {
        return buildingContainer;
    }

    public PlantContainer getPlantContainer() {
        return plantContainer;
    }
}