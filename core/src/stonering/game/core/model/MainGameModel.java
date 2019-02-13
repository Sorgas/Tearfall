package stonering.game.core.model;

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

/**
 * Model of game, contains LocalMap and sub-Containers. Inits all components after creation.
 * Time ticks are performed with Timer. Calls turning for all game entity.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class MainGameModel extends GameModel {

    @Override
    public void init() {
        super.init();
        get(LocalTileMapUpdater.class).flushLocalMap();
        get(GameCalendar.class).addListener(GameCalendar.MINUTE, get(World.class).getStarSystem());
        get(GameCalendar.class).addListener(GameCalendar.MINUTE, get(PlantContainer.class));
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
}