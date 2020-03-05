package stonering.generators.buildings;

import com.badlogic.gdx.Input;
import stonering.enums.OrientationEnum;
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

import static stonering.enums.OrientationEnum.N;

/**
 * Generates BuildingType entity from descriptors
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class BuildingGenerator {

    public Building generateBuilding(String name, Position position, OrientationEnum orientation) {
        BuildingType type = BuildingTypeMap.instance().getBuilding(name);
        if (type == null) {
            Logger.BUILDING.logWarn("No building with name '" + name + "' found.");
            return null;
        }
        Building building = new Building(position, type);
        building.setMaterial(38); //TODO replace with material from task
        initAspects(building, type);
        createRenderAspect(building, type, orientation);
        initBlocks(building, type);
        return building;
    }

    private void createRenderAspect(Building building, BuildingType type, OrientationEnum orientation) {
        building.addAspect(new RenderAspect(building, type.sprites.get(orientation), AtlasesEnum.buildings));
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
                    case ItemContainerAspect.NAME: {
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

    private void initBlocks(Building building, BuildingType type) {
        building.getBlock().position = building.position;
        building.getBlock().passage = type.passage;
    }
}
