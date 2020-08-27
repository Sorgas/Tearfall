package stonering.enums.unit.need.hunger;

import static stonering.enums.action.TaskPriorityEnum.NONE;

import java.util.Comparator;
import java.util.Optional;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodEffect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.FoodCategoryEnum;
import stonering.enums.unit.need.Need;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.unit.HealthSystem;

/**
 * Need for eating. Part of {@link HealthSystem}.
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
 *
 * @author Alexander on 30.09.2019.
 */
public class FoodNeed extends Need {

    public FoodNeed(String moodEffectKey) {
        super(moodEffectKey);
    }

    @Override
    public TaskPriorityEnum countPriority(Unit unit) {
        return HungerLevelEnum.getLevel(unit).priority;
    }

    @Override
    public boolean isSatisfied(Unit unit) {
        return HungerLevelEnum.getLevel(unit).ordinal() <= HungerLevelEnum.REGULAR.ordinal();
    }

    @Override
    public Task tryCreateTask(Unit unit) {
        HungerLevelEnum level = HungerLevelEnum.getLevel(unit);
        if(level.priority == NONE) return null;
        return Optional.ofNullable(level.priority)
                .map(predicate -> findFoodItem(unit, level.foodCategory))
                .map(EatAction::new)
                .map(action -> new Task(action, level.priority.VALUE))
                .orElse(null);
    }

    @Override
    public MoodEffect getMoodPenalty(Unit unit, NeedState state) {
        HungerLevelEnum level = HungerLevelEnum.getLevel(unit);
        return level.moodDelta != 0 
                ? new MoodEffect(moodEffectKey, level.moodMessage, level.moodDelta, -1)
                : null;
    }

    private Item findFoodItem(Unit unit, FoodCategoryEnum maxCategory) {
        //TODO find food in unit's equipment
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return container.objects.stream()
                .filter(item -> !container.equipped.containsKey(item))
                .filter(item -> item.optional(FoodItemAspect.class).map(aspect -> aspect.category.WEIGHT <= maxCategory.WEIGHT).orElse(false)) // olny appropriate items
                .filter(item -> map.passageMap.util.positionReachable(unit.position, item.position, false)) // reachable items 
                .min(Comparator.comparingInt(item -> (int) item.position.getDistance(unit.position))) // nearest item
                .orElse(null);
    }
}
