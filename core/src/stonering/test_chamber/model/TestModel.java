package stonering.test_chamber.model;

import stonering.entity.environment.aspects.CelestialCycleAspect;
import stonering.entity.environment.aspects.CelestialLightSourceAspect;
import stonering.entity.environment.CelestialBody;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.GameModel;
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
import stonering.util.pathfinding.AStar;

/**
 * Game model for testing features without starting the game.
 *
 * @author Alexander Kuzyakov on 02.07.2019.
 */
public abstract class TestModel extends GameModel {
    public static final int MAP_SIZE = 11;

    public TestModel() {
        createDefaultComponents();
    }

    @Override
    public void init() {
        super.init();
        updateLocalMap();
    }

    /**
     * Creates minimal set of model components, required for work.
     */
    void createDefaultComponents() {
        put(createWorld());
        put(new LocalMap(getMapSize(), getMapSize(), 20));
        put(new PlantContainer());
        put(new SubstrateContainer());
        put(new BuildingContainer());
        put(new ItemContainer());
        put(new LocalTileMap());
        put(new EntitySelectorSystem());
        put(new UnitContainer());
        put(new ZoneContainer());
        put(new TaskContainer());
        put(new EntitySelectorSystem());
        put(new AStar());
        put(new LiquidContainer());
    }

    /**
     * Creates default local map with 3 layers of soil.
     */
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        for (int x = 0; x < getMapSize(); x++) {
            for (int y = 0; y < getMapSize(); y++) {
                localMap.blockType.setBlock(x, y, 0, BlockTypeEnum.WALL.CODE, MaterialMap.instance().getId("soil"));
                localMap.blockType.setBlock(x, y, 1, BlockTypeEnum.WALL.CODE, MaterialMap.instance().getId("soil"));
                localMap.blockType.setBlock(x, y, 2, BlockTypeEnum.FLOOR.CODE, MaterialMap.instance().getId("soil"));
            }
        }
    }

    /**
     * Creates world with one cell.
     */
    protected World createWorld() {
        World world = new World(1, 1);
        CelestialBody sun = new CelestialBody();
        sun.add(new CelestialLightSourceAspect(sun));
        float orbitSpeed = 0.01f;
        sun.add(new CelestialCycleAspect(orbitSpeed, sun));
        world.getStarSystem().objects.add(sun);
        return world;
    }

    protected int getMapSize() {
        return MAP_SIZE;
    }
}
