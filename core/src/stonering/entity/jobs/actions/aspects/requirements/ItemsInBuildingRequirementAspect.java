package stonering.entity.jobs.actions.aspects.requirements;

import stonering.entity.jobs.actions.Action;
import stonering.entity.local.building.Building;
import stonering.entity.local.items.selectors.ItemSelector;

import java.util.List;

/**
 * Checks that specified items are in building container. building should have itemcontainer aspect.
 */
public class ItemsInBuildingRequirementAspect extends RequirementsAspect {
    private Building building;
    private List<ItemSelector> itemSelectors;

    public ItemsInBuildingRequirementAspect(Action action) {
        super(action);
    }

    @Override
    public boolean check() {
        if(building.getAspects().containsKey())
        return false;
    }
}
