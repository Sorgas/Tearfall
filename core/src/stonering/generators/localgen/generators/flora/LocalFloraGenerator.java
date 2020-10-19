package stonering.generators.localgen.generators.flora;

import stonering.entity.world.World;
import stonering.enums.generation.PlacingTagEnum;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.localgen.generators.LocalGenerator;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypeEnum.*;
import static stonering.enums.generation.PlacingTagEnum.*;

/**
 * Parent class for local generators of different plant types.
 * Generates plants suitable for local climate and places them on local map.
 * Generation steps:
 * 1. filter all plants from {@link PlantTypeMap}
 *
 * @author Alexander Kuzyakov on 10.04.2018.
 */
public abstract class LocalFloraGenerator extends LocalGenerator {
    protected LocalMap localMap;
    protected PlantContainer plantContainer;
    protected PlantGenerator generator;
    private float maxTemp;
    private float minTemp;
    private float midTemp;
    private float rainfall;

    private PlacingTagEnum[] waterTags = {WATER_FAR, WATER_NEAR, WATER_UNDER};
    private PlacingTagEnum[] lighttags = {LIGHT_HIGH, LIGHT_LOW, LIGHT_UNDERGROUND};
    private PlacingTagEnum[] soilTags = {SOIL_SOIL, SOIL_STONE, SOIL_WOOD};

    protected List<Position> positions;
    protected Position cachePosition = new Position(0, 0, 0);

    public LocalFloraGenerator(LocalGenContainer container) {
        super(container);
        generator = new PlantGenerator();
    }

    public void execute() {
        extractContainer();
        countTemperature();
        Set<PlantType> commonPlantSet = filterPlantsByBounds(filterPlantsByType()); // get all plants that can grow here
        Set<Position> allPositions = gatherPositions();
        for (PlacingTagEnum waterTag : waterTags) {
            for (PlacingTagEnum lightTag : lighttags) {
                for (PlacingTagEnum soilTag : soilTags) {
                    List<PlacingTagEnum> tags = Arrays.asList(waterTag, lightTag, soilTag);
                    Map<String, Float> filteredPlants = filterPlantsByTags(tags, commonPlantSet); // get all plants with two tags simultaneously
                    if (commonPlantSet.isEmpty()) continue;
                    Logger.GENERATION.logDebug("placing " + waterTag + " " + lightTag + " " + soilTag);
                    Logger.GENERATION.logDebug("filtered plants: " + commonPlantSet.size());
                    normalizeWeights(filteredPlants); // make total weight equal 1
                    positions = gatherTagPositions(tags, allPositions);
                    if (positions.isEmpty()) continue;
                    Logger.GENERATION.logDebug("appropriate positions: " + positions.size());
                    modifyCounts(filteredPlants, positions);
                    filteredPlants.forEach(this::placePlants);
                }
            }
        }
    }

    protected void extractContainer() {
        plantContainer = container.model.get(PlantContainer.class);
        localMap = container.model.get(LocalMap.class);
        int x = config.getLocation().x;
        int y = config.getLocation().y;
        rainfall = container.model.get(World.class).worldMap.getRainfall(x, y);
    }

    /**
     * Counts temperature bounds for local area.
     */
    private void countTemperature() {
        minTemp = container.monthlyTemperatures[0];
        maxTemp = minTemp;
        midTemp = 0;
        for (float temp : container.monthlyTemperatures) {
            minTemp = Math.min(temp, minTemp);
            maxTemp = Math.max(temp, maxTemp);
            midTemp += temp;
        }
        midTemp /= container.monthlyTemperatures.length;
    }

    /**
     * Filters all PlantTypeMap with local climate parameters and adds passed plants and trees to maps.
     */
    //TODO add grade of specimen spreading in this area.
    private Set<PlantType> filterPlantsByBounds(Set<PlantType> types) {
        return types.stream().filter(type -> rainfall > type.rainfallBounds[0]
                && rainfall < type.rainfallBounds[1]
                && minTemp > type.temperatureBounds[0]
                && maxTemp < type.temperatureBounds[1]).collect(Collectors.toSet());
    }

    /**
     * Filters out plants which cannot grow in given conditions.
     */
    private Map<String, Float> filterPlantsByTags(List<PlacingTagEnum> tags, Set<PlantType> types) {
        Map<String, Float> filteredPlants = new HashMap<>();
        types.stream().filter(type -> type.placingTags.containsAll(tags))
                .forEach(type -> filteredPlants.put(type.name, getSpreadModifier(type)));
        return filteredPlants;
    }

    /**
     * Translates all weights so their sum is not greater than 1.
     * Thus, area can be filled with big number of low-adapted plants, but cannot be fully filled with single low-adapted plant.
     * Highly-adapted plants will crowd out others proportionally. Shares of plants with same adaptation level will be equal.
     */
    private void normalizeWeights(Map<String, Float> plantWeights) {
        float total = 0;
        for (Float aFloat : plantWeights.values()) total += aFloat;
        if (total < 1f) return;
        for (String specimen : plantWeights.keySet())
            plantWeights.put(specimen, plantWeights.get(specimen) / total);
    }

    /**
     * Collects all tiles that can have plants (floors).
     */
    protected Set<Position> gatherPositions() {
        Set<Position> positions = new HashSet<>();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (plantContainer.isPlantBlockExists(cachePosition.set(x, y, z))) continue;
                    if (localMap.blockType.get(x, y, z) != FLOOR.CODE) continue;
                    positions.add(new Position(x, y, z));
                }
            }
        }
        return positions;
    }

    /**
     * Collects all positions validates by both tags.
     */
    private List<Position> gatherTagPositions(List<PlacingTagEnum> tags, Collection<Position> positions) {
        return positions.stream()
                .filter(position -> tags.stream().allMatch(tag -> tag.VALIDATOR.apply(position)))
                .collect(Collectors.toList());
    }

    /**
     * Changes weights to number of plants (multiplies to available tiles count).
     *
     * @param filteredPlants
     * @param filteredPositions
     */
    private void modifyCounts(Map<String, Float> filteredPlants, Collection<Position> filteredPositions) {
        int size = filteredPositions.size();
        filteredPlants.keySet().forEach(s -> filteredPlants.put(s, filteredPlants.get(s) * size));
    }

    /**
     * Should put plants of desired type into commonPlantSet.
     */
    protected abstract Set<PlantType> filterPlantsByType();

    /**
     * Should place plants on map in number from weightedPlantTypes.
     */
    protected abstract void placePlants(String specimen, float amount);

    /**
     * Specimen will be spreaded more widely, if its grom range is closer to year middle temperature.
     */
    private float getSpreadModifier(PlantType type) {
        return Math.abs(((type.temperatureBounds[0] + type.temperatureBounds[1]) / 2f) - midTemp) / (maxTemp - midTemp);
    }
}
