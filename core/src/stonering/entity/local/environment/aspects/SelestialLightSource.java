package stonering.entity.local.environment.aspects;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.entity.local.AspectHolder;

import java.util.logging.SocketHandler;

/**
 * Light source of sun, moon, etc. Emits rays from up to down, first non-space tile becomes illuminated.
 * Should be used only for bodies that emit noticeable amount of light.
 * Should be updated by {@link CelestialCycleAspect}
 *
 * @author Alexander Kuzyakov
 */
public class SelestialLightSource extends AbstractLighSourceAspect {

    public SelestialLightSource(AspectHolder aspectHolder) {
        super(aspectHolder);
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
        LocalMap localMap = gameContainer.getLocalMap();
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
