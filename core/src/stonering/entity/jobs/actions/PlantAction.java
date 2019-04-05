package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.aspects.ItemPickAction;
import stonering.entity.jobs.actions.target.ActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.SeedAspect;
import stonering.entity.local.items.selectors.SingleItemSelector;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Action for planting seed to a farm.
 * Planting always use single seed item.
 * Seed item should have {@link SeedAspect}
 */
public class PlantAction extends Action {
    private SingleItemSelector seedSelector;

    protected PlantAction(ActionTarget actionTarget, SingleItemSelector seedSelector) {
        super(actionTarget);
        this.seedSelector = seedSelector;
    }

    @Override
    public boolean check() {
        TagLoggersEnum.TASKS.log("Checking planting action");
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

    private Item getSeedFromMap() {
        ItemContainer itemContainer = GameMvc.instance().getModel().get(ItemContainer.class);
        List<Item> items = new ArrayList<>(itemContainer.getItems(actionTarget.getPosition()));
        Item foundItem = seedSelector.selectItem(items);
        return foundItem;
    }

    private Item getSeedFromEquipment() {
        EquipmentAspect equipmentAspect = task.getPerformer().getAspect(EquipmentAspect.class);
        List<Item> items = equipmentAspect.getHauledItems();
        Item foundItem = seedSelector.selectItem(items);
        return foundItem;
    }

    private void createPlant(Item seed) {
        try {
            PlantContainer plantContainer = GameMvc.instance().getModel().get(PlantContainer.class);
            PlantGenerator plantGenerator = new PlantGenerator();
            Plant plant = plantGenerator.generatePlant(seed.getAspect(SeedAspect.class));
            plant.setPosition(actionTarget.getPosition());
            plantContainer.place(plant);
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
    }
}
