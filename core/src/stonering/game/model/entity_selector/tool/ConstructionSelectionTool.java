package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
import stonering.stage.toolbar.menus.ConstructionTileSelector;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander on 22.03.2020
 */
public class ConstructionSelectionTool extends SelectionTool {
    private Blueprint blueprint;

    public void setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
    }

    @Override
    public void apply() {
        int x = ConstructionTileSelector.select(BlockTypeEnum.getType(blueprint.building));
        selector().get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        List<Position> positions = new ArrayList<>();
        selector().get(BoxSelectionAspect.class).boxIterator.accept(positions::add);
        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, positions), true).show();
    }
}
