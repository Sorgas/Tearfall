package stonering.test_chamber.model;

import stonering.entity.environment.aspects.CelestialCycleAspect;
import stonering.entity.environment.aspects.CelestialLightSourceAspect;
import stonering.entity.environment.CelestialBody;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.GameModel;
import stonering.game.model.PlayerSettlementProperties;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.local_map.BlockTypeMap;
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
import stonering.util.geometry.Position;
import stonering.util.pathfinding.AStar;

/**
 * Game model for testing features without starting the game.
 *
 * @author Alexander Kuzyakov on 02.07.2019.
 */
public abstract class TestModel extends GameModel {
    public static int MAP_SIZE;

    public TestModel() {
        this(11);
    }

    public TestModel(int size) {
        MAP_SIZE = size;
        createDefaultComponents();
    }
    
    @Override
    public void init() {
        super.init();
        updateLocalMap();
        get(EntitySelectorSystem.class).setSelectorPosition(new Position(MAP_SIZE / 2, MAP_SIZE / 2, 2));
    }

    /**
     * Creates minimal set of model components, required for work.
     */
    void createDefaultComponents() {
        put(createWorld());
        put(new LocalMap(MAP_SIZE, MAP_SIZE, 20));
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
        put(new PlayerSettlementProperties());
    }

    /**
     * Creates default local map with 3 layers of soil.
     */
    protected void updateLocalMap() {
        LocalMap localMap = get(LocalMap.class);
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                localMap.blockType.setBlock(x, y, 0, BlockTypeEnum.WALL.CODE, MaterialMap.getId("soil"));
                localMap.blockType.setBlock(x, y, 1, BlockTypeEnum.WALL.CODE, MaterialMap.getId("soil"));
                localMap.blockType.setBlock(x, y, 2, BlockTypeEnum.FLOOR.CODE, MaterialMap.getId("soil"));
            }
        }
    }

    /**
     * Creates world with one cell.
     */
    protected World createWorld() {
        World world = new World(1, 1, 123);
        CelestialBody sun = new CelestialBody();
        sun.add(new CelestialLightSourceAspect(sun));
        float orbitSpeed = 0.01f;
        sun.add(new CelestialCycleAspect(orbitSpeed, sun));
        world.starSystem.objects.add(sun);
        return world;
    }

    private void setWall(int x, int y, int z) {
        BlockTypeMap map = get(LocalMap.class).blockType;
        map.setBlock(x, y, z, BlockTypeEnum.WALL, MaterialMap.getId("soil"));
        if (map.getEnumValue(x, y, z + 1) == BlockTypeEnum.SPACE)
            map.setBlock(x, y, z + 1, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
    }

    private void setSpace(int x, int y, int z) {
        get(LocalMap.class).blockType.setBlock(x, y, z, BlockTypeEnum.SPACE, MaterialMap.getId("air"));
    }

    private void setFloor(int x, int y, int z) {
        get(LocalMap.class).blockType.setBlock(x, y, z, BlockTypeEnum.FLOOR, MaterialMap.getId("soil"));
    }

    public void setWall(Position pos) {
        setWall(pos.x, pos.y, pos.z);
    }

    public void setSpace(Position pos) {
        setSpace(pos.x, pos.y, pos.z);
    }

    public void setFloor(Position pos) {
        setFloor(pos.x, pos.y, pos.z);
    }
}
