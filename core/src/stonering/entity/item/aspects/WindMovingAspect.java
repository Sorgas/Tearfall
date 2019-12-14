package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Items with this aspect will move horizontally with the wind.
 * Combined with slow falling in {@link FallingAspect} will give light and high sailing things.
 * TODO post mvp, create system
 *
 * @author Alexander on 02.08.2019.
 */
public class WindMovingAspect extends Aspect {
    private float speedModifier; // percent of wind speed

    public WindMovingAspect(Entity entity) {
        super(entity);
    }
}
