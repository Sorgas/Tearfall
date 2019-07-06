package stonering.entity.environment.aspects;

import stonering.entity.Entity;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;


/**
 * Light source of sun, moon, etc. Emits rays from up to down, first non-space tile becomes illuminated.
 * Should be used only for bodies that emit noticeable amount of light.
 * Should be updated by {@link CelestialCycleAspect}
 *
 * @author Alexander Kuzyakov
 */
public class CelestialLightSourceAspect extends AbstractLighSourceAspect {
    public static String NAME = "celestial_light_source";

    public CelestialLightSourceAspect(Entity entity) {
        super(entity);
    }

    @Override
    public void init() {
        super.init();
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
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = localMap.zSize - 1; z >= 0; z--) {
                    localMap.generalLight.changeValue(x, y, z, forceDelta);
                    if (localMap.getBlockType(x, y, z) != BlockTypesEnum.SPACE.CODE) {
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
