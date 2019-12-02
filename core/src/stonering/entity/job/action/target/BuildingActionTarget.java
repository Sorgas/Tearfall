package stonering.entity.job.action.target;

import stonering.entity.building.BuildingOrder;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.util.geometry.Position;

import static stonering.enums.action.ActionTargetTypeEnum.EXACT;
import static stonering.enums.action.ActionTargetTypeEnum.NEAR;

/**
 * Action target for buildings.
 * Can be switched to active state to hold additional position for builder
 * to ensure that items are brought to and building is built from same position.
 *
 * @author Alexander on 02.12.2019.
 */
public class BuildingActionTarget extends ActionTarget {
    public final Position center;
    public Position builderPosition;

    public BuildingActionTarget(BuildingOrder order) {
        super(ActionTargetTypeEnum.EXACT);
        center = order.position;
    }

    @Override
    public Position getPosition() {
        return builderPosition != null ? builderPosition : center;
    }

    public boolean findPositionForBuilder(BuildingOrder order, Position currentBuilderPosition) {
        BlockTypesEnum type = BlockTypesEnum.getType(BuildingTypeMap.instance().getBuilding(order.blueprint.building).passage);
        builderPosition = new NeighbourPositionStream(center)
                .filterByAccessibilityWithFutureTile(type)
                .filterByArea(GameMvc.instance().model().get(LocalMap.class).passageMap.area.get(currentBuilderPosition))
                .stream.findFirst().orElse(null);
        if(builderPosition != null) targetType = EXACT;
        return builderPosition != null;
    }

    public void reset() {
        builderPosition = null;
        targetType = NEAR;
    }
}
