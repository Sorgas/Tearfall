package stonering.entity.job.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.util.geometry.Position;

/**
 * Adds building NAME to designation.
 *
 * /TODO render building tile instead of stock one.
 * @author Alexander Kuzyakov
 */
public class BuildingDesignation extends Designation {
    public final String building; // building name

    public BuildingDesignation(Position position, DesignationTypeEnum type, String building) {
        super(position, type);
        this.building = building;
    }
}
