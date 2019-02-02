package stonering.entity.local.building;

import stonering.game.core.model.local_map.LocalMap;

/**
 * Proxy for BuildingType. This is stored on {@link LocalMap} and points to real object.
 *
 * @author Alexander Kuzyakov on 09.12.2017.
 */
public class BuildingBlock {
    private Building building;

    public BuildingBlock(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
