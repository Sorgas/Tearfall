package stonering.entity.local.items.selectors;

import stonering.enums.items.type.ToolItemType;
import stonering.entity.local.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Selects tools which allows specified action.
 *
 * @author Alexander Kuzyakov on 11.09.2018.
 */
public class ToolWithActionItemSelector extends ItemSelector {
    private String actionName;

    public ToolWithActionItemSelector(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public boolean check(List<Item> items) {
        return !selectItems(items).isEmpty();
    }

    @Override
    public List<Item> selectItems(List<Item> items) {
        List<Item> foundItems = new ArrayList<>();
        for (Item item : items) {
            ToolItemType tool;
            if ((tool = item.getType().getTool()) != null) {
                if (tool.getActions().size() > 0) {
                    for (ToolItemType.ToolAction toolAction : tool.getActions()) {
                        if (toolAction.action.equals(actionName)) {
                            foundItems.add(item);
                        }
                    }
                }
            }
        }
        return foundItems;
    }
}
