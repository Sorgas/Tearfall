package stonering.game.model;

import stonering.entity.world.World;
import stonering.game.model.lists.*;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.entity.local.environment.GameCalendar;
import stonering.util.global.Logger;

/**
 * Model of game, contains LocalMap and sub-Containers. Inits all components after creation.
 * Time ticks are performed with Timer. Calls turning for all game entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class MainGameModel extends GameModel {

    public MainGameModel(LocalMap map) {
        super();
        put(map);
    }

    @Override
    public void init() {
        super.init();
        get(GameCalendar.class).addListener(GameCalendar.MINUTE, get(World.class).getStarSystem());
        get(GameCalendar.class).addListener(GameCalendar.MINUTE, get(PlantContainer.class));
        get(LocalMap.class).init(); //TODO local map inited second time here
    }

    /**
     * Creates model components.
     */
    public void createComponents(World world) {
        Logger.GENERAL.logDebug("creating model components");
        put(world);
        put(new LocalTileMap());
        put(new PlantContainer());
        put(new BuildingContainer());
        put(new UnitContainer());
        put(new ZonesContainer());
        put(new ItemContainer());
        put(new TaskContainer());
        put(new LiquidContainer());
        put(new GameCalendar());            // slow game entities make turns through this.
        put(new EntitySelector());          // local map camera
    }
}