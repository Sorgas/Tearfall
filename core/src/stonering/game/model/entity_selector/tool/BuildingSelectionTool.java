package stonering.game.model.entity_selector.tool;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.RenderAspect;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.job.designation.BuildingDesignation;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.entity_menu.building.ItemSelectSection;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.RotationUtil;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.OrientationEnum.*;
import static stonering.stage.renderer.atlas.AtlasesEnum.ui_tiles;

/**
 * Tool for selecting place for new building. Buildings are placed one by one and can be rotated.
 * On selecting place, new {@link BuildingDesignation} is created.
 * Selector for building materials are created basing on {@link stonering.stage.entity_menu.building.BuildingMaterialTab}.
 * Materials set for last designation of this building or default materials will be set for designation.
 * If player holds lCtrl while confirming building, material selection menu appears.
 * <p>
 *
 * @author Alexander on 16.03.2020.
 */
public class BuildingSelectionTool extends SelectionTool {
    private Blueprint blueprint;
    private BuildingType type;
    public List<IntVector2> accessPoints = new ArrayList<>();
    public TextureRegion workbenchAccessSprite;

    public SelectionTool setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        type = BuildingTypeMap.getBuilding(blueprint.building);
        return this;
    }

    @Override
    public void apply() {
        selector().get(BoxSelectionAspect.class).boxEnabled = false;
        selector().size.set(type.size);
        validator = PlaceValidatorEnum.getValidator(blueprint.placing);
        orientation = N; // reset orientation
        workbenchAccessSprite = ui_tiles.getBlockTile(11, 3);
        updateSpriteAndSize();
        GameMvc.view().toolbarStage.showBuildingTab(blueprint);
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        system.allowChangingZLevelOnSelection = true;
        system.allowTwoDimensionsOnSelection = true;
    }

    @Override
    public void rotate(boolean clockwise) {
        super.rotate(clockwise);
        updateSpriteAndSize();
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        EntitySelector selector = selector();
        // validate position
        boolean positionsValid = selector.getSelectorPositions().stream().allMatch(validator::apply);
        if (!positionsValid) {
            Logger.BUILDING.logWarn("Place invalid.");
            return;
        }
        if(!buildingTabValid()) {
            Logger.BUILDING.logWarn("Not all materials selected.");
            return;
        }
        GameMvc.model().get(TaskContainer.class).designationSystem.submitBuildingDesignation(createOrder(), 1);
        // TODO lock unique items
    }

    /**
     * Sets correct sprite to selector. Called on rotation and tool change.
     */
    private void updateSpriteAndSize() {
        IntVector2 rotatedSize = RotationUtil.rotateVector(type.size, orientation);
        IntVector2 correction = rotatedSize.invertToPositive();
        if (correction.x > 0) correction.x--;
        if (correction.y > 0) correction.y--;
        selector().size.set(rotatedSize);
        selector().get(RenderAspect.class).region = blueprint != null
                ? BuildingTypeMap.getBuilding(blueprint.building).getSprite(orientation)
                : null;
        accessPoints.clear();
        type.access.stream()
                .map(vector -> RotationUtil.rotateVector(vector, orientation).add(correction))
                .forEach(accessPoints::add);
    }

    /**
     * Ensures that material items selected for all parts of a building.
     */
    private boolean buildingTabValid() {
        return blueprint.ingredients.keySet().stream()
                .map(GameMvc.view().toolbarStage.buildingTab.sectionMap::get)
                .allMatch(ItemSelectSection::isAtLeastOneSelected);
    }
    
    private BuildingOrder createOrder() {
        BuildingOrder order = new BuildingOrder(blueprint, selector().position.clone());
        order.orientation = orientation;
        blueprint.ingredients.forEach((key, ingredient) -> {
            ItemSelectSection section = GameMvc.view().toolbarStage.buildingTab.sectionMap.get(key);
            ItemSelector itemSelector = section.getItemSelector();
            order.ingredientOrders.put(key, new IngredientOrder(ingredient, itemSelector));
        });
        return order;
    }
}
