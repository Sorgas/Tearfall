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
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;
import stonering.entity.building.Building;
import stonering.util.geometry.RotationUtil;
import stonering.util.global.Logger;

import java.util.List;

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
            for (int y = 0; y < building.blocks[x].length; y++) {
                Position position = Position.add(building.position, x, y, 0); // map position of block
                IntVector2 coord = RotationUtil.unrotateVector(x, y, building.orientation);
                coord = RotationUtil.normalizeWithSize(coord, size);
                building.blocks[x][y] = new BuildingBlock(building, position, building.type.passageArray[coord.x][coord.y]);
            }
        }
        building.blocks[0][0].drawn = true;
    }

    private void createRenderAspect(Building building, BuildingType type, OrientationEnum orientation) {
        int[] sprite = type.sprites[orientation.ordinal()];
        building.addAspect(new RenderAspect(building, sprite[0], sprite[1], type.size[0], type.size[1], AtlasesEnum.buildings));
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
