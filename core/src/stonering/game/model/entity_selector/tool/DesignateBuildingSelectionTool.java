package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.RotationUtil;

import java.util.Collections;

import static stonering.enums.OrientationEnum.N;

/**
 * Tool for selecting place for new building. Buildings are placed one by one and can be rotated.
 *
 * @author Alexander on 16.03.2020.
 */
public class DesignateBuildingSelectionTool extends SelectionTool {
    private Blueprint blueprint;
    private BuildingType type;

    public void setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        type = BuildingTypeMap.getBuilding(blueprint.building);
        selector().get(BoxSelectionAspect.class).boxEnabled = false;
        selector().size.x = type.size.x;
        selector().size.y = type.size.y;
        validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
        orientation = N; // reset orientation
        updateSpriteAndSize();
    }

    @Override
    public void rotate(boolean clockwise) {
        super.rotate(clockwise);
        updateSpriteAndSize();
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, Collections.singletonList(selector().position)), true).show();
    }

    /**
     * Sets correct sprite to selector. Called on rotation and tool change.
     */
    private void updateSpriteAndSize() {
        selector().size.set(RotationUtil.orientSize(type.size, orientation));
        selector().get(RenderAspect.class).region = blueprint != null
                ? BuildingTypeMap.getBuilding(blueprint.building).getSprite(orientation)
                : null;
    }
}
