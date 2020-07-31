package stonering.enums.generation;

import stonering.util.validation.*;

import java.util.*;

/**
 * Enumeration of tags for placing plants ans buildings.
 * Each tag has {@link PositionValidator} for selecting appropriate tile.
 * <p>
 * Used in plants.json, trees.json, substrates.json.
 */
public enum PlacingTagEnum {
    WATER_NEAR("water_near", new DistanceToWaterValidator()),
    WATER_FAR("water_far", new DistanceToWaterValidator()),
    //    WATER_ON("water_on", new DistanceToWaterValidator()),              //TODO blocked by stable water.
    WATER_UNDER("water_under", new DistanceToWaterValidator()),
    LIGHT_UNDERGROUND("light_underground", position -> true),   //TODO implement vaidators
    LIGHT_LOW("light_low ", position -> true),                  //TODO implement vaidators
    LIGHT_HIGH("light_high", position -> true),                 //TODO implement vaidators
    LIGHT_OPEN("light_open", position -> true),                 // not used outside json ad this enum. //TODO implement vaidators
    SOIL_SOIL("soil_soil", new TileMaterialValidator("soil")),                   //TODO implement vaidators
    SOIL_STONE("soil_stone", new TileMaterialValidator("stone")),                 //TODO implement vaidators
    SOIL_WOOD("soil_wood", new TileMaterialValidator("wood")),                   //TODO implement vaidators
    FREE_FLOOR("floor", new FreeFloorValidator()),
    CONSTRUCTION("construction", new ConstructionValidator());
    
    private static Map<String, PlacingTagEnum> tagMap;
    public static final List<PlacingTagEnum> WATER_GROUP = Arrays.asList(WATER_FAR, WATER_NEAR, WATER_UNDER);
    public static final List<PlacingTagEnum> LIGHT_GROUP = Arrays.asList(LIGHT_LOW, LIGHT_HIGH, LIGHT_UNDERGROUND, LIGHT_OPEN);
    public static final List<PlacingTagEnum> SOIL_GROUP = Arrays.asList(SOIL_SOIL, SOIL_STONE, SOIL_WOOD);

    public final String VALUE;
    public final PositionValidator VALIDATOR;

    static {
        tagMap = new HashMap<>();
        for (PlacingTagEnum tag : values()) {
            tagMap.put(tag.VALUE, tag);
        }
    }

    PlacingTagEnum(String value, PositionValidator validator) {
        this.VALUE = value;
        this.VALIDATOR = validator;
        if (validator instanceof DistanceToWaterValidator) {
            ((DistanceToWaterValidator) validator).setTag(this);
        }
    }

    public static PlacingTagEnum get(String tag) {
        return tagMap.get(tag);
    }
}
