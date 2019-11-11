package stonering.enums;

import stonering.game.model.system.building.WorkbenchSystem;

/**
 * Enumeration of all statuses of orders.
 *
 * Open - order is just created, and has no task.
 * Active - task is created, and order is waiting for it to complete.
 * Suspended - set on pause by player or automatically. These orders are skipped.
 * Complete - order's task is complete, order will be removed or reopened if it was repeated.
 * Failed - order's task is failed, order will be removed or suspended if it was repeated.
 *
 * See {@link WorkbenchSystem}
 *
 * @author Alexander_Kuzyakov on 20.05.2019.
 */
public enum OrderStatusEnum {
    OPEN,                   // newly created
    ACTIVE,                 // taken by performer
    SUSPENDED,
    COMPLETE,               // complete (removed from workbench)
    FAILED                  // not complete (removed from workbench with warning to player)
}
