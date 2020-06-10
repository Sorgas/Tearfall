package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.DinningTableFurnitureAspect;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.enums.unit.health.HungerParameter;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.item.OnMapItemsStream;
import stonering.game.model.system.unit.CreatureHealthSystem;
import stonering.util.geometry.Position;
import stonering.util.global.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static stonering.enums.action.TaskPriorityEnum.NONE;
import static stonering.enums.items.ItemTagEnum.*;
import static stonering.enums.unit.health.HealthParameterEnum.HUNGER;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 * Checks if unit is hungry, and creates task for eating.
 * Item condition affects distance, unit will be ready to travel to it.
 * See also {@link HungerParameter}.
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {
    private Map<ItemTagEnum, Integer> itemsSelectionPriority; // effects of food items tags
    private int priorityToDistanceMultiplier = 60;

    {
        itemsSelectionPriority = new HashMap<>();
        itemsSelectionPriority.put(PREPARED, 1);
        itemsSelectionPriority.put(RAW, -1);
        itemsSelectionPriority.put(SPOILED, -2);
    }

    @Override
    public TaskPriorityEnum countPriority(Unit unit) {
        return unit.getOptional(HealthAspect.class)
                .map(aspect -> aspect.parameters.get(HUNGER).getRelativeValue())
                .map(HUNGER.PARAMETER::getRange)
                .map(range -> range.priority)
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
                .sorted(Comparator.comparing(item -> countItemPriority(item, unit)))
                .findFirst().orElse(null);
    }

    /**
     * Looks for table with adjacent chair on map.
     */
    private Pair<Building, Building> findTableWithChair(Position position) {
        GameMvc.model().get(BuildingContainer.class).stream()
                .filter(building -> building.has(DinningTableFurnitureAspect.class))
                .filter(building -> GameMvc.model().get(LocalMap.class).passageMap.inSameArea(building.position, position))
                .filter(building -> {

                });
        return null;
    }

    private int countItemPriority(Item item, Unit unit) {
        return item.tags.stream()
                .filter(itemsSelectionPriority.keySet()::contains)
                .map(itemsSelectionPriority::get)
                .reduce(Integer::sum).orElse(0);
    }

    private void getChairNearBuiliding() {

    }
}
