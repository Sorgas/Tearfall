package stonering.entity.job.action;

import stonering.entity.item.Item;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.needs.FoodNeed;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.items.TagEnum;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for consuming edible items and satisfying {@link FoodNeed}.
 * Edible is an item tag, see {@link TagEnum}.
 *
 * @author Alexander on 30.09.2019.
 */
public class EatAction extends Action {
    private Item item;

    public EatAction(Item item) {
        super(new EntityActionTarget(item, ActionTargetTypeEnum.ANY));
        this.item = item;
    }

    /**
     * TODO create action to put food to table and use dishes.
     */
    @Override
    public ActionConditionStatusEnum check() {
        if(!item.tags.contains(TagEnum.EDIBLE)) return FAIL; // item is not edible
        if(checkBetterFood()) return FAIL; // better food is available, recreate.
        //TODO if tables available, use
        //TODO if dishes available, use
        Unit performer = task.performer;
        if(performer.hasAspect(EquipmentAspect.class)) {
            if(performer.getAspect(EquipmentAspect.class).hauledItems.contains(item)) return OK;
        } else {

        }
        return OK;
    }

    /**
     * Consumes item, restores hunger in {@link HealthAspect}.
     */
    @Override
    protected void performLogic() {
        //TODO
    }

    /**
     * Checks that more suitable item for food is available.
     * Action is failed, and will be recreated with best food item on next {@link FoodNeed} update.
     */
    private boolean checkBetterFood() {
        //TODO if item is not prepared and prepared available, fail
        //TODO if item is raw and not raw available, fail
        //TODO if item is spoiled and not spoiled available, fail
        return false;
    }

    private boolean tryCreateActionForDishes() {
        //TODO by food type, select dish type
//        GameMvc.instance().model().get(ItemContainer.class).getNearestItemWithTag(task.getPerformer().position, )
        return false;
    }

    private boolean tryCreateActionForTables() {
        return false;
    }
}
