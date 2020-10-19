package stonering.entity.environment.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Aspect for entity which emit light.
 * Changes light map of local map on update.
 *
 * @author Alexander on 07.10.2018.
 */
public abstract class AbstractLightSourceAspect extends Aspect {
    protected float force;                  // [0,1]
    protected float previousForce;          // [0,1]

    public AbstractLightSourceAspect(Entity entity) {
        super(entity);
    }
    
    public AbstractLightSourceAspect() {}

    /**
     * Recreates light spot from this source.
     * Should be called on every source move or brightness change.
     */
    public abstract void updateLigtOnMap();

    /**
     * Saves current light source state to be removed on next update.
     */
    protected void saveCurrentSpot() {
        previousForce = force;
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }
}
