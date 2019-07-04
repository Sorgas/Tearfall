package stonering.test_chamber.model;

import stonering.entity.local.environment.CelestialBody;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.environment.aspects.CelestialCycleAspect;
import stonering.entity.local.environment.aspects.CelestialLightSourceAspect;
import stonering.entity.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.lists.*;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.tilemaps.LocalTileMap;

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

    /**
     * Creates minimal set of model components, required for work.
     */
    void createDefaultComponents() {
        put(createWorld());
        put(createLocalMap(getMapSize()));
        put(new PlantContainer());
        put(new BuildingContainer());
        put(new ItemContainer());
        put(new LocalTileMap());
        put(new EntitySelector());
        put(new UnitContainer());
        put(new ZonesContainer());
        put(new GameCalendar());
        put(new TaskContainer());
    }

    /**
     * Creates default local map with 3 layers of soil.
     */
    protected LocalMap createLocalMap(int size) {
        LocalMap localMap = new LocalMap(size, size, 20);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, MaterialMap.getInstance().getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.WALL, MaterialMap.getInstance().getId("soil"));
                localMap.setBlock(x, y, 2, BlockTypesEnum.FLOOR, MaterialMap.getInstance().getId("soil"));
            }
        }
        return localMap;
    }

    /**
     * Creates world with one cell.
     */
    protected World createWorld() {
        World world = new World(1, 1);
        CelestialBody sun = new CelestialBody();
        sun.addAspect(new CelestialLightSourceAspect(sun));
        float dayScale = 0.01f;
        sun.addAspect(new CelestialCycleAspect(dayScale, dayScale, sun));
        world.getStarSystem().getCelestialBodies().add(sun);
        return world;
    }

    protected int getMapSize() {
        return MAP_SIZE;
    }
}
