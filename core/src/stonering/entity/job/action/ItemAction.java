package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

/**
 * Abstract action, which performs manipulations with items.
 * 
 * @author Alexander on 22.06.2020.
 */
public abstract class ItemAction extends Action {
    protected ItemContainer itemContainer;

    protected ItemAction(ActionTarget target) {
        super(target);
        itemContainer = GameMvc.model().get(ItemContainer.class);
    }
}
