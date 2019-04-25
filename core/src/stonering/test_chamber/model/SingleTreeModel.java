package stonering.test_chamber.model;

import stonering.entity.local.environment.CelestialBody;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.environment.aspects.CelestialCycleAspect;
import stonering.entity.local.environment.aspects.CelestialLightSourceAspect;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Tree;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.lists.UnitContainer;
import stonering.game.model.lists.ZonesContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.lists.PlantContainer;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

public class SingleTreeModel extends GameModel {
    private static final int MAP_SIZE = 11;
    private static final int TREE_CENTER = 5;

    public SingleTreeModel() {
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
        put(new PlantContainer(createTree()));
        put(new LocalTileMap(get(LocalMap.class)));
        put(new EntitySelector());
        put(new GameCalendar());
        put(new UnitContainer());
        put(new TaskContainer());
        put(new ZonesContainer());

    }

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

    private List<AbstractPlant> createTree() {
        List<AbstractPlant> plants = new ArrayList<>();
        TreeGenerator treeGenerator = new TreeGenerator();
        Tree tree = treeGenerator.generateTree("willow", 0);
        tree.setPosition(new Position(TREE_CENTER, TREE_CENTER, 2));
        plants.add(tree);
        return plants;
    }

    private World createWorld() {
        World world = new World(1,1);
        CelestialBody sun = new CelestialBody();
        sun.addAspect(new CelestialLightSourceAspect(sun));
        float dayScale = 0.01f;
        sun.addAspect(new CelestialCycleAspect(dayScale, dayScale, sun));
        world.getStarSystem().getCelestialBodies().add(sun);
        return world;
    }

    @Override
    public String toString() {
        return "SingleTreeModel";
    }
}
