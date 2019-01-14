package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.target.BuildingActionTarget;
import stonering.entity.local.crafting.ItemOrder;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.aspects.ItemContainerAspect;
import stonering.generators.items.ItemGenerator;
import stonering.util.global.TagLoggersEnum;

/**
 * @author Alexander on 24.12.2018.
 */
public class CraftItemInWorkbenchEffectAspect extends EffectAspect {

    ItemOrder order;

    public CraftItemInWorkbenchEffectAspect(Action action, int workAmount, ItemOrder order) {
        super(action, workAmount);
        this.order = order;
    }


    @Override
    protected void applyEffect() {
        spendIngredients();
        createProduct();
    }

    private void spendIngredients() {
        //TODO
    }

    private void createProduct() {
        ItemGenerator itemGenerator = new ItemGenerator();
        Item product = itemGenerator.generateItem(order);
        BuildingActionTarget buildingTargetAspect = ((BuildingActionTarget) action.getTargetAspect());
        ((ItemContainerAspect) buildingTargetAspect.getBuilding().getAspects().get(ItemContainerAspect.NAME)).getItems().add(product);
        TagLoggersEnum.BUILDING.log("Item " + product.getName() + " crafted.");
    }
}
