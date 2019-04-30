package stonering.generators.localgen.generators.flora;

import stonering.enums.generation.PlantPlacingTags;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenConfig;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
    protected LocalGenConfig config;
    protected LocalMap localMap;
    protected PerlinNoiseGenerator noiseGenerator;
    protected float maxTemp;
    protected float minTemp;
    protected float midTemp;
    protected float rainfall;
    protected int areaSize;

    private PlantPlacingTags[] waterTags = {WATER_FAR, WATER_NEAR, WATER_UNDER};
    private PlantPlacingTags[] lighttags = {LIGHT_HIGH, LIGHT_LOW, LIGHT_UNDERGROUND};
    private PlantPlacingTags[] soilTags = {SOIL_SOIL, SOIL_STONE, SOIL_WOOD};

    protected Set<PlantType> commonPlantSet;
    protected Map<String, Float> weightedPlantTypes;
    private Set<Byte> substrateBlockTypes;
    protected Set<Position> allPositions;
    protected List<Position> positions;

    private Position cachePosition;

    public LocalFloraGenerator(LocalGenContainer container) {
        super(container);
    }

    public void execute() {
        TagLoggersEnum.GENERATION.log("generating flora");
        extractContainer();
        weightedPlantTypes = new HashMap<>();
        cachePosition = new Position(0, 0, 0);
        substrateBlockTypes = new HashSet<>(Arrays.asList(FLOOR.CODE, RAMP.CODE));
        commonPlantSet = new HashSet<>();
        allPositions = new HashSet<>();
        positions = new ArrayList<>();

        countTemperature();
        filterPlantsByType();
        filterPlantsByBounds(); // get all plants that can grow here
        gatherPositions();
        for (PlantPlacingTags waterTag : waterTags) {
            for (PlantPlacingTags lightTag : lighttags) {
                for (PlantPlacingTags soilTag : soilTags) {
                    filterPlantsByTags(waterTag, lightTag, soilTag); // get all plants with two tags simultaneously
                    if(commonPlantSet.isEmpty()) continue;
                    normalizeWeights(weightedPlantTypes); // make total weight equal 1
                    gatherTagPositions(waterTag, lightTag, soilTag);
                    if(positions.isEmpty()) continue;
                    modifyCounts();
                    weightedPlantTypes.forEach(this::placePlants);
                }
            }
        }
    }

    /**
     * Collects all tiles that can have plants (floors).
     */
    private void gatherPositions() {
        Position cachePosition = new Position();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if(container.plantBlocks.containsKey(cachePosition.set(x,y,z))) continue;
                    if (localMap.getBlockType(x, y, z) != FLOOR.CODE) continue;
                    allPositions.add(new Position(x, y, z));
                }
            }
        }
    }

    /**
     * Collects all positions validates by both tags.
     */
    private void gatherTagPositions(PlantPlacingTags waterTag, PlantPlacingTags lightTag, PlantPlacingTags soilTag) {
        positions.clear();
        positions.addAll(allPositions.stream().filter(position -> waterTag.VALIDATOR.validate(localMap, position)
                && lightTag.VALIDATOR.validate(localMap, position)
                && soilTag.VALIDATOR.validate(localMap, position)).collect(Collectors.toList()));
    }

    /**
     * Filters out plants which cannot grow in given conditions.
     */
    private void filterPlantsByTags(PlantPlacingTags waterTag, PlantPlacingTags lightTag, PlantPlacingTags soilTag) {
        commonPlantSet.stream().filter(type -> type.placingTagsSet.contains(waterTag)
                && (type.placingTagsSet.contains(lightTag))
                && type.placingTagsSet.contains(soilTag))
                .forEach(type -> weightedPlantTypes.put(type.name, getSpreadModifier(type)));
    }

    /**
     * Should put plants of desired type into commonPlantSet.
     */
    protected abstract void filterPlantsByType();

    /**
     * Changes weights to number of plants (multiplies to available tiles count).
     */
    protected void modifyCounts() {
        weightedPlantTypes.keySet().forEach(s -> weightedPlantTypes.put(s, weightedPlantTypes.get(s) * positions.size()));
    }

    /**
     * Should place plants on map in number from weightedPlantTypes.
     */
    protected abstract void placePlants(String specimen, float amount);

    private void extractContainer() {
        this.config = container.config;
        this.localMap = container.localMap;
        int x = config.getLocation().getX();
        int y = config.getLocation().getY();
        areaSize = config.getAreaSize();
        rainfall = container.world.getWorldMap().getRainfall(x, y);
        noiseGenerator = new PerlinNoiseGenerator();
    }

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
                    if (container.plantBlocks.containsKey(cachePosition.set(mapX, mapY, mapZ))) return false;
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
                    Position onMapPosition = new Position(
                            cx + x - treeRadius,
                            cy + y - treeRadius,
                            cz + z - treeCenterZ);
                    List<PlantBlock> blocks = container.plantBlocks.getOrDefault(onMapPosition, Collections.emptyList());
                    blocks.add(treeParts[x][y][z]);
                    container.plantBlocks.put(onMapPosition, blocks);
                    treeParts[x][y][z].setPosition(onMapPosition);
                }
            }
        }
        container.plants.add(tree);
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
                container.plants.add(plant);
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
    private void filterPlantsByBounds() {
        commonPlantSet.stream().filter(type -> rainfall > type.rainfallBounds[0]
                && rainfall > type.rainfallBounds[1]
                && minTemp > type.temperatureBounds[0]
                && maxTemp < type.temperatureBounds[1])
                .forEach(type -> commonPlantSet.add(type));
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
        return Math.abs(((type.temperatureBounds[2] + type.temperatureBounds[3]) / 2f) - midTemp) / (maxTemp - midTemp);
    }

    /**
     * Gets soil placing tag from {@link PlantMap} and truncates it to material tag.
     */
    private String getBlockMateriaTag(PlantType type) {
        return PlantMap.getInstance().resolveSoilType(type).substring(5); // skips tag prefix.
    }
}
