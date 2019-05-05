package stonering.enums.generation;

import stonering.util.validation.*;

import java.util.*;

/**
 * Enumeration of tags for plants placing.
 * Each tag have {@link PositionValidator} for selecting appropriate tile.
 *
 * Used in plants.json, trees.json, substrates.json.
 */
public enum PlantPlacingTags {
    WATER_NEAR("water_near", new DistanceToWaterValidator()),
    WATER_FAR("water_far", new DistanceToWaterValidator()),
//    WATER_ON("water_on", new DistanceToWaterValidator()),              //TODO blocked by stable water.
    WATER_UNDER("water_under", new DistanceToWaterValidator()),
    LIGHT_UNDERGROUND("light_underground", new EmptyValidator()),   //TODO implement vaidators
    LIGHT_LOW("light_low ", new EmptyValidator()),                  //TODO implement vaidators
    LIGHT_HIGH("light_high", new EmptyValidator()),                 //TODO implement vaidators
    LIGHT_OPEN("light_open", new EmptyValidator()),                 // not used outside json ad this enum. //TODO implement vaidators
    SOIL_SOIL("soil_soil", new FloorMaterialValidator("soil")),                   //TODO implement vaidators
    SOIL_STONE("soil_stone", new FloorMaterialValidator("stone")),                 //TODO implement vaidators
    SOIL_WOOD("soil_wood", new FloorMaterialValidator("wood"));                   //TODO implement vaidators

    private static Map<String, PlantPlacingTags> tagMap;
    public static final List<PlantPlacingTags> WATER_GROUP = Arrays.asList(WATER_FAR, WATER_NEAR, WATER_UNDER);
    public static final List<PlantPlacingTags> LIGHT_GROUP = Arrays.asList(LIGHT_LOW, LIGHT_HIGH, LIGHT_UNDERGROUND, LIGHT_OPEN);
    public static final List<PlantPlacingTags> SOIL_GROUP = Arrays.asList(SOIL_SOIL, SOIL_STONE, SOIL_WOOD);

    public final String VALUE;
    public final PositionValidator VALIDATOR;

    static {
        tagMap = new HashMap<>();
        for (PlantPlacingTags tag : values()) {
            tagMap.put(tag.VALUE, tag);
        }
    }

    PlantPlacingTags(String value, PositionValidator validator) {
        this.VALUE = value;
        this.VALIDATOR = validator;
        if(validator instanceof DistanceToWaterValidator) {
            ((DistanceToWaterValidator) validator).setTag(this);
        }
    }

    public static Collection<PlantPlacingTags> getTag(String tag) {
        if("light_open".equals(tag)) return new ArrayList<>(Arrays.asList(LIGHT_LOW, LIGHT_HIGH));
        return Arrays.asList(tagMap.get(tag));
    }
}
