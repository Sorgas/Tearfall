package stonering.game.core.model;

import com.badlogic.gdx.utils.Timer;
import stonering.entity.world.World;
import stonering.game.core.model.lists.BuildingContainer;
import stonering.game.core.model.lists.ItemContainer;
import stonering.game.core.model.lists.LiquidContainer;
import stonering.game.core.model.lists.PlantContainer;
import stonering.game.core.model.lists.TaskContainer;
import stonering.game.core.model.lists.UnitContainer;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.tilemaps.LocalTileMap;
import stonering.game.core.view.tilemaps.LocalTileMapUpdater;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.local.environment.GameCalendar;
import stonering.util.global.TagLoggersEnum;

/**
 * Model of game, contains LocalMap and sub-Containers. Inits all components after creation.
 * Time ticks are performed with Timer. Calls turning for all game entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class MainGameModel extends GameModel {
    private Timer timer;                 //makes turns for entity containers and calendar.
    private boolean paused;

    @Override
    public void init() {
        super.init();
        get(LocalTileMapUpdater.class).flushLocalMap();
        timer = new Timer();
        paused = false;
        get(GameCalendar.class).addListener("minute", get(World.class).getStarSystem());
        startContainer();
    }

    /**
     * Loads data from Local generation to gameContainer. //TODO add same for saves
     *
     * @param container
     */
    public void loadFromContainer(LocalGenContainer container) {
        put(container.getWorld());
        put(container.getLocalMap());
        put(new LocalTileMap(get(LocalMap.class)));
        put(new LocalTileMapUpdater());

        put(new PlantContainer(container.getPlants()));
        put(new BuildingContainer(container.getBuildings()));
        put(new UnitContainer(container.getUnits()));
        put(new ItemContainer().placeItems(container.getItems()));
        put(new TaskContainer());
        put(new LiquidContainer().loadWater(container));
        put(new GameCalendar());            // slow game entities make turns through this.
        put(new EntitySelector());          // local map camera
    }

    private void startContainer() {
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                turn();
            }
        }, 0, 1f / 60);
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