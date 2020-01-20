package stonering.entity.job.designation;

import stonering.entity.building.BuildingOrder;
import stonering.enums.designations.DesignationTypeEnum;

/**
 * Stores building order.
 *
 * /TODO render building tile instead of stock one.
 * @author Alexander Kuzyakov
 */
public class BuildingDesignation extends Designation {
    public final BuildingOrder order;

    public BuildingDesignation(BuildingOrder order) {
        super(order.position, DesignationTypeEnum.D_BUILD);
        this.order = order;
    }
}
