package stonering.game.core.model;

import com.badlogic.gdx.utils.Timer;
import stonering.entity.world.World;
import stonering.game.core.model.lists.*;
import stonering.game.core.view.tilemaps.LocalTileMap;
import stonering.game.core.view.tilemaps.LocalTileMapUpdater;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.local.environment.GameCalendar;
import stonering.util.global.TagLoggersEnum;

/**
 * Model of game, contains LocalMap and sub-Containers.
 * Time ticks are performed with Timer. Calls turning for all game entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameContainer {
    private World world;
    private LocalMap localMap;                              //local map is created during localgeneration.
    private LocalTileMap localTileMap;

    private BuildingContainer buildingContainer;
    private PlantContainer plantContainer;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;
    private LiquidContainer liquidContainer;

    private Timer timer;                                    //makes turns for entity containers and calendar.
    private GameCalendar gameCalendar;                      //slow game entities make turns through this.
    private EntitySelector camera;

    private boolean paused;

    public GameContainer(LocalGenContainer container) {
        loadFromContainer(container);
        init();
    }

    private void init() {
        localTileMap = new LocalTileMap(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        createTileMapUpdater();
        camera = new EntitySelector(this);
        timer = new Timer();
        gameCalendar = new GameCalendar();
        paused = false;
        startContainer();
        gameCalendar.addListener("minute", world.getStarSystem());
        world.getStarSystem().init(this);
    }

    private void loadFromContainer(LocalGenContainer container) {
        this.world = container.getWorld();
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
        localMap.init();
    }

    private void startContainer() {
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                turn();
            }
        }, 0, 1f / 60);
    }

    private void createTileMapUpdater() {
        LocalTileMapUpdater localTileMapUpdater = new LocalTileMapUpdater(this);
        localMap.setLocalTileMapUpdater(localTileMapUpdater);
        localTileMapUpdater.flushLocalMap();
    }

    private synchronized void turn() {
        if(!paused) {
            unitContainer.turn();
            plantContainer.turn();
            buildingContainer.turn();
            itemContainer.turn();
            liquidContainer.turn();
            gameCalendar.turn();
        }
    }

    public EntitySelector getCamera() {
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

    public LocalMap getLocalMap() {
        return localMap;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public LocalTileMap getLocalTileMap() {
        return localTileMap;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        TagLoggersEnum.GENERAL.logDebug("Game paused set to " + paused);
        if (paused) {
            timer.stop();
            this.paused = true;
        } else {
            timer.start();
            this.paused = false;
        }
    }
}