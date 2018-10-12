package stonering.entity.local.building;

/**
 * Proxy for BuildingType. This is stored on {@link stonering.game.core.model.LocalMap} and points to real object.
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
