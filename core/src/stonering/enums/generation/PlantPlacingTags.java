package stonering.enums.generation;

import stonering.util.validation.PositionValidator;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.Map;

/**
 * Enumeration of tags for plants placing.
 * <p>
 * Used in plants.json, trees.json, substrates.json.
 */
public enum PlantPlacingTags {
    WATER_NEAR("water_near", ),
    WATER_FAR("water_far"),
    WATER_ON("water_on"),
    WATER_UNDER("water_under"),
    LIGHT_UNDERGROUND("light_underground"),
    LIGHT_LOW("light_low "),
    LIGHT_HIGH("light_high"),
    LIGHT_OPEN("light_open"),
    SOIL_SOIL("soil_soil"),
    SOIL_STONE("soil_stone"),
    SOIL_WOOD("soil_wood");

    public final String value;
    private Map<String, PlantPlacingTags> tagMap;

    PlantPlacingTags(String value) {
        this.value = value;
    }

    public PlantPlacingTags getTag(String tag) {
        return tagMap.get(tag);
    }

    public class DistanceToWaterValidator extends PositionValidator {

    }
}
