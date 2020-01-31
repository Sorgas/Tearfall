package stonering.enums.action;

/**
 * This priorities are used for selecting tasks.
 * TODO add configuration in config screen.
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public enum TaskPriorityEnum {
    NONE(-1), // task not required. used for counting satisfied needs (RestNeed will return this right after sleep)
    COMFORT(3),
    JOB(5),
    HEALTH_NEEDS(7), // regular needs
    SAFETY(10), // avoiding health harm (heavy need level)
    LIFE(11); // avoiding death

    public int VALUE; // numeric value for comparing priorities

    TaskPriorityEnum(int value) {
        VALUE = value;
    }
}
