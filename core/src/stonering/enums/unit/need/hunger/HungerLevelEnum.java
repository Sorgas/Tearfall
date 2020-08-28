package stonering.enums.unit.need.hunger;

import static stonering.enums.action.TaskPriorityEnum.*;
import static stonering.enums.items.FoodCategoryEnum.*;

import stonering.entity.unit.Unit;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.items.FoodCategoryEnum;
import stonering.enums.unit.need.NeedEnum;

/**
 * Used in {@link FoodNeed}.
 *
 * @author Alexander on 20.08.2020.
 */
public enum HungerLevelEnum {
    NONE(TaskPriorityEnum.NONE, 0, "Not hungry", READY_TO_EAT),
    LIGHT(JOB, 0, "Slightly hungry", READY_TO_EAT),
    REGULAR(HEALTH_NEEDS, 0, "Hungry", UNPREPARED),
    MEDIUM(SAFETY, 0, "Very hungry", RAW_MEAT),
    STRONG(SAFETY, 0, "Very hungry", STALE_FOOD),
    VERY_STRONG(SAFETY, 0, "Deadly hungry", CORPSE),
    DEADLY(SAFETY, 0, "Deadly hungry", SAPIENT);

    public final TaskPriorityEnum priority;
    public final int moodDelta;
    public final String moodMessage;
    public final FoodCategoryEnum foodCategory;
    
    HungerLevelEnum(TaskPriorityEnum priority, int moodDelta, String moodMessage, FoodCategoryEnum foodCategory) {
        this.priority = priority;
        this.moodDelta = moodDelta;
        this.moodMessage = moodMessage;
        this.foodCategory = foodCategory;
    }

    public static HungerLevelEnum getLevel(Unit unit) {
        float hunger = NeedEnum.FOOD.NEED.needLevel(unit);
        if (hunger < 0.5f) return NONE;
        if (hunger < 0.8f) return LIGHT;
        if (hunger < 1) return REGULAR;
        float starvation = NeedEnum.FOOD.NEED.diseaseLevel(unit);
        if (starvation < 0.4f) return MEDIUM;
        if (starvation < 0.7f) return STRONG;
        if (starvation < 0.85f) return VERY_STRONG;
        return DEADLY;
    }
}
