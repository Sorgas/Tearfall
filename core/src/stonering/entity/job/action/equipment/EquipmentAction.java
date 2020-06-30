package stonering.entity.job.action.equipment;

import stonering.entity.job.action.ItemAction;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.game.model.system.unit.UnitContainer;
import stonering.util.logging.Logger;

/**
 * Base class for actions that manipulate unit equipment.
 * Equipment tasks are composed of several actions 'get from source' -> 'put to destination'
 * Examples: get from container, get from ground, 
 * 
 * 
 * @author Alexander on 22.06.2020.
 */
public abstract class EquipmentAction extends ItemAction {
    protected CreatureEquipmentSystem system;

    protected EquipmentAction(ActionTarget target) {
        super(target);
        system = GameMvc.model().get(UnitContainer.class).equipmentSystem;
    }
    
    protected EquipmentAspect equipment() {
        return task.performer.get(EquipmentAspect.class);
    }
    
    protected boolean validate() {
        if (!task.performer.has(EquipmentAspect.class))
            return Logger.TASKS.logError("unit " + task.performer + " has no Equipment Aspect.", false);
        return true;
    }
}
