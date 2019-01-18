package stonering.entity.jobs.actions;

import stonering.entity.jobs.actions.target.PlantActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.geometry.Position;

import java.util.List;

public class PlantHarvestAction extends Action {
    private ItemSelector toolItemSelector;

    public PlantHarvestAction(AbstractPlant plant) {
        super(new PlantActionTarget(plant));
        toolItemSelector = new ToolWithActionItemSelector("harvest_plants"); //TODO handle harvesting without tool.
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = (EquipmentAspect) task.getPerformer().getAspects().get("equipment");
        if (aspect == null) return false;
        return toolItemSelector.check(aspect.getEquippedItems()) || addActionToTask();
    }

    private boolean addActionToTask() {
        Item target = gameMvc.getModel().getItemContainer().getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return false;
        EquipItemAction equipItemAction = new EquipItemAction(target, true);
        task.addFirstPreAction(equipItemAction);
        return true;
    }

    @Override
    public boolean perform() {
        System.out.println("harvesting plant");
        AbstractPlant abstractPlant = ((PlantActionTarget) actionTarget).getPlant();
        Position position = actionTarget.getPosition();
        PlantBlock block = gameMvc.getModel().getLocalMap().getPlantBlock(position);
        if (block != null && block.getPlant() == abstractPlant) {
            List<Item> items = new PlantProductGenerator().generateHarvestProduct(block);
            items.forEach(item -> gameMvc.getModel().getItemContainer().putItem(item, position));
        }
        return true;
    }
}
