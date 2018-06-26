package stonering.objects.local_actors.building;

/**
 * @author Alexander Kuzyakov on 09.12.2017.
 * <p>
 * Proxy for Building
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
