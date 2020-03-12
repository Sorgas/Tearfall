package stonering.entity.job.action.target;

import stonering.entity.building.BuildingOrder;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;
import stonering.util.geometry.RotationUtil;

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

    /**
     * Finds appropriate position for builder to build from and bring materials.
     * TODO find nearest position.
     */
    public boolean findPositionForBuilder(BuildingOrder order, Position currentBuilderPosition) {
        BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
        Position inBuildingPosition = getPassableBuildingTile(order);
        if(inBuildingPosition != null) {
            builderPosition = inBuildingPosition;
        } else {
            Int2dBounds bounds = defineBuildingBounds(order);
            builderPosition = new NeighbourPositionStream(center)
                    .filterByAccessibilityWithFutureTile(type)
                    .filterInArea(GameMvc.model().get(LocalMap.class).passageMap.area.get(currentBuilderPosition))
                    .stream.findFirst().orElse(null);
            if(builderPosition != null) targetType = EXACT;
        }
        return builderPosition != null;
    }

    private Int2dBounds defineBuildingBounds(BuildingOrder order) {
        BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
        IntVector2 orientedSize = RotationUtil.orientSize(type.size, order.orientation);
        Position position = order.position;
        return new Int2dBounds(position.x, position.y, position.x + orientedSize.x, position.y + orientedSize.y);
    }

    private Position getPassableBuildingTile(BuildingOrder order) {
        BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
        IntVector2 orientedSize = RotationUtil.orientSize(type.size, order.orientation);
        for (int x = 0; x < type.size.x; x++) {
            for (int y = 0; y < type.size.y; y++) {
                if(type.passageArray[x][y] == PassageEnum.PASSABLE) {
                    IntVector2 rotatedVector = RotationUtil.rotateVector(new IntVector2(x,y), order.orientation);
                    RotationUtil.normalizeWithSize(rotatedVector, orientedSize);
                    return Position.add(order.position, rotatedVector.x, rotatedVector.y, 0); // return first found passable position
                }
            }
        }
        return null;
    }

    public void reset() {
        builderPosition = null;
        targetType = NEAR;
    }
}
