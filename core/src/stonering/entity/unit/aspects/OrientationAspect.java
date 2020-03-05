package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.OrientationEnum;

/**
 * Holds direction creature is facing to. Is updated by {@link stonering.game.model.system.unit.CreatureMovementSystem}.
 * Updates {@link RenderAspect}
 * 
 * @author Alexander on 05.03.2020.
 */
public class OrientationAspect extends Aspect {
    public OrientationEnum currentOrientation;
    
    public OrientationAspect(Entity entity) {
        super(entity);
    }
}
