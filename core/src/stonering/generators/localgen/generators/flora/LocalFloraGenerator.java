package stonering.generators.localgen.generators.flora;

import stonering.entity.world.World;
import stonering.enums.generation.PlantPlacingTags;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.localgen.generators.LocalAbstractGenerator;
import stonering.generators.plants.PlantGenerator;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;
import stonering.util.global.Pair;
import stonering.util.global.TagLoggersEnum;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static stonering.enums.blocks.BlockTypesEnum.*;
import static stonering.enums.generation.PlantPlacingTags.*;

/**
 * Parent class for local generators of different plant types.
 * Generates plants suitable for local climate and places them on local map.
 * Generation steps:
 * 1. filter all plants from {@link PlantMap}
 *
 * @author Alexander Kuzyakov on 10.04.2018.
 */
public abstract class LocalFloraGenerator extends LocalAbstractGenerator {
    protected LocalMap localMap;
    private PlantContainer plantContainer;
    private PerlinNoiseGenerator noiseGenerator;
    private float maxTemp;
    private float minTemp;
    private float midTemp;
    private float rainfall;
    private int areaSize;

    private PlantPlacingTags[] waterTags = {WATER_FAR, WATER_NEAR, WATER_UNDER};
    private PlantPlacingTags[] lighttags = {LIGHT_HIGH, LIGHT_LOW, LIGHT_UNDERGROUND};
    private PlantPlacingTags[] soilTags = {SOIL_SOIL, SOIL_STONE, SOIL_WOOD};

    private Set<Byte> substrateBlockTypes;

    protected List<Position> positions;
    private Position cachePosition;

    public LocalFloraGenerator(LocalGenContainer container) {
        super(container);
        cachePosition = new Position(0, 0, 0);
        substrateBlockTypes = new HashSet<>(Arrays.asList(FLOOR.CODE, RAMP.CODE));
    }

    public void execute() {
        extractContainer();
        countTemperature();
        Set<PlantType> commonPlantSet = filterPlantsByBounds(filterPlantsByType()); // get all plants that can grow here
        Set<Position> allPositions = gatherPositions();
        for (PlantPlacingTags waterTag : waterTags) {
            for (PlantPlacingTags lightTag : lighttags) {
                for (PlantPlacingTags soilTag : soilTags) {
                    List<PlantPlacingTags> tags = Arrays.asList(waterTag, lightTag, soilTag);
                    Map<String, Float> filteredPlants = filterPlantsByTags(tags, commonPlantSet); // get all plants with two tags simultaneously
                    if (commonPlantSet.isEmpty()) continue;
                    TagLoggersEnum.GENERATION.logDebug("placing " + waterTag + " " + lightTag + " " + soilTag);
                    TagLoggersEnum.GENERATION.logDebug("filtered plants: " + commonPlantSet.size());
                    normalizeWeights(filteredPlants); // make total weight equal 1
                    positions = gatherTagPositions(tags, allPositions);
                    if (positions.isEmpty()) continue;
                    TagLoggersEnum.GENERATION.logDebug("appropriate positions: " + positions.size());
                    modifyCounts(filteredPlants, positions);
                    filteredPlants.forEach(this::placePlants);
                }
            }
        }
    }

    private void extractContainer() {
        plantContainer = container.model.get(PlantContainer.class);
        localMap = container.model.get(LocalMap.class);
        areaSize = config.getAreaSize();
        noiseGenerator = new PerlinNoiseGenerator();
        int x = config.getLocation().getX();
        int y = config.getLocation().getY();
        rainfall = container.model.get(World.class).getWorldMap().getRainfall(x, y);
    }

