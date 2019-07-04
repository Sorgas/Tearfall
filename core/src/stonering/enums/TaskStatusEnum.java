package stonering.enums;

/**
 * Enumeration of all statuses of tasks.
 * Shown in workbenches. Available name depend on status.
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public enum TaskStatusEnum {
    OPEN,                   // newly created
    ACTIVE,                 // taken by performer
    PAUSED,                 // paused by player
    SUSPENDED,              // paused on fail
    COMPLETE,               // complete (removed from container)
    FAILED                  // not complete (removed from container)
}
