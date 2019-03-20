package stonering.game.model;

import stonering.entity.world.World;
import stonering.game.model.lists.*;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.tilemaps.LocalTileMap;
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
        get(GameCalendar.class).addListener(GameCalendar.MINUTE, get(World.class).getStarSystem());
        get(GameCalendar.class).addListener(GameCalendar.MINUTE, get(PlantContainer.class));
        get(LocalMap.class).init();
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

        put(new PlantContainer(container.getPlants()));
        put(new BuildingContainer(container.getBuildings()));
        put(new UnitContainer(container.getUnits()));
        put(new ZonesContainer());
        put(new ItemContainer().placeItems(container.getItems()));
        put(new TaskContainer());
        put(new LiquidContainer().loadWater(container));
        put(new GameCalendar());            // slow game entities make turns through this.
        put(new EntitySelector());          // local map camera
    }
}