package stonering.objects.local_actors.environment.aspects;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;

/**
 * @author Alexander on 07.10.2018.
 */
public class LighSourceAspect extends Aspect {
    private float radius;
    private float force;          // [0,1]
    private boolean global;

    public LighSourceAspect(AspectHolder aspectHolder, boolean global) {
        super("lightSource", aspectHolder);
        this.global = global;
    }

    public void ApplyLigt() {
        if (global) {
            addLightToMap();
        } else {
            addLightInArea();
        }
    }

    private void addLightToMap() {
        LocalMap localMap = gameContainer.getLocalMap();
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = localMap.getzSize() - 1; z >= 0; z--) {
                    localMap.setLightLevel(x, y, z, (byte) (localMap.getLightLevel(x, y, z) + force * Byte.MAX_VALUE));
                    if (localMap.getBlockType(x, y, z) != BlockTypesEnum.SPACE.getCode()) {
                        break;
                    }
                }
            }
        }
    }

    private void addLightInArea() {
        //TODO
    }
}
