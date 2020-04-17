package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.blocks.BlockTypeEnum.FARM;
import static stonering.enums.blocks.BlockTypeEnum.SPACE;

/**
 * Tool for designating construction on local map.
 *
 * @author Alexander on 22.03.2020
 */
public class ConstructionSelectionTool extends SelectionTool {
    private Blueprint blueprint;

    public SelectionTool setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
        return this;
    }

    @Override
    public void apply() {
        int x = selectTileForConstruction(BlockTypeEnum.getType(blueprint.building));
        selector().get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        List<Position> positions = new ArrayList<>();
        selector().get(BoxSelectionAspect.class).boxIterator.accept(positions::add);
        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, positions), true).show();
    }

    private int selectTileForConstruction(BlockTypeEnum type) {

        if (type != SPACE && type != FARM) return type.CODE - 1;
        Logger.BUILDING.logError("Invalid construction");
        return 0;
    }
}
