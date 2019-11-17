package stonering.enums.action;

/**
 * This priorities are used for selecting tasks.
 * TODO add configuration in config screen.
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public enum TaskPrioritiesEnum {
    NONE(-1), // task not required. used for counting satisfied needs (RestNeed will return this right after sleep)
    COMFORT(3),
    JOB(5),
    HEALTH_NEEDS(7), // regular needs
    SAFETY(10), // avoiding health harm (heavy need level)
    LIFE(11); // avoiding death

    public int VALUE;

    TaskPrioritiesEnum(int value) {
        VALUE = value;
    }
}
