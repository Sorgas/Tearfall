package stonering.entity.job.action;

import stonering.entity.job.action.target.PlantActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.item.selectors.ToolWithActionItemSelector;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.geometry.Position;

public class PlantHarvestAction extends Action {
    private ItemSelector toolItemSelector;

    public PlantHarvestAction(AbstractPlant plant) {
        super(new PlantActionTarget(plant));
        toolItemSelector = new ToolWithActionItemSelector("harvest_plants"); //TODO handle harvesting without tool.
    }

    @Override
    public boolean check() {
        PlantContainer container = GameMvc.instance().getModel().get(PlantContainer.class);
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return false;
        AbstractPlant targetPlant = ((PlantActionTarget) actionTarget).getPlant();
        if (container.getPlantInPosition(actionTarget.getPosition()) != targetPlant) return false;
        return toolItemSelector.check(aspect.getEquippedItems()) || addActionToTask();
    }

    private boolean addActionToTask() {
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return false;
        EquipItemAction equipItemAction = new EquipItemAction(target, true);
        task.addFirstPreAction(equipItemAction);
        return true;
    }

    @Override
    public void performLogic() {
        System.out.println("harvesting plant");
        Position position = actionTarget.getPosition();
        PlantBlock block = GameMvc.instance().getModel().get(PlantContainer.class).getPlantBlock(position);
        Item item = new PlantProductGenerator().generateHarvestProduct(block);
        GameMvc.instance().getModel().get(ItemContainer.class).putItem(item, position);
    }
}
