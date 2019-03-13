package stonering.designations;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.util.geometry.Position;

/**
 * Adds building NAME to designation.
 *
 * /TODO render building tile instead of stock one.
 * @author Alexander Kuzyakov
 */
public class BuildingDesignation extends Designation {
    private String building;
    private boolean construction;

    public BuildingDesignation(Position position, DesignationTypeEnum type, String building) {
        super(position, type);
        this.building = building;
        construction = BlockTypesEnum.hasType(building);
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public boolean isConstruction() {
        return construction;
    }

    public void setConstruction(boolean construction) {
        this.construction = construction;
    }
}
