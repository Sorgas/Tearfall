package stonering.enums.action;

/**
 * This priorities are used for selecting tasks.
 * TODO add configuration in config screen.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public enum TaskPriorityEnum {
    NONE(-1, 0.5f), // task not required.
    COMFORT(3, 0.7f), // performed, when no job is available
    JOB(5, 0.7f),
    HEALTH_NEEDS(7, 1), // will stop job for satisfying need
    SAFETY(10, 1.05f); // avoiding health harm (heavy need level)

    public final int VALUE; // numeric value for comparing priorities
    public final float NEED_THRESHOLD;

    TaskPriorityEnum(int value, float needThreshold) {
        VALUE = value;
        NEED_THRESHOLD = needThreshold;
    }
}
