package stonering.entity.item.selectors;

import stonering.entity.item.Item;

/**
 * Selects tools which allows specified name.
 *
 * @author Alexander Kuzyakov on 11.09.2018.
 */
public class ToolWithActionItemSelector extends ItemSelector {
    private String actionName;

    public ToolWithActionItemSelector(String actionName) {
        this.actionName = actionName;
    }

    /**
     * Checks that item is tool and has name with name.
     */
    @Override
    public boolean checkItem(Item item) {
        return item.isTool() && item.getType().tool.getActions().stream().anyMatch(action -> action.action.equals(actionName));
    }
}
