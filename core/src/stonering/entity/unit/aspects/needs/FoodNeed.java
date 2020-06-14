package stonering.entity.unit.aspects.needs;

import static stonering.enums.action.TaskPriorityEnum.NONE;
import static stonering.enums.items.ItemTagEnum.*;
import static stonering.enums.unit.health.HealthParameterEnum.HUNGER;

import java.util.*;
import java.util.function.Predicate;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HungerParameter;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureHealthSystem;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 * Checks if unit is hungry, and creates task for eating.
 * Item condition affects distance which unit will be ready to travel to it.
 * See also {@link HungerParameter}.
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {
    private Map<ItemTagEnum, Integer> itemsSelectionPriority; // effects of food items tags
    private int priorityToDistanceMultiplier = 40; //TODO

    public FoodNeed() {
        itemsSelectionPriority = new HashMap<>();
        itemsSelectionPriority.put(PREPARED, 1);
        itemsSelectionPriority.put(RAW, -1);
        itemsSelectionPriority.put(SPOILED, -2);
    }

    @Override
    public TaskPriorityEnum countPriority(Unit unit) {
        return unit.getOptional(HealthAspect.class)
                .map(aspect -> aspect.parameters.get(HUNGER).getRelativeValue())
                .map(relValue -> HUNGER.PARAMETER.getRange(relValue).priority)
                .orElse(NONE);
    }

    @Override
    public Task tryCreateTask(Unit unit) {
        TaskPriorityEnum priority = countPriority(unit);
        return Optional.of(priority)
                .filter(p -> p != NONE)
                .map(this::getPredicate)
                .map(predicate -> findFoodItem(unit, predicate))
                .map(EatAction::new)
                .map(action -> new Task("eat", action, priority.VALUE))
                .orElse(null);
    }

    /**
     * Creates predicate for filtering food items basing on need severity.
     */
    private Predicate<Item> getPredicate(TaskPriorityEnum priority) {
        switch (priority) { // will eat any food (incl. raw and spoiled) for HEALTH_NEEDS priority
            case COMFORT: // eat prepared and unprepared food
            case JOB:
            case HEALTH_NEEDS:
                return item -> !item.tags.contains(RAW) && !item.tags.contains(SPOILED);
            case SAFETY:
                return item -> !item.tags.contains(SPOILED);
            case LIFE:
                return item -> true;
            case NONE: // no task if not hungry
            default:
                return null;
        }
    }

    /**
     * Selects best food item available to creature. Bad food quality decreases task priority.
     * Substracted from hunger level, this will make units refuse to eat bad food even being very hungry.
     */
    private Item findFoodItem(Unit unit, Predicate<Item> predicate) {
        //TODO find food in unit's equipment
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return container.objects.stream()
                .filter(item -> !container.equipped.containsKey(item))
                .filter(item -> item.has(FoodItemAspect.class))
                .filter(predicate)
                .filter(item -> map.passageMap.util.positionReachable(unit.position, item.position, false))
                .min(Comparator.comparingInt(item -> countItemPriority(item, unit)))
                .orElse(null);
    }

    /**
     * Calculated item 'priority'. Priority is higher for near and prepared items, and lower for raw and spoiled items.
     */
    private int countItemPriority(Item item, Unit unit) {
        return (int) item.position.getDistance(unit.position) -
                item.tags.stream()
                        .map(itemsSelectionPriority::get)
                        .filter(Objects::nonNull)
                        .map(value -> value * priorityToDistanceMultiplier)
                        .reduce(0, Integer::sum);
    }
}
