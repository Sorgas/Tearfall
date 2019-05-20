package stonering.enums;

/**
 * Enumeration of all statuses of tasks.
 * Shown in workbenches. Available actions depend on status.
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public enum OrderStatusEnum {
    WAITING, // waiting for execution
    ACTIVE, // currently performing
    PAUSED, // paused by player
    SUSPENDED; // paused by workbench
}
