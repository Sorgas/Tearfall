package stonering.entity.job.action;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

import java.util.List;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.item.selectors.SeedItemSelector;
import stonering.entity.job.action.equipment.ItemPickupAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.plant.Plant;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.logging.Logger;

/**
 * Action for planting seed to a farm.
 * Planting always use single seed item.
 * Seed item should have {@link SeedAspect}
 */
public class PlantingAction extends Action {
    private SeedItemSelector seedSelector;

    public PlantingAction(ActionTarget actionTarget, SeedItemSelector seedSelector) {
        super(actionTarget);
        this.seedSelector = seedSelector;
        startCondition = () -> {
            Logger.TASKS.logDebug("Checking planting action");
            if (getSeedFromEquipment() != null) return OK;
            return tryCreatePickingAction();
        };

        onFinish = () -> {
            Logger.TASKS.logDebug("Planting seed of " + seedSelector.getSpecimen() + " to " + actionTarget.getPosition());
            createPlant(spendSeed());
        };
    }

    /**
     * Tries to pick seed item if none is available in performer's inventory.
     */
    private ActionConditionStatusEnum tryCreatePickingAction() {
        Item item = GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(seedSelector, task.performer.position);
        if (item == null) return FAIL;
        task.addFirstPreAction(new ItemPickupAction(item));
        Logger.TASKS.logDebug("Creating pocking action for " + seedSelector.getSpecimen() + " seed.");
        return NEW;
    }

    /**
     * Seeks seed item in performers inventory.
     */
    private Item spendSeed() {
        Item seed = getSeedFromEquipment(); // seed should never be null after check()
        task.performer.get(EquipmentAspect.class).dropItem(seed);
        return seed;
    }

    /**
     * Looks for seed item in performer's inventory.
     */
    private Item getSeedFromEquipment() {
        EquipmentAspect equipmentAspect = task.performer.get(EquipmentAspect.class);
        return seedSelector.selectItem(equipmentAspect.hauledItems);
    }

    /**
     * Creates new plant from seed item in target position.
     */
    private void createPlant(Item seed) {
        PlantContainer plantContainer = GameMvc.model().get(PlantContainer.class);
        PlantGenerator plantGenerator = new PlantGenerator();
        Plant plant = plantGenerator.generatePlant(seed.get(SeedAspect.class));
        plantContainer.add(plant, target.getPosition());
    }
}
