package stonering.util.validation;

import stonering.enums.generation.PlacingTagEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.liquid.LiquidContainer;
import stonering.util.geometry.Position;

/**
 * Checks that distance to water is within bounds.
 */
public class DistanceToWaterValidator implements PositionValidator {
    public static final int XY_DISTANCE = 5;
    public static final int Z_DISTANCE = 1;
    private PlacingTagEnum tag;

    @Override
    public Boolean apply(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        LiquidContainer container = GameMvc.model().get(LiquidContainer.class);
        if(!map.inMap(position) || tag == null) return false;
        switch (tag) {
            case WATER_NEAR: {
                return hasWaterInDistance(container, position);
            }
            case WATER_FAR: {
                return !hasWaterInDistance(container, position);
            }
            case WATER_UNDER: {
                return container.getAmount(position) == 7;
            }
            // TODO case WATER_ON: {}
        }
        return false;
    }

    private boolean hasWaterInDistance(LiquidContainer container, Position pos) {
        for (int x = pos.x - XY_DISTANCE; x < pos.x + XY_DISTANCE; x++) {
            for (int y = pos.y - XY_DISTANCE; y < pos.y + XY_DISTANCE; y++) {
                for (int z = pos.z - Z_DISTANCE; z < pos.z + Z_DISTANCE; z++) {
                    if(container.getAmount(x,y,z) > 0) return true;
                }
            }
        }
        return false;
    }

    public void setTag(PlacingTagEnum tag) {
        this.tag = tag;
    }
}
