package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.entity.building.BuildingOrder;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.EntitySelectorInputHandler;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Tool for designating construction on local map.
 *
 * @author Alexander on 22.03.2020
 */
public class ConstructionSelectionTool extends SelectionTool {
    private Blueprint blueprint;
    private BlockTypeEnum type;

    public SelectionTool setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        type = BlockTypeEnum.getType(blueprint.building);
        validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
        return this;
    }

    @Override
    public void apply() {
        int x = selectTileForConstruction(BlockTypeEnum.getType(blueprint.building));
        selector().get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        system.allowChangingZLevelOnSelection = false;
        system.allowTwoDimensionsOnSelection = type != WALL;
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
//        Int3dBounds bounds = new Int3dBounds(selector().position, selector().get(BoxSelectionAspect.class).boxStart);
        EntitySelector selector = selector();
        Position cachePosition = new Position();

        // validate position
        for (int x = 0; x < selector.size.x; x++) {
            for (int y = 0; y < selector.size.y; y++) {
                if (!validator.apply(cachePosition.set(selector.position).add(x, y, 0))) {
                    Logger.BUILDING.logWarn("Place invalid.");
                    return;
                }
            }
        }

        GameMvc.model().get(TaskContainer.class).designationSystem.submitBuildingDesignation(createOrder(), 1);
        // lock unique items
    }

    private BuildingOrder createOrder() {
        return null;
    }

    private int selectTileForConstruction(BlockTypeEnum type) {
        if (type != SPACE && type != FARM) return type.CODE - 1;
        Logger.BUILDING.logError("Invalid construction");
        return 0;
    }
}
