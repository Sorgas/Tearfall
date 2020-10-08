package stonering.enums.action;

/**
 * This priorities are used for selecting tasks.
 * TODO add configuration in config screen.
 *
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public enum TaskPriorityEnum {
    NONE(-1),           // task not required.
    COMFORT(3),         // performed, when no job is available
    JOB(5),
    HEALTH_NEEDS(7),    // will stop job for satisfying need
    SAFETY(10);         // avoiding health harm (heavy need level)

    public static final int MAX = 10;
    public final int VALUE; // numeric value for comparing priorities

    TaskPriorityEnum(int value) {
        VALUE = value;
    }
}
