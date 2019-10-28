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

    public ItemOrderTask(String name, Action initialAction, ItemOrder order, int priority) {
        super(name, initialAction, priority);
        this.order = order;
    }
}
