package stonering.game.model.entity_selector.tool;

import stonering.game.GameMvc;
import stonering.util.geometry.Int3dBounds;
import stonering.util.global.Logger;

/**
 * Tool for selecting entities on localmap.
 * 
 * @author Alexander on 16.03.2020.
 */
public class SelectEntitySelectionTool extends SelectionTool {

    @Override
    public void handleSelection(Int3dBounds bounds) {
        Logger.INPUT.logDebug("selecting entities in " + bounds);
        GameMvc.view().showEntityStage(bounds);
    }

    @Override
    public void cancelSelection() {}
}
