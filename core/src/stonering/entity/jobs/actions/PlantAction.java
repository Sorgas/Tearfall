package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.target.ActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.model.lists.ItemContainer;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

public class PlantAction extends Action {
    private ItemSelector seedSelector;

    protected PlantAction(ActionTarget actionTarget) {
        super(actionTarget);
    }

    @Override
    public boolean check() {
        TagLoggersEnum.TASKS.log("Checking planting action: " + buildingType.getBuilding());
        ArrayList<Item> uncheckedItems = new ArrayList<>(gameMvc.getModel().get(ItemContainer.class).getItems(actionTarget.getPosition()));
        uncheckedItems.addAll(((EquipmentAspect) task.getPerformer().getAspects().get(EquipmentAspect.NAME)).getHauledItems()); // from performer inventory
        for (ItemSelector itemSelector : itemSelectors) {
            List<Item> selectedItems = itemSelector.selectItems(uncheckedItems);
            if (selectedItems.isEmpty()) return tryCreateDroppingAction(itemSelector); // create actions for item
            for (Item selectedItem : selectedItems) {
                uncheckedItems.remove(selectedItem);
            }
        }
        return true; // all selectors have items.
    }

    @Override
    protected void performLogic() {

    }
}
