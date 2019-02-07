package stonering.entity.local.environment.aspects;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.local_map.LocalMap;
import stonering.entity.local.AspectHolder;


/**
 * Light source of sun, moon, etc. Emits rays from up to down, first non-space tile becomes illuminated.
 * Should be used only for bodies that emit noticeable amount of light.
 * Should be updated by {@link CelestialCycleAspect}
 *
 * @author Alexander Kuzyakov
 */
public class CelestialLightSource extends AbstractLighSourceAspect {
    public static String NAME = "celestial_light_source";

    public CelestialLightSource(AspectHolder aspectHolder) {
        super(aspectHolder);
    }

    @Override
    public void init() {
        super.init();
        applyLightToMap();
    }

    @Override
    public String getName() {
        return NAME;
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
        LocalMap localMap = gameContainer.get(LocalMap.class);
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = localMap.getzSize() - 1; z >= 0; z--) {
                    localMap.generalLight.changeValue(x, y, z, forceDelta);
                    if (localMap.getBlockType(x, y, z) != BlockTypesEnum.SPACE.getCode()) {
                        break;
                    }
                }
            }
        }
        saveCurrentSpot();
    }

    @Override
    public void turn() {
        updateLigtOnMap();
    }
}
