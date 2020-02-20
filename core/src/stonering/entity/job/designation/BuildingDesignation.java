package stonering.entity.job.designation;

import stonering.entity.building.BuildingOrder;
import stonering.enums.designations.DesignationTypeEnum;

/**
 * Stores building order. Rendered on localMap as transparent sprite of a building.
 * Task for building is instantly created on creation of designation, and selected items are locked.
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
