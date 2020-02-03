package stonering.entity.job.action;

import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.needs.FoodNeed;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

/**
 * Action for consuming edible items and satisfying {@link FoodNeed}.
 * Will create pre actions for using table, chair, dishes, if some available.
 * Action target is always food item, but putting it onto table can be created as pre-action,
 * as well as putting dishes on the table.
 * If table is occupied by dirty dishes, it will be put off.
 * Edible is an item tag, see {@link ItemTagEnum}.
 * TODO all dishes and table behaviour is post-MVP
 *
 * @author Alexander on 30.09.2019.
 */
public class EatAction extends Action {
    private Item item;

    public EatAction(Item item) {
        super(new EntityActionTarget(item, ActionTargetTypeEnum.ANY));
        this.item = item;
        startCondition = () -> {
            if(!item.tags.contains(ItemTagEnum.EDIBLE)) return FAIL; // item is not edible
            ItemContainer container = GameMvc.model().get(ItemContainer.class);
            if(container.contained.containsKey(item)) {

            }
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if (equipment != null && equipment.hauledItems.contains(item)) return OK;
            return OK;
        };


    }

    //TODO
    private Task findTable() {
        BuildingContainer container = GameMvc.model().get(BuildingContainer.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);
        container.buildingBlocks.values().stream()
                .filter(block -> block.building.type.building.equals("table")) // get tables
                .filter(block -> !block.building.occupied) // get free tables
                .filter(block -> map.passageMap.area.get(block.position) == map.passageMap.area.get(task.performer.position));
        return null;
    }

    public Task addActionToOpenContainer() {

    }
}
