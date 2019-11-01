package stonering.enums;

/**
 * Statues for tasks.
 *
 * @author Alexander on 01.11.2019.
 */
public enum TaskStatusEnum {
    OPEN,                   // newly created
    ACTIVE,                 // taken by performer
    COMPLETE,               // complete (removed from container)
    FAILED                  // not complete (removed from container)
}
