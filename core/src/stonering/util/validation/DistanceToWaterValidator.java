package stonering.util.validation;

import stonering.enums.generation.PlantPlacingTags;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Checks that distance to water is within bounds.
 */
public class DistanceToWaterValidator extends PositionValidator {
    public static final int XY_DISTANCE = 5;
    public static final int Z_DISTANCE = 1;
    private PlantPlacingTags tag;

    @Override
    public boolean validate(LocalMap map, Position position) {
        if(!map.inMap(position) || tag == null) return false;
        switch (tag) {
            case WATER_NEAR: {
                return hasWaterInDistance(map, position);
            }
            case WATER_FAR: {
                return !hasWaterInDistance(map, position);
            }
            case WATER_UNDER: {
                return map.getFlooding(position) == 7;
            }
            // TODO case WATER_ON: {}
        }
        return false;
    }

    private boolean hasWaterInDistance(LocalMap map, Position pos) {
        for (int x = pos.x - XY_DISTANCE; x < pos.x + XY_DISTANCE; x++) {
            for (int y = pos.y - XY_DISTANCE; y < pos.y + XY_DISTANCE; y++) {
                for (int z = pos.z - Z_DISTANCE; z < pos.z + Z_DISTANCE; z++) {
                    if(map.inMap(x,y,z) && map.getFlooding(x,y,z) > 0) return true;
                }
            }
        }
        return false;
    }

    public void setTag(PlantPlacingTags tag) {
        this.tag = tag;
    }
}
