package stonering.entity.local.building.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.crafting.ItemOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect for workbenches. Manages orders of workbench.
 *
 * @author Alexander on 01.11.2018.
 */
public class WorkbenchAspect extends Aspect {
    public static String NAME = "workbench";

    private List<ItemOrder> orders;

    public WorkbenchAspect(AspectHolder aspectHolder) {
        super(NAME, aspectHolder);
        orders = new ArrayList<>();
    }

    public List<ItemOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ItemOrder> orders) {
        this.orders = orders;
    }
}
