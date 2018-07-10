package stonering.designations;

import stonering.enums.designations.DesignationTypes;
import stonering.global.utils.Position;

/**
 * @author Alexander Kuzyakov
 * created on 26.06.2018
 */
public class BuildingDesignation extends Designation {
    private String building;

    public BuildingDesignation(Position position, DesignationTypes type, String building) {
        super(position, type);
        this.building = building;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
