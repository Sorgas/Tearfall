package stonering.entity.job.designation;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 7/19/2020
 */
public class PlantingDesignation extends Designation {
    public final String specimen;

    public PlantingDesignation(Position position, String specimen) {
        super(position, DesignationTypeEnum.D_PLANT);
        this.specimen = specimen;
    }
}
