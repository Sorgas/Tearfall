package stonering.enums.unit.need.hunger;

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
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.need.Need;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedEnum;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.FoodCategoryEnum;
import stonering.enums.items.ItemTagEnum;
import stonering.enums.unit.health.HungerParameter;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.CreatureHealthSystem;
import stonering.util.geometry.Position;

/**
 * Need for eating. Part of {@link CreatureHealthSystem}.
 * Checks if unit is hungry, and creates task for eating.
 * Item condition affects distance which unit will be ready to travel to it.
 * The more hungry unit is, the worse food it will eat.
 * 50-80% - prepared food, fruits,
 * 80-100% - not prepared food,
 * When malnutrition disease is present, unit will eat stale food, raw meat and corpses.
 * 0-40% - raw meat,
 * 40-70% - stale food,
 * 70-85% - animal corpses,
 * 85+% - meat and corpses of sapient species
 * <p>
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
        return HungerLevelEnum.getLevel(hungerLevel(unit), starvationLevel(unit)).priority;
    }

    @Override
    public Task tryCreateTask(Unit unit) {
        HungerLevelEnum level = HungerLevelEnum.getLevel(hungerLevel(unit), starvationLevel(unit));
        TaskPriorityEnum priority = countPriority(unit);
        if(priority == NONE) return null;

        return Optional.ofNullable(priority)
                .map(p -> getPredicate(hungerLevel(unit), starvationLevel(unit)))
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
        HungerLevelEnum level = HungerLevelEnum.getLevel(hungerLevel(unit), starvationLevel(unit));
        return level.moodDelta != 0 
                ? new MoodEffect("hunger", level.moodMessage, level.moodDelta, -1) 
                : null;
    }

    private Item findFoodItem(Unit unit, FoodCategoryEnum maxCategory) {
        //TODO find food in unit's equipment
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);
        unit.get(EquipmentAspect.class).
        
        return container.objects.stream()
                .filter(item -> !container.equipped.containsKey(item))
                .filter(item -> item.optional(FoodItemAspect.class).map(aspect -> aspect.category.WEIGHT <= maxCategory.WEIGHT).orElse(false)) // olny appropriate items
                .filter(item -> map.passageMap.util.positionReachable(unit.position, item.position, false)) // reachable items 
                .min(Comparator.comparingInt(item -> (int) item.position.getDistance(unit.position))) // nearest item
                .orElse(null);
    }

    private float hungerLevel(Unit unit) {
        return unit.get(NeedAspect.class).needs.get(NeedEnum.FOOD).getRelativeValue();
    }

    private float starvationLevel(Unit unit) {
        return unit.get(BodyAspect.class).diseases.get("starvation").progress;
    }
}
