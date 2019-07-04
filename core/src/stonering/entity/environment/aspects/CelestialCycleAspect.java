package stonering.entity.local.environment.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Determines body state change over time. Has orbit position and phase.
 * Currently, all bodies except current planet considered orbiting this planet(sun too).
 *
 * @author Alexander Kuzyakov
 */
public class CelestialCycleAspect extends Aspect {
    public static String NAME = "celestial_cycle";
    private float phase;            //[0,1]
    private float orbitPos;         //[0,1] position on orbit in radians * 2
    private float phaseScale;
    private float orbitScale;       // part of orbit passed in one minute

    public CelestialCycleAspect(float phaseScale, float orbitScale, Entity entity) {
        super(entity);
        this.phaseScale = phaseScale;
        this.orbitScale = orbitScale;
    }

    private float countLightForce() {
//        System.out.println((float) (Math.cos(orbitPos * 2 * Math.PI) + 1) / 2f);
        return (float) (Math.cos(orbitPos * 2 * Math.PI) + 1) / 2f;
    }

    @Override
    public void init() {
        super.init();
        updateOtherAspects();
    }

    /**
     * Updates brightness of celestial body. Should be called each minute.
     */
    //TODO add longitude
    @Override
    public void turn() {
        orbitPos += orbitScale;
        if (orbitPos > 1) {
            orbitPos -= 1;
        }
        updateOtherAspects();
    }

    private void updateOtherAspects() {
        if (entity.getAspect(CelestialLightSourceAspect.class) == null) return;
        entity.getAspect(CelestialLightSourceAspect.class).setForce(countLightForce());
    }

    public float getOrbitPos() {
        return orbitPos;
    }

    public float getPhase() {
        return phase;
    }

    public float getPhaseScale() {
        return phaseScale;
    }
}
