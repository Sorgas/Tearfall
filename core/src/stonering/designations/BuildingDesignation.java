package stonering.designations;

import stonering.global.utils.Position;

/**
 * @author Alexander Kuzyakov
 * created on 26.06.2018
 */
public class BuildingDesignation extends Designation {
    private String building;
    private String material;

    public BuildingDesignation(Position position, String building, String material) {
        super(position);
        this.building = building;
        this.material = material;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
