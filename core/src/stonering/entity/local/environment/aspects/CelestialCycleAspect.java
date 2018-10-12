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
    private float orbitPos;         //[0,1]
    private float phaseScale;
    private float orbitScale;

    public CelestialCycleAspect(float phaseScale, float orbitScale, AspectHolder aspectHolder) {
        super("celestialCycle", aspectHolder);
        this.phaseScale = phaseScale;
        this.orbitScale = orbitScale;
    }

    public void update(int timeDelta) {
        phase += timeDelta * phaseScale;
        if (phase > 1) {
            phase -= 1;
        }
        orbitPos += timeDelta * orbitScale;
        if (orbitPos > 1) {
            orbitPos -= 1;
        }
        updateOtherAspects();
    }

    private void updateOtherAspects() {
        if (aspectHolder.getAspects().containsKey("lightSource")) {
            ((SelestialLightSource) aspectHolder.getAspects().get("lightSource")).setForce(countLightForce());
        }
    }

    private float countLightForce() {
        return 1f;
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
