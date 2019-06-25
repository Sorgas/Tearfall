package stonering.entity.job.action;

import stonering.entity.job.action.aspects.ItemPickAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.aspects.SeedAspect;
import stonering.entity.local.item.selectors.SingleItemSelector;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Action for planting seed to a farm.
 * Planting always use single seed item.
 * Seed item should have {@link SeedAspect}
 */
public class PlantingAction extends Action {
    private SingleItemSelector seedSelector;

    public PlantingAction(ActionTarget actionTarget, SingleItemSelector seedSelector) {
        super(actionTarget);
        this.seedSelector = seedSelector;
    }

    @Override
    public boolean check() {
        Logger.TASKS.log("Checking planting action");
        return getSeedFromMap() != null || getSeedFromEquipment() != null || tryCreatePickingAction();
    }

    @Override
    protected void performLogic() {
        createPlant(spendSeed());
    }

    private boolean tryCreatePickingAction() {
        Item item = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(seedSelector, task.getPerformer().getPosition());
        if (item == null) return false;
        task.addFirstPreAction(new ItemPickAction(item));
        return true;
    }

    /**
     * Seeks seed item in target position on map and in performers inventory.
     */
    private Item spendSeed() {
        Item seed = getSeedFromMap();
        if(seed != null) {
            GameMvc.instance().getModel().get(ItemContainer.class).removeItem(seed);
            return seed;
        }
        seed = getSeedFromEquipment(); // seed should never be null after check()
        task.getPerformer().getAspect(EquipmentAspect.class).dropItem(seed);
        return seed;
    }

    /**
     * Finds seed item on map tiles.
     */
    private Item getSeedFromMap() {
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        List<Item> items = new ArrayList<>(itemContainer.getItemsInPosition(actionTarget.getPosition()));
        Item foundItem = seedSelector.selectItem(items);
        return foundItem;
    }

    /**
     * Finds seed item in performer's inventory.
     */
    private Item getSeedFromEquipment() {
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        List<Item> items = equipmentAspect.getHauledItems();
        Item foundItem = seedSelector.selectItem(items);
        return foundItem;
    }

    /**
     * Creates new plant from seed item in target position.
     */
    private void createPlant(Item seed) {
        try {
            PlantContainer plantContainer = GameMvc.instance().getModel().get(PlantContainer.class);
            PlantGenerator plantGenerator = new PlantGenerator();
            Plant plant = plantGenerator.generatePlant(seed.getAspect(SeedAspect.class));
            plantContainer.place(plant, actionTarget.getPosition());
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
    }
}
