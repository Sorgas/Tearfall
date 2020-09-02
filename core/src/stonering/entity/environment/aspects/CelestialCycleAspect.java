package stonering.entity.environment.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.time.TimeUnitEnum;
import stonering.util.lang.Initable;

/**
 * Determines body state change over time. Has orbit position.
 * Currently, all bodies except current planet considered orbiting this planet(sun too).
 *
 * @author Alexander Kuzyakov
 */
public class CelestialCycleAspect extends Aspect implements Initable {
    public static String NAME = "celestial_cycle";
    private float orbitPos;         //[0,1] position on orbit in radians * 2
    private float orbitSpeed;       // part of orbit passed in one minute

    public CelestialCycleAspect(float orbitSpeed, Entity entity) {
        super(entity);
        this.orbitSpeed = orbitSpeed;
    }

    private float countLightForce() {
        return (float) (Math.cos(orbitPos * 2 * Math.PI) + 1) / 2f;
    }

    @Override
    public void init() {
        updateOtherAspects();
    }

    /**
     * Updates brightness of celestial body. Should be called each minute.
     */
    //TODO add longitude

    public void turnUnit(TimeUnitEnum unit) {
        //TODO move to system
        if (unit != TimeUnitEnum.MINUTE) return;
            orbitPos += orbitSpeed;
        if (orbitPos > 1) {
            orbitPos -= 1;
        }
        updateOtherAspects();
    }

    private void updateOtherAspects() {
        if (entity.get(CelestialLightSourceAspect.class) == null) return;
        entity.get(CelestialLightSourceAspect.class).setForce(countLightForce());
    }

    public float getOrbitPos() {
        return orbitPos;
    }
}
