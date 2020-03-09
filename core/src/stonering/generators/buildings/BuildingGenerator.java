package stonering.generators.buildings;

import stonering.entity.building.BuildingBlock;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.enums.buildings.BuildingType;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.RenderAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.entity.building.Building;
import stonering.util.global.Logger;

import java.util.List;

/**
 * Generates BuildingType entity from descriptors
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
        initBlocks(building, type);
        return building;
    }

    /**
     * Fills array of building blocks. Passage map of building type is rotated to building orientation.
     */
    private void initBlocks(Building building, BuildingType type) {
        int width = building.blocks.length;
        int height = building.blocks[0].length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < building.blocks[x].length; y++) {
                Position position = Position.add(building.position, x, y, 0); // on map position
                int[] coord = building.orientation.unrotate(x,y);
                coord[0] = (coord[0] + width - 1) % width;
                coord[1] = (coord[1] + height - 1) % height;
                building.blocks[x][y] = new BuildingBlock(building, position, type.passageArray[coord[0]][coord[1]]);
            }
        }
    }

    private void createRenderAspect(Building building, BuildingType type, OrientationEnum orientation) {
        building.addAspect(new RenderAspect(building, type.sprites[orientation.ordinal()], AtlasesEnum.buildings));
    }

    /**
     * Creates aspects from description in type.
     */
    private void initAspects(Building building, BuildingType type) {
        for (List<String> aspect : type.aspects) {
            if (!aspect.isEmpty()) {
                switch (aspect.get(0)) {
                    case "workbench": {
                        building.addAspect(new WorkbenchAspect(building));
                        break;
                    }
                    case "item_container": {
                        ItemContainerAspect itemContainerAspect = new ItemContainerAspect(building, aspect.get(1).split("/"));
                        building.addAspect(itemContainerAspect);
                        break;
                    }
                    case "rest_furniture": {
                        building.addAspect(new RestFurnitureAspect(building));
                        break;
                    }
                }
            }
        }
    }
}
