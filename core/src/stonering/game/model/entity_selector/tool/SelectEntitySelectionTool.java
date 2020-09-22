package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Int3dBounds;
import stonering.util.logging.Logger;

/**
 * Tool for selecting entities on localmap.
 *
 * @author Alexander on 16.03.2020.
 */
public class SelectEntitySelectionTool extends SelectionTool {

    @Override
    public void apply() {
        selector().get(BoxSelectionAspect.class).boxEnabled = true;
        selector().size.set(1, 1);
        selector().get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(0, 0);
        GameMvc.view().toolbarStage.hideTab();
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        system.allowChangingZLevelOnSelection = true;
        system.allowTwoDimensionsOnSelection = true;
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        Logger.INPUT.logDebug("selecting entities in " + bounds);
        GameMvc.view().showEntityStage(bounds);
    }
}
