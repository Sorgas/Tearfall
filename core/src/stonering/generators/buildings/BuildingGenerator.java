package stonering.generators.buildings;

import stonering.entity.building.BuildingBlock;
import stonering.entity.building.aspects.DinningTableFurnitureAspect;
import stonering.entity.building.aspects.SitFurnitureAspect;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.BuildingType;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.RenderAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;
import stonering.entity.building.Building;
import stonering.util.geometry.RotationUtil;
import stonering.util.logging.Logger;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Generates {@link Building} objects.
 * Building type contains building properties(size, passage) without orientation.
 * Generator changes size, if building is rotated, selects sprite, rotates passage map and puts passage info into blocks.
 * Fills positions for blocks.
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class BuildingGenerator {

    public Building generateBuilding(String name, Position position, OrientationEnum orientation) {
        BuildingType type = BuildingTypeMap.getBuilding(name);
        if (type == null) return Logger.BUILDING.logWarn("No building with name '" + name + "' found.", null);

        Building building = new Building(position, type, orientation);

        initAspects(building, type); // fill aspects of a building
        createRenderAspect(building, type, orientation); // find sprite
        initSizeAndBlocks(building);
        return building;
    }

    /**
     * Fills array of building blocks. Passage map of building type is rotated to building orientation.
     */
    private void initSizeAndBlocks(Building building) {
        IntVector2 size = RotationUtil.orientSize(building.type.size, building.orientation); // rotate size
        building.blocks = new BuildingBlock[size.x][size.y];
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                Position position = Position.add(building.position, x, y, 0); // map position of block
                building.blocks[x][y] = new BuildingBlock(building, position, building.type.passage);
            }
        }
    }

    private void createRenderAspect(Building building, BuildingType type, OrientationEnum orientation) {
        building.add(new RenderAspect(type.getSprite(orientation)));
    }

    /**
     * Creates aspects from description in type.
     */
    private void initAspects(Building building, BuildingType type) {
        type.aspects.keySet().forEach(aspectName -> {
            List<String> args = type.aspects.get(aspectName);
            switch (aspectName) {
                case "workbench": {
                    building.add(new WorkbenchAspect(building));
                    break;
                }
                case "item_container": {
                    building.add(new ItemContainerAspect(building)); // TODO use parameters
                    break;
                }
                case "rest_furniture": {
                    float spriteRotation = 0;
                    if (NumberUtils.isNumber(args.get(0))) {
                        spriteRotation = Float.parseFloat(args.get(0));
                    } else {
                        Logger.GENERATION.logError("Sprite rotation value for building " + type.title + " is invalid");
                    }
                    building.add(new RestFurnitureAspect(building, spriteRotation));
                    break;
                }
                case "dinning_table": {
                    building.add(new DinningTableFurnitureAspect(building));
                }
                case "sit_furniture": {
                    building.add(new SitFurnitureAspect(building));
                }
            }
        });
    }
}
