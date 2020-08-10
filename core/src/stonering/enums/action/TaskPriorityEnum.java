package stonering.enums.action;

/**
 * This priorities are used for selecting tasks.
 * TODO add configuration in config screen.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public enum TaskPriorityEnum {
    NONE(-1, 0.6f), // task not required. used for counting satisfied needs (RestNeed will return this right after sleep)
    COMFORT(3, 0.7f),
    JOB(5, 0.8f),
    HEALTH_NEEDS(7, 0.9f), // regular needs
    SAFETY(10, 0.95f), // avoiding health harm (heavy need level)
    LIFE(11, 1); // avoiding death

    public final int VALUE; // numeric value for comparing priorities
    public final float NEED_THRESHOLD;

    TaskPriorityEnum(int value, float needThreshold) {
        VALUE = value;
        NEED_THRESHOLD = needThreshold;
    }
}
