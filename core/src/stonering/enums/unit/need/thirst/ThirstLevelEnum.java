package stonering.enums.unit.need.thirst;

import static stonering.enums.action.TaskPriorityEnum.*;

import stonering.enums.action.TaskPriorityEnum;

/**
 * @author Alexander on 8/24/2020
 */
public enum ThirstLevelEnum {
    NONE(TaskPriorityEnum.NONE, 0, "Not thirsty"),
    LIGHT(JOB, 0, "Slightly thirsty"),
    REGULAR(HEALTH_NEEDS, 0, "thirsty"),
    MEDIUM(SAFETY, 0, "Very thirsty"),
    STRONG(SAFETY, 0, "Very thirsty"),
    VERY_STRONG(SAFETY, 0, "Deadly thirsty"),
    DEADLY(SAFETY, 0, "Deadly thirsty");

    public final TaskPriorityEnum priority;
    public final int moodDelta;
    public final String moodMessage;

    ThirstLevelEnum(TaskPriorityEnum priority, int moodDelta, String moodMessage) {
        this.priority = priority;
        this.moodDelta = moodDelta;
        this.moodMessage = moodMessage;
    }

    public static ThirstLevelEnum getLevel(float hunger, float starvation) {
        if (hunger < 0.5f) return NONE;
        if (hunger < 0.8f) return LIGHT;
        if (hunger < 1) return REGULAR;
        if (starvation < 0.4f) return MEDIUM;
        if (starvation < 0.7f) return STRONG;
        if (starvation < 0.85f) return VERY_STRONG;
        return DEADLY;
    }
}
