package stonering.game.model;

import stonering.entity.world.World;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.system.*;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.substrate.SubstrateContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.tilemaps.LocalTileMap;
import stonering.util.logging.Logger;
import stonering.util.pathfinding.AStar;

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
    }

    /**
     * Creates model components.
     */
    public void createComponents(World world) {
        Logger.GENERAL.logDebug("creating model components");
        put(world);
        put(new LocalTileMap());
        put(new PlantContainer());
        put(new SubstrateContainer());
        put(new BuildingContainer());
        put(new UnitContainer());
        put(new ZoneContainer());
        put(new ItemContainer());
        put(new TaskContainer());
        put(new LiquidContainer());
        put(new EntitySelectorSystem());          // local map camera
        put(new PlayerSettlementProperties());
        put(new EntityIdGenerator());
        put(new AStar());
    }
}