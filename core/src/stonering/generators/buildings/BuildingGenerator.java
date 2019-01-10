package stonering.generators.buildings;

import stonering.entity.local.building.BuildingType;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.util.geometry.Position;
import stonering.entity.local.building.Building;
import stonering.util.global.TagLoggersEnum;

import java.util.List;

/**
 * Generates BuildingType entity from descriptors
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class BuildingGenerator {

    public BuildingGenerator() {
        init();
    }

    public void init() {
    }

    public Building generateBuilding(String name, Position position) {
        BuildingType type = BuildingTypeMap.getInstance().getBuilding(name);
        if (type == null) {
            TagLoggersEnum.BUILDING.logWarn("No building with name '" + name + "' found.");
            return null;
        }
        Building building = new Building(position, type);
        building.setName(type.getTitle());
        building.setMaterial(38); //TODO replace with material from task
        initAspects(building, type);
        return building;
    }

    /**
     * Creates aspects from description in type.
     */
    private void initAspects(Building building, BuildingType type) {
        for (List<String> aspect : type.getAspects()) {
            if (!aspect.isEmpty()) {
                switch (aspect.get(0)) {
                    case WorkbenchAspect.NAME: {
                        WorkbenchAspect workbenchAspect = new WorkbenchAspect(building);
                        building.getAspects().put(WorkbenchAspect.NAME, workbenchAspect);
                        break;
                    }
                    case ItemContainerAspect.NAME: {
                        ItemContainerAspect itemContainerAspect = new ItemContainerAspect(building);
                        itemContainerAspect.setItemType(aspect.get(1));
                        itemContainerAspect.setVolume(Integer.parseInt(aspect.get(2)));
                        building.getAspects().put(ItemContainerAspect.NAME, itemContainerAspect);
                        break;
                    }
                }
            }
        }
    }
}
