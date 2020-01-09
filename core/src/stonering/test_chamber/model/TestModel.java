package stonering.test_chamber.model;

import stonering.entity.environment.aspects.CelestialCycleAspect;
import stonering.entity.environment.aspects.CelestialLightSourceAspect;
import stonering.entity.environment.CelestialBody;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.GameModel;
import stonering.game.model.system.*;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.tilemaps.LocalTileMap;
import stonering.util.pathfinding.a_star.AStar;

/**
 * Game model for testing features without starting the game.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public abstract class TestModel extends GameModel {
    public static final int MAP_SIZE = 11;

    public TestModel() {
        createDefaultComponents();
    }

    @Override
    public void init() {
        super.init();
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
        put(new ZonesContainer());
        put(new TaskContainer());
        put(new AStar());
        updateLocalMap();
    }

    /**
     * Creates default local map with 3 layers of soil.
     */
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        for (int x = 0; x < getMapSize(); x++) {
            for (int y = 0; y < getMapSize(); y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, MaterialMap.instance().getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.WALL, MaterialMap.instance().getId("soil"));
                localMap.setBlock(x, y, 2, BlockTypesEnum.FLOOR, MaterialMap.instance().getId("soil"));
            }
        }
    }

    /**
     * Creates world with one cell.
     */
    protected World createWorld() {
        World world = new World(1, 1);
        CelestialBody sun = new CelestialBody();
        sun.addAspect(new CelestialLightSourceAspect(sun));
        float orbitSpeed = 0.01f;
        sun.addAspect(new CelestialCycleAspect(orbitSpeed, sun));
        world.getStarSystem().entities.add(sun);
        return world;
    }

    protected int getMapSize() {
        return MAP_SIZE;
    }
}
