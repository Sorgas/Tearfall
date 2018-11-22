package stonering.designations;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.global.utils.Position;

/**
 * Adds building name to designation.
 *
 * /TODO render building tile instead of stock one.
 * @author Alexander Kuzyakov
 */
public class BuildingDesignation extends Designation {
    private String building;

    public BuildingDesignation(Position position, DesignationTypeEnum type, String building) {
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
