package stonering.entity.local.environment.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;

/**
 * Determines body state change over time. Has orbit position and phase.
 * Currently, all bodies except current planet considered orbiting this planet(sun too).
 *
 * @author Alexander Kuzyakov
 */
public class CelestialCycleAspect extends Aspect {
    private float phase;            //[0,1]
    private float orbitPos;         //[0,1] position on orbit in radians * 2
    private float phaseScale;
    private float orbitScale;       // part of orbit passed in one minute

    public CelestialCycleAspect(float phaseScale, float orbitScale, AspectHolder aspectHolder) {
        super("celestial_cycle", aspectHolder);
        this.phaseScale = phaseScale;
        this.orbitScale = orbitScale;
    }

    private void updateOtherAspects() {
        if (aspectHolder.getAspects().containsKey("light_source")) {
            ((SelestialLightSource) aspectHolder.getAspects().get("light_source")).setForce(countLightForce());
        }
    }

    private float countLightForce() {
//        System.out.println((float) (Math.cos(orbitPos * 2 * Math.PI) + 1) / 2f);
        return (float) (Math.cos(orbitPos * 2 * Math.PI) + 1) / 2f;
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
