package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.enums.unit.health.HungerParameter;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.ItemsStream;
import stonering.game.model.system.unit.CreatureHealthSystem;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static stonering.enums.items.ItemTagEnum.*;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 * See also {@link HungerParameter}.
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {
    private Map<ItemTagEnum, Integer> itemsSelectionPriority; // effects of food items tags

    {
        itemsSelectionPriority = new HashMap<>();
        itemsSelectionPriority.put(PREPARED, 1);
        itemsSelectionPriority.put(RAW, -1);
        itemsSelectionPriority.put(SPOILED, -2);
    }

    @Override
    public TaskPriorityEnum countPriority(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        float relativeHunger = aspect.parameters.get(HealthParameterEnum.HUNGER).getRelativeValue();
        return HealthParameterEnum.HUNGER.PARAMETER.getRange(relativeHunger).priority;
    }

    @Override
    public Task tryCreateTask(Entity entity) {
        TaskPriorityEnum priority = countPriority(entity);
        Predicate<Item> predicate = null;
        switch (priority) { // will eat any food (incl. raw and spoiled) for HEALTH_NEEDS priority
            case NONE: // no task if not hungry
                return null;
            case COMFORT: // eat prepared and unprepared food
                predicate = item -> !item.tags.contains(RAW) && !item.tags.contains(SPOILED);
        }
        Item item = getBestAvailableFood(entity, predicate);
        if (item == null) return null;
        Action eatAction = new EatAction(item);
        return new Task("eat", eatAction, priority.VALUE);
    }

    /**
     * Selects best food item available to creature. Bad food quality decreases task priority.
     * Substracted from hunger level, this will make units refuse to eat bad food even being very hungry.
     */
    private Item getBestAvailableFood(Entity entity, Predicate<Item> additionalFilter) {
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        EquipmentAspect aspect = entity.getAspect(EquipmentAspect.class);
        return new ItemsStream()
                .filterHasTag(EDIBLE)
                .filter(item -> !container.equipped.containsKey(item) || container.equipped.get(item) == aspect)
                .filter(additionalFilter != null ? additionalFilter : item -> true)
                .filterByReachability(entity.position)
                .sorted(Comparator.comparing(this::countPriority))
                .getNearestTo(entity.position);
    }

    private int countPriority(Item item) {
        return item.tags.stream()
                .filter(itemsSelectionPriority.keySet()::contains)
                .map(itemsSelectionPriority::get)
                .reduce(Integer::sum).orElse(0);
    }
}
