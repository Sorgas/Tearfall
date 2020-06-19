package stonering.generators.localgen.generators;

import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.buildings.BuildingGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

/**
 * Generates buildings on local generation
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class LocalBuildingGenerator extends LocalGenerator {
    private BuildingGenerator buildingGenerator;

    public LocalBuildingGenerator(LocalGenContainer container) {
        super(container);
        buildingGenerator = new BuildingGenerator();
    }

    public void execute() {
        Logger.GENERATION.log("generating buildings");
        Position position = findSurfacePosition();
        if(position == null) return;
        Building building = buildingGenerator.generateBuilding("forge", position, OrientationEnum.N);
        container.model.get(BuildingContainer.class).addBuilding(building);
    }

    /**
     * Returns position on the ground in the center of the map.
     * @return
     */
    private Position findSurfacePosition() {
        LocalMap localMap = container.model.get(LocalMap.class);
        int x = localMap.xSize /2;
        int y = localMap.ySize /2;
        for (int z = localMap.zSize - 1; z > 0; z--) {
            if (localMap.blockType.get(x, y, z) != BlockTypeEnum.SPACE.CODE) {
                return new Position(x, y, z);
            }
        }
        return null;
    }
}
