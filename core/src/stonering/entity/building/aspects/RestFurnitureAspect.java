package stonering.entity.building.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Buildings with this aspect provide rest places.
 * Building should be passable.
 *
 * @author Alexander on 05.09.2019.
 */
public class RestFurnitureAspect extends Aspect {
    public float restModifier; //TODO use to increase resting time.

    public RestFurnitureAspect(Entity entity) {
        super(entity);
        restModifier = 1;
    }
}
