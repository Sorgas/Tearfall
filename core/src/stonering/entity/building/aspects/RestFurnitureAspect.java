package stonering.entity.building.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Buildings with this aspect provide rest places.
 * Building should be passable.
 * TODO add this to seats with penalty to resting time
 *
 * @author Alexander on 05.09.2019.
 */
public class RestFurnitureAspect extends Aspect {
    public float restModifier; //TODO use to increase resting time.
    public float spriteRotation; // creature sprite is rotated while resting

    public RestFurnitureAspect(Entity entity, float spriteRotation) {
        super(entity);
        restModifier = 1;
        this.spriteRotation = spriteRotation;
    }
}
