package stonering.enums;

/**
 * Enumeration of all statuses of orders.
 * Shown in workbenches.
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public enum OrderStatusEnum {
    OPEN,                   // newly created
    ACTIVE,                 // taken by performer
    PAUSED,                 // paused by player
    SUSPENDED,              // paused on fail
    COMPLETE,               // complete (removed from workbench)
    FAILED                  // not complete (removed from workbench with warning to player)
}
