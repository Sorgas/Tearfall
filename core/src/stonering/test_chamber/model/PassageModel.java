package stonering.test_chamber.model;

import stonering.entity.local.environment.CelestialBody;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.environment.aspects.CelestialCycleAspect;
import stonering.entity.local.environment.aspects.CelestialLightSourceAspect;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.lists.BuildingContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.PassageMap;
import stonering.game.view.tilemaps.LocalTileMap;

/**
 * Model for testing {@link PassageMap} functionality
 *
 * @author Alexander_Kuzyakov on 16.05.2019.
 */
public class PassageModel extends GameModel {
    private static final int MAP_SIZE = 11;

    public PassageModel() {
        reset();
    }

    @Override
    public void init() {
        super.init();
        get(GameCalendar.class).addListener("minute", get(World.class).getStarSystem());
        get(GameCalendar.class).addListener("minute", get(PlantContainer.class));
    }

    /**
     * Recreates model.
     */
    public void reset() {
        put(createWorld());
        put(createMap());
        put(new PlantContainer());
        put(new BuildingContainer());
        put(new LocalTileMap(get(LocalMap.class)));
        put(new EntitySelector());
        put(new GameCalendar());
    }

    /**
     * Creates flat layers of soil.
     * //TODO add more complex relief
     */
    private LocalMap createMap() {
        LocalMap localMap = new LocalMap(MAP_SIZE, MAP_SIZE, 20);
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                localMap.setBlock(x, y, 0, BlockTypesEnum.WALL, materialMap.getId("soil"));
                localMap.setBlock(x, y, 1, BlockTypesEnum.WALL, materialMap.getId("soil"));
                localMap.setBlock(x, y, 2, BlockTypesEnum.FLOOR, materialMap.getId("soil"));
            }
        }
        return localMap;
    }

    /**
     * Creates world with one cell.
     */
    private World createWorld() {
        World world = new World(1, 1);
        CelestialBody sun = new CelestialBody();
        sun.addAspect(new CelestialLightSourceAspect(sun));
        float dayScale = 0.01f;
        sun.addAspect(new CelestialCycleAspect(dayScale, dayScale, sun));
        world.getStarSystem().getCelestialBodies().add(sun);
        return world;
    }

    @Override
    public String toString() {
        return "PassageModel";
    }
}
