package stonering.entity.unit.aspects.need;

import static stonering.enums.action.TaskPriorityEnum.*;
import static stonering.enums.items.ItemTagEnum.*;

import java.util.*;
import java.util.function.Predicate;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodEffect;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.DiseaseState;
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
 * The more hungry unit is, the worse food it will eat.
 * 0-80% - prepared food, fruits,
 * 80-100% - not prepared food,
 * When malnutrition disease is present, unit will eat stale food, raw meat and corpses.
 * 0-40% - stale food,
 * 40-70% - raw meat,
 * 70-85% - animal corpses,
 * 85+% - meat and corpses of sapient species
 * 
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
        float hungerLevel = hungerLevel(unit);
        if(hungerLevel < 0.5f) return NONE;
        if(hungerLevel < 0.8f) return JOB;
        if(hungerLevel < 1) return HEALTH_NEEDS;
        return SAFETY;
    }

    @Override
    public Task tryCreateTask(Unit unit) {
        NeedState state = unit.get(NeedAspect.class).needs.get(NeedEnum.FOOD);
        TaskPriorityEnum priority = countPriority(unit);
        switch(priority) {
            case NONE:
                return null;
            case COMFORT:
            case JOB:
            case HEALTH_NEEDS:
                break;
            case SAFETY:
                break;
        }
        
        return Optional.ofNullable(priority)
                .map(p -> getPredicate(p, starvationLevel(unit)))
                .map(predicate -> findFoodItem(unit, predicate))
                .map(EatAction::new)
                .map(action -> new Task(action, priority.VALUE))
                .orElse(null);
    }

    @Override
    public DiseaseState createDisease() {
        return null;
    }

    @Override
    public MoodEffect getMoodPenalty(Unit unit, NeedState state) {
        return null;
    }

    private Predicate<Item> getPredicate(float hunger, float starvation) {
        if(hunger < 0.5f) return item -> false; // never reached
        if(hunger < 0.8f) return item -> !item.tags.contains(RAW) && !item.tags.contains(SPOILED); // prepared food and fruits
        if(hunger < 1) return item -> !(item.tags.contains(MEAT) && item.tags.contains(MEAT)) && !item.tags.contains(SPOILED); // not prepared food, like turnip
        if(starvation < 0.4f) return item -> !(item.tags.contains(MEAT) && item.tags.contains(MEAT)); // stale food
        if(starvation < 0.7f) return item -> true; // raw meat
        if(starvation < 0.85f) return item -> false; // animal corpses
        return item -> false; // sapient meat
        
        
        
        switch (priority) { // will eat any food (incl. raw and spoiled) for HEALTH_NEEDS priority
            case JOB:
                return ;
            case HEALTH_NEEDS:
                return
            case SAFETY:
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

    private float hungerLevel(Unit unit) {
        return unit.get(NeedAspect.class).needs.get(NeedEnum.FOOD).getRelativeValue();
    }

    private float starvationLevel(Unit unit) {
        return unit.get(BodyAspect.class).diseases.get("starvation").progress;
    }
}
