package stonering.generators.buildings;

import stonering.entity.building.BuildingType;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.entity.building.Building;
import stonering.util.global.Logger;

import java.util.List;

/**
 * Generates BuildingType entity from descriptors
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class BuildingGenerator {

    public Building generateBuilding(String name, Position position) {
        BuildingType type = BuildingTypeMap.instance().getBuilding(name);
        if (type == null) {
            Logger.BUILDING.logWarn("No building with name '" + name + "' found.");
            return null;
        }
        Building building = new Building(position, type);
        building.setMaterial(38); //TODO replace with material from task
        initAspects(building, type);
        createRenderAspect(building, type);
        initBlocks(building, type);
        return building;
    }

    private void createRenderAspect(Building building, BuildingType type) {
        building.addAspect(new RenderAspect(building, type.atlasXY, AtlasesEnum.buildings));
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
                        itemContainerAspect.setVolume(Integer.parseInt(aspect.get(2)));
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
        building.getBlock().setPosition(building.position);
        building.getBlock().setPassage(type.passage);
    }
}
