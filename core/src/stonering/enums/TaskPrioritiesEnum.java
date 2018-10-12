package stonering.enums;

/**
 * @author Alexander Kuzyakov on 16.09.2018.
 */
public enum TaskPrioritiesEnum {
    LOW(3), //comfort
    NORMAL(5), //jobs
    HIGH(7), // avoiding health harm
    EXTREME(10); //avoiding death

    private int value;

    TaskPrioritiesEnum(int value) {
        this.value = value;
    }
}