    /**
     * Collects all tiles that can have plants (floors).
     */
    private Set<Position> gatherPositions() {
        Set<Position> positions = new HashSet<>();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (plantContainer.getPlantBlocks().containsKey(cachePosition.set(x, y, z))) continue;
                    if (localMap.getBlockType(x, y, z) != FLOOR.CODE) continue;
                    positions.add(new Position(x, y, z));
                }
            }
        }
        return positions;
    }

    /**
     * Collects all positions validates by both tags.
     */
    private List<Position> gatherTagPositions(List<PlantPlacingTags> tags, Collection<Position> positions) {
        return positions.stream()
                .filter(position -> tags.stream().allMatch(tag -> tag.VALIDATOR.validate(localMap, position)))
                .collect(Collectors.toList());
    }

    /**
     * Filters out plants which cannot grow in given conditions.
     */
    private Map<String, Float> filterPlantsByTags(List<PlantPlacingTags> tags, Set<PlantType> types) {
        Map<String, Float> filteredPlants = new HashMap<>();
        types.stream().filter(type -> type.placingTags.containsAll(tags))
                .forEach(type -> filteredPlants.put(type.name, getSpreadModifier(type)));
        return filteredPlants;
    }

    /**
     * Changes weights to number of plants (multiplies to available tiles count).
     * @param filteredPlants
     * @param filteredPositions
     */
    protected void modifyCounts(Map<String, Float> filteredPlants, Collection<Position> filteredPositions) {
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
     * Counts temperature bounds for local area.
     */
    private void countTemperature() {
        minTemp = container.monthlyTemperatures[0];
        maxTemp = minTemp;
        midTemp = 0;
        for (float temp : container.monthlyTemperatures) {
            minTemp = temp < minTemp ? temp : minTemp;
            maxTemp = temp > maxTemp ? temp : maxTemp;
            midTemp += temp;
        }
        midTemp /= container.monthlyTemperatures.length;
    }

    /**
     * Calls placing method for all filtered plants.
     * Trees give shadow, therefore they should be placed before plants and substrates.
     * Trees placing:
     * 1. all floor tiles are collected.
     * 2. tiles are filtered by tree's requirements.
     * 3. required amount of trees is placed.
     */

    /**
     * Generates and places trees on local map. Uses limited attempts with random positions.
     * //TODO add underground trees.
     * //TODO change placing logic to fores areas.
     */
    private void placeInitialTrees(String specimen, float amount) {
        TreeGenerator treeGenerator = new TreeGenerator();
        Random random = new Random();
        int maxAge = PlantMap.getInstance().getPlantType(specimen).getMaxAge();
        Tree tree = treeGenerator.generateTree(specimen, random.nextInt(maxAge));
        for (int tries = 500; amount > 0 && tries > 0; tries--) {
            int x = random.nextInt(areaSize);
            int y = random.nextInt(areaSize);
            int z = container.roundedHeightsMap[x][y] + 1;
            if (!checkTreePlacing(tree, x, y, z)) continue;
            placeTree(tree, x, y, z);
            tree.setPosition(new Position(x, y, z));
            tree = treeGenerator.generateTree(specimen, random.nextInt(maxAge));
            amount--;
        }
    }

    /**
     * Checks that desired area for tree is free.
     */
    private boolean checkTreePlacing(Tree tree, int cx, int cy, int cz) {
        PlantBlock[][][] treeParts = tree.getBlocks();
        int treeCenterZ = tree.getCurrentStage().treeForm.get(2);
        int treeRadius = tree.getCurrentStage().treeForm.get(0);
        String soilType = getBlockMateriaTag(tree.getType());
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    int mapX = cx + x - treeRadius;
                    int mapY = cy + y - treeRadius;
                    int mapZ = cz + z - treeCenterZ;
                    if (treeParts[x][y][z] != null) return false;
                    if (!localMap.inMap(mapX, mapY, mapZ)) return false;
                    if (plantContainer.getPlantBlocks().containsKey(cachePosition.set(mapX, mapY, mapZ))) return false;
                    Material material = MaterialMap.getInstance().getMaterial(localMap.getMaterial(x, y, z));
                    if (material != null && material.getTags().contains(soilType)) ;
                }
            }
        }
        return true;
    }

    /**
     * Places tree on map. Area on map should be checked before placing.
     */
    private void placeTree(Tree tree, int cx, int cy, int cz) {
        PlantBlock[][][] treeParts = tree.getBlocks();
        int treeCenterZ = tree.getCurrentStage().treeForm.get(2);
        int treeRadius = tree.getCurrentStage().treeForm.get(0);
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    if (treeParts[x][y][z] == null) continue;
                    cachePosition.set(
                            cx + x - treeRadius,
                            cy + y - treeRadius,
                            cz + z - treeCenterZ);
                    if (plantContainer.getPlantBlocks().containsKey(cachePosition)) continue;
                    plantContainer.getPlantBlocks().put(cachePosition, treeParts[x][y][z]);
                    treeParts[x][y][z].setPosition(cachePosition);
                }
            }
        }
        plantContainer.place(tree);
    }


    /**
     * Places substrate plants into separate list in {@link LocalGenContainer}.
     */
    private void placeSubstrates(String specimen, float relativeAmount) {
        PlantGenerator plantGenerator = new PlantGenerator();
        Pair<boolean[][][], ArrayList<Position>> pair = findAllAvailableSubstratePositions(specimen);
        ArrayList<Position> positions = pair.getValue();
        boolean[][][] array = pair.getKey();
        Random random = new Random();
        for (int number = (int) (positions.size() * relativeAmount / 2); number > 0; number--) {
            try {
                Position position = positions.remove(random.nextInt(positions.size()));
                array[position.x][position.y][position.z] = false;
                Plant plant = plantGenerator.generatePlant(specimen, 0);
                plant.setPosition(position);
                plantContainer.place(plant);
            } catch (DescriptionNotFoundException e) {
                System.out.println("material for plant " + specimen + " not found");
            }
        }
    }


    /**
     * Collects all positions suitable for specific plant. Used only for single tile plants.
     */
    private Pair<boolean[][][], ArrayList<Position>> findAllAvailableSubstratePositions(String specimen) {
        //TODO should count plant requirements for light level, water source, soil type
        ArrayList<Position> positions = new ArrayList<>();
        boolean[][][] array = new boolean[localMap.xSize][localMap.ySize][localMap.zSize];
        PlantType type = PlantMap.getInstance().getPlantType(specimen);
        String soilType = getBlockMateriaTag(type);
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (!substrateBlockTypes.contains(localMap.getBlockType(x, y, z))) continue;
                    Material material = MaterialMap.getInstance().getMaterial(localMap.getMaterial(x, y, z));
                    if (material == null)
                        TagLoggersEnum.GENERATION.logError("material in tile " + x + " " + y + " " + z + " is null.");
                    if (!material.getTags().contains(soilType)) continue;
                    positions.add(new Position(x, y, z));
                    array[x][y][z] = true;
                }
            }
        }
        return new Pair<>(array, positions);
    }

    /**
     * Filters all PlantMap with local climate parameters and adds passed plants and trees to maps.
     */
    //TODO add grade of specimen spreading in this area.
    private Set<PlantType> filterPlantsByBounds(Set<PlantType> types) {
        return types.stream().filter(type -> rainfall > type.rainfallBounds[0]
                && rainfall < type.rainfallBounds[1]
                && minTemp > type.temperatureBounds[0]
                && maxTemp < type.temperatureBounds[1]).collect(Collectors.toSet());
    }

    /**
     * Translates all weights so their sum is not greater than 1.
     * Thus, area can be filled with big number of low-adapted plants, but cannot be fully filled with single low-adapted plant.
     * Highly-adapted plants will crowd out others proportionally. Shares of plants with same adaptation level will be equal.
     */
    protected void normalizeWeights(Map<String, Float> plantWeights) {
        float total = 0;
        for (Float aFloat : plantWeights.values()) total += aFloat;
        if (total < 1f) return;
        for (String specimen : plantWeights.keySet())
            plantWeights.put(specimen, plantWeights.get(specimen) / total);
    }

    /**
     * Specimen will be spreaded more widely, if its grom range is closer to year middle temperature.
     */
    protected float getSpreadModifier(PlantType type) {
        return Math.abs(((type.temperatureBounds[0] + type.temperatureBounds[1]) / 2f) - midTemp) / (maxTemp - midTemp);
    }

    /**
     * Gets soil placing tag from {@link PlantMap} and truncates it to material tag.
     */
    private String getBlockMateriaTag(PlantType type) {
        Set<PlantPlacingTags> soilTags = new HashSet<>(type.placingTags);
        soilTags.retainAll(SOIL_GROUP);
        return soilTags.iterator().next().VALUE.substring(5); // skips tag prefix.
    }
}
