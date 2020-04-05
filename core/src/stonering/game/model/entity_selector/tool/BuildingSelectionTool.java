package stonering.game.model.entity_selector.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.RenderAspect;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.RotationUtil;
import stonering.util.global.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static stonering.enums.OrientationEnum.*;
import static stonering.stage.renderer.AtlasesEnum.ui_tiles;

/**
 * Tool for selecting place for new building. Buildings are placed one by one and can be rotated.
 * On selecting place, new {@link BuildingDesignation} is created.
 * Materials set for last designation of this building or default materials will be set for designation.
 * If player holds lCtrl while confirming building, material selection menu appears.
 *
 * Has list of additional sprites with offsets to selector's position. Currently used for workbenches access points.
 * TODO If player holds lShift while confirming place, multiple buildings are added.
 *
 * @author Alexander on 16.03.2020.
 */
public class BuildingSelectionTool extends SelectionTool {
    private Blueprint blueprint;
    private BuildingType type;
    public List<IntVector2> additionalSprites = new ArrayList<>();
    public TextureRegion workbenchAccessSprite;

    public void setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        type = BuildingTypeMap.getBuilding(blueprint.building);
        selector().get(BoxSelectionAspect.class).boxEnabled = false;
        selector().size.x = type.size.x;
        selector().size.y = type.size.y;
        validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
        orientation = N; // reset orientation
        workbenchAccessSprite = ui_tiles.getBlockTile(1, 3);
        updateSpriteAndSize();
    }

    @Override
    public void rotate(boolean clockwise) {
        super.rotate(clockwise);
        updateSpriteAndSize();
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
        } else {
            // create designation with default settings
        }
        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, Collections.singletonList(selector().position)), true).show();
    }

    /**
     * Sets correct sprite to selector. Called on rotation and tool change.
     */
    private void updateSpriteAndSize() {
        IntVector2 rotatedSize = RotationUtil.rotateVector(type.size, orientation);
        IntVector2 correction = rotatedSize.invertToPositive();
        if(correction.x > 0) correction.x--;
        if(correction.y > 0) correction.y--;
        selector().size.set(rotatedSize);
        selector().get(RenderAspect.class).region = blueprint != null
                ? BuildingTypeMap.getBuilding(blueprint.building).getSprite(orientation)
                : null;
        additionalSprites.clear();
        type.access.stream()
                .map(vector -> RotationUtil.rotateVector(vector, orientation).add(correction))
                .forEach(additionalSprites::add);
    }
}
