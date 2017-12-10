package stonering.objects.local_actors.building;

/**
 * Created by Alexander on 09.12.2017.
 *
 * Proxy for Building
 */
public class BuildingBlock {
    private Building building;

    public BuildingBlock(Building building) {
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
