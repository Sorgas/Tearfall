package stonering.objects.jobs.actions.aspects.requirements;

import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.effect.DropItemEffectAspect;
import stonering.objects.jobs.actions.aspects.effect.PickUpItemEffectAspect;
import stonering.objects.jobs.actions.aspects.target.BlockTargetAspect;
import stonering.objects.jobs.actions.aspects.target.ItemTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.ItemSelector;

import java.util.ArrayList;

/**
 * Checks if specific items are on desired position.
 * Creates hauling actions if needed.
 *
 * @author Alexander on 08.07.2018.
 */
public class ItemsOnPositionRequirementAspect extends RequirementsAspect {
    private Position target;
    private ArrayList<ItemSelector> items;

    public ItemsOnPositionRequirementAspect(Action action, Position target, ArrayList<ItemSelector> items) {
        super(action);
        this.target = target;
        this.items = items;
    }

    @Override
    public boolean check() {
        ArrayList<Item> itemsOnSite = (ArrayList<Item>) action.getGameContainer().getItemContainer().getItems(target).clone();
        for (ItemSelector itemSelector : items) {
            if ()
                return false;
        }
        return true;
    }

    private boolean addHaulingActionsToTask(Item item) {
        Action pickAction = new Action(action.getGameContainer());
        pickAction.setRequirementsAspect(new BodyPartRequirementAspect(pickAction, "grab"));
        pickAction.setTargetAspect(new ItemTargetAspect(pickAction, item));
        pickAction.setEffectAspect(new PickUpItemEffectAspect(pickAction));

        Action dropAction = new Action(action.getGameContainer());
        dropAction.setRequirementsAspect(new EquippedItemRequirementAspect(dropAction, ""));
        dropAction.setTargetAspect(new BlockTargetAspect(dropAction, target));
        dropAction.setEffectAspect(new DropItemEffectAspect(dropAction, item));
        action.getTask().addFirstAction();
    }
}
