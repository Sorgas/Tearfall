package stonering.entity.job.designation;

import stonering.entity.RenderAspect;
import stonering.entity.building.BuildingOrder;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.generation.PlacingTagEnum;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;
import stonering.util.geometry.RotationUtil;

import java.util.function.Function;

/**
 * Stores building order. Rendered on localMap as transparent sprite of a building.
 * Task for building is instantly created on creation of designation.
 * <p>
 * /TODO render building tile instead of stock one.
 *
 * @author Alexander Kuzyakov
 */
public class BuildingDesignation extends Designation {
    public final BuildingOrder order;

    public BuildingDesignation(BuildingOrder order) {
        super(order.position, DesignationTypeEnum.D_BUILD);
        this.order = order;
        createRenderAspect();
    }

    /**
     * Checks that tiles did not become invalid since designation.
     */
    public boolean checkSite() {
        return iterateSite(PlacingTagEnum.get(order.blueprint.placing).VALIDATOR);
    }

    private boolean iterateSite(Function<Position, Boolean> function) {
        if (order.blueprint.construction) return function.apply(order.position); // constructions are single tile
        BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
        IntVector2 size = RotationUtil.orientSize(type.size, order.orientation);
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                if (!function.apply(Position.add(order.position, x, y, 0))) return false;
            }
        }
        return true;
    }

    private void createRenderAspect() {
        if (order.blueprint.construction) {
            BlockTypeEnum blockType = BlockTypeEnum.getType(order.blueprint.building);
            get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(blockType.CODE - 1, 0);
        } else {
            BuildingType type = BuildingTypeMap.getBuilding(order.blueprint.building);
            get(RenderAspect.class).region = type.getSprite(order.orientation); // set sprite of a building
        }
    }
}
