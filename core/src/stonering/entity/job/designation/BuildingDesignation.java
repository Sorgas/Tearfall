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

    public BuildingDesignation(Position position, String building) {
        super(position, DesignationTypeEnum.BUILD);
        this.building = building;
    }
}
