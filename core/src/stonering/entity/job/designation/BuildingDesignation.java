package stonering.entity.job.designation;

import stonering.entity.building.BuildingOrder;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;
import stonering.util.geometry.RotationUtil;

import java.util.function.Function;

/**
 * Stores building order. Rendered on localMap as transparent sprite of a building.
 * Task for building is instantly created on creation of designation.
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

    public boolean checkSite() {
        return iterateSite(PlaceValidatorsEnum.getValidator(order.blueprint.placing));
    }

    private boolean iterateSite(Function<Position, Boolean> function) {
        BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
        IntVector2 size = RotationUtil.orientSize(type.size, order.orientation);
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                if (!function.apply(Position.add(order.position, x, y, 0))) return false;
            }
        }
        return true;
    }
}
