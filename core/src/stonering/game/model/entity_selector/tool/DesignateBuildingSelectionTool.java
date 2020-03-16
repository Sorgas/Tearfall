package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
import stonering.stage.toolbar.menus.ConstructionTileSelector;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.OrientationEnum.N;

/**
 * Tool for selecting place for new building.
 * After setting blueprint, it can be rotated.
 *
 * @author Alexander on 16.03.2020.
 */
public class DesignateBuildingSelectionTool extends SelectionTool {
    private Blueprint blueprint;
    private BuildingType type;
    private OrientationEnum orientation;
    private PositionValidator validator;

    public void resetFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        type = BuildingTypeMap.getBuilding(blueprint.building);
        orientation = N;
        setBlueprintForBuilding(blueprint);
    }

    @Override
    public void rotate(boolean clockwise) {
        super.rotate(clockwise);
        // rotate sprite
        selector().get(RenderAspect.class).region = AtlasesEnum.buildings.getBlockTile(type.sprites[orientation.ordinal()]);
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        List<Position> positions = new ArrayList<>();
        selector().get(BoxSelectionAspect.class).boxIterator.accept(positions::add);
        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, positions), true).show();
    }

    @Override
    public void cancelSelection() {
        selector().get(SelectionAspect.class).set(SelectionToolEnum.SELECT);
    }

    public void setBlueprintForBuilding(Blueprint blueprint) {
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        setSpriteToSelector(system, blueprint);
        validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
        system.inputHandler.allowChangingZLevelOnSelection = false;
    }

    private void setSpriteToSelector(EntitySelectorSystem system, Blueprint blueprint) {
        RenderAspect render = system.selector.get(RenderAspect.class);
        if (blueprint.construction) { // special sprites for constructions todo remove to another tool
            int x = ConstructionTileSelector.select(BlockTypeEnum.getType(blueprint.building));
            render.region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
        } else { // building sprites for buildings
            render.region = BuildingTypeMap.getBuilding(blueprint.building).getSprite(N);
            orientation = N;
        }
    }
}
