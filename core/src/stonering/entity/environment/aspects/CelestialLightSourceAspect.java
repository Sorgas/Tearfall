package stonering.entity.environment.aspects;

import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.lang.Initable;

/**
 * Light source of sun, moon, etc.
 * Can consider {@link CelestialCycleAspect} to calculate light amount.
 *
 * @author Alexander Kuzyakov
 */
public class CelestialLightSourceAspect extends AbstractLightSourceAspect implements Initable {
    public static String NAME = "celestial_light_source";

    public CelestialLightSourceAspect(Entity entity) {
        super(entity);
    }

    public CelestialLightSourceAspect() {
    }

    @Override
    public void init() {
        applyLightToMap();
    }

    @Override
    public void updateLigtOnMap() {
        applyLightToMap();
    }

    /**
     * Applies light from this source by rays from up to down.
     */
    private void applyLightToMap() {
        byte forceDelta = (byte) ((force - previousForce) * Byte.MAX_VALUE);
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        localMap.light.generalLight += forceDelta;
        saveCurrentSpot();
    }

    /**
     *
     */
    private void updateForce() {
        if(entity.has(CelestialCycleAspect.class)) {
//            entity.getAspect(CelestialCycleAspect.class).
        }
    }
}
