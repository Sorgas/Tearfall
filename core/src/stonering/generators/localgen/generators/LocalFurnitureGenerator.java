package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;
import stonering.entity.local.building.Building;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class LocalFurnitureGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private LocalMap localMap;
    private BuildingGenerator buildingGenerator;

    public LocalFurnitureGenerator(LocalGenContainer container) {
        this.container = container;
        config = container.getConfig();
        localMap = container.getLocalMap();
        buildingGenerator = new BuildingGenerator();
    }

    public void execute() {
//        Position position = findSurfacePosition();
//        Building building = buildingGenerator.generateBuilding("forge", position);
//        container.getBuildings().add(building);
    }

    private Position findSurfacePosition() {
        int x = localMap.getxSize() /2;
        int y = localMap.getySize() /2;
        for (int z = localMap.getzSize() - 1; z > 0; z--) {
            if (localMap.getBlockType(x, y, z) == BlockTypesEnum.FLOOR.getCode()) {
                return new Position(x, y, z);
            }
        }
        return null;
    }

    private Position findPlace() {
        Random random = new Random();
        while (true) {
            int x = random.nextInt();
            int y = random.nextInt(localMap.getySize());
            for (int z = localMap.getzSize() - 1; z > 0; z--) {
                if (localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.getCode() && localMap.getBlockType(x, y, z - 1) == BlockTypesEnum.WALL.getCode()) {
                    return new Position(x, y, z);
                }
            }
        }
    }
}
