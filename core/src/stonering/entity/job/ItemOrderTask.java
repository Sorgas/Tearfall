package stonering.entity.job;

import stonering.entity.crafting.ItemOrder;
import stonering.entity.job.action.Action;

/**
 * Task for using in workbenches, sets {@link ItemOrder} status to
 *
 * @author Alexander on 28.10.2019.
 */
public class ItemOrderTask extends Task {
    public ItemOrder order;

    public ItemOrderTask(ItemOrder order, Action initialAction, int priority) {
        super(order.recipe.name, initialAction, priority);
        this.order = order;
    }
}
