package stonering.entity.job.action.item;

import stonering.entity.building.aspects.FuelConsumerAspect;
import stonering.entity.job.action.FuelingAciton;
import stonering.entity.job.action.equipment.use.PutItemToContainerAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.Entity;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.crafting.ItemOrder;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.unit.SkillMap;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;

/**
 * Action for crafting item by item order on workbench. Ingredient items will be brought to WB.
 * WB should have {@link WorkbenchAspect} and {@link ItemContainerAspect}.
 * Crafting speed is influenced by unit's performance (see {@link stonering.entity.unit.aspects.health.HealthAspect}), and skill.
 * TODO add intermediate 'unfinished' item like in RW.
 *
 * @author Alexander on 06.01.2019.
 */
public class CraftItemAction extends ItemConsumingAction {
    private ItemOrder itemOrder;
    private Entity workbench;
    private String skill;

    public CraftItemAction(ItemOrder itemOrder, Entity workbench) {
        super(itemOrder, new EntityActionTarget(workbench, ActionTargetTypeEnum.NEAR)); // unit will stand near wb while performing task
        this.itemOrder = itemOrder;
        this.workbench = workbench;
        ItemContainerAspect containerAspect = workbench.get(ItemContainerAspect.class);
        WorkbenchAspect workbenchAspect = workbench.get(WorkbenchAspect.class);
        FuelConsumerAspect fuelAspect = workbench.get(FuelConsumerAspect.class);
        
        //TODO check ingredients and fuel availability before bringing something to workbench.
        //TODO add usage of items in nearby containers.
        startCondition = () -> {
            System.out.println("start check action craft");
            if (workbenchAspect == null)
                return Logger.TASKS.logWarn("Building " + workbench.toString() + " is not a workbench.", FAIL);
            if (!ingredientOrdersValid()) return FAIL; // check/find items for order
            if (checkBringingItems(containerAspect)) return NEW; // bring ingredient items
            if (fuelAspect != null && !fuelAspect.isFueled()) { // workbench requires fuel
                task.addFirstPreAction(new FuelingAciton(workbench));
                return NEW;
            }
            return OK;
        };

        onStart = () -> {
            System.out.println("start action craft");
            maxProgress = itemOrder.recipe.workAmount * (1 + getMaterialWorkAmountMultiplier());
            float performanceBonus = Optional.ofNullable(task.performer.get(HealthAspect.class))
                    .map(aspect -> aspect.stats.get("performance"))
                    .orElse(0f);
            float skillBonus = Optional.ofNullable(SkillMap.getSkill(this.skill))
                    .map(skill -> Optional.ofNullable(task.performer.get(JobSkillAspect.class))
                            .map(aspect -> aspect.skills.get(this.skill).level())
                            .map(level -> level * skill.speed)
                            .orElse(0f)).orElse(0f);
            //TODO add WB tier bonus
            speed = 1 + performanceBonus + skillBonus;
        };

        // Creates item, consumes ingredients. Product item is put to Workbench.
        onFinish = () -> {
            System.out.println("finish action craft");
            Item product = new ItemGenerator().generateItemByOrder(itemOrder);
            // spend components
            List<Item> items = itemOrder.allIngredients().stream()
                    .map(ingredientOrder -> ingredientOrder.items)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            itemContainer.removeItems(items);
            storeProduct(product);
        };
    }

    private boolean checkBringingItems(ItemContainerAspect containerAspect) {
        List<Item> items = getIngredientItems().stream()
                .filter(item -> !containerAspect.items.contains(item)) // item is not in WB
                .collect(Collectors.toList());
        items.forEach(item -> task.addFirstPreAction(new PutItemToContainerAction(containerAspect, item))); // create action
        return !items.isEmpty();
    }

    private void storeProduct(Item product) {
        //TODO put product into WB's bound container
        ItemContainerAspect containerAspect = workbench.get(ItemContainerAspect.class);
        itemContainer.addItem(product);
        itemContainer.containedItemsSystem.addItemToContainer(product, containerAspect);
    }

    @Override
    protected Position getPositionForItems() {
        return workbench.position;
    }

    @Override
    public String toString() {
        return "Crafting action: " + itemOrder.toString();
    }
}
