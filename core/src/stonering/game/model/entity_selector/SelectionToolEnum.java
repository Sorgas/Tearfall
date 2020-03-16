package stonering.game.model.entity_selector;

import stonering.game.model.entity_selector.tool.SelectEntitySelectionTool;
import stonering.game.model.entity_selector.tool.SelectionTool;

/**
 * @author Alexander_Kuzyakov on 16.03.2020.
 */
public enum SelectionToolEnum {
    SELECT(new SelectEntitySelectionTool()),
    ;
    
    public final SelectionTool TOOL;

    SelectionToolEnum(SelectionTool TOOL) {
        this.TOOL = TOOL;
    }
}
