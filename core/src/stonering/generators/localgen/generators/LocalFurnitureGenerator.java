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
//        for (int i = 0; i < 100; i++) {
//            Building building = buildingGenerator.generateBuilding("chair");
//            building.setPosition(findPlace());
//            container.getBuildings().add(building);
//        }
    }

    private Position findPlace() {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(localMap.getxSize());
            int y = random.nextInt(localMap.getySize());
            for (int z = localMap.getzSize() - 1; z > 0; z--) {
                if (localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.getCode()
                        && localMap.getBlockType(x, y, z - 1) == BlockTypesEnum.WALL.getCode()) {
                    return new Position(x, y, z);
                }
            }
        }
    }
}
