package stonering.generators.localgen.generators;

import javafx.util.Pair;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.generators.plants.TreesGenerator;
import stonering.global.utils.Position;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Alexander on 10.04.2018.
 * <p>
 * Generates plants suitable for local climate and places them on local map.
 */
public class LocalFloraGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private LocalMap localMap;
    private PerlinNoiseGenerator noiseGenerator;
    private float maxTemp;
    private float minTemp;
    private float rainfall;
    private int areaSize;


    private final int floorCode = BlockTypesEnum.FLOOR.getCode();

    private HashMap<String, Float> weightedPlantTypes;
    private HashMap<String, Float> weightedTreeTypes;

    public LocalFloraGenerator(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        int x = config.getLocation().getX();
        int y = config.getLocation().getY();
        areaSize = config.getAreaSize();
        rainfall = container.getWorldMap().getRainfall(x, y);
        noiseGenerator = new PerlinNoiseGenerator();
    }

    public void execute() {
        System.out.println("generating flora");
        this.localMap = container.getLocalMap();
        weightedPlantTypes = new HashMap<>();
        weightedTreeTypes = new HashMap<>();
        countTemperature();
        filterPlants();
        generateFlora();
    }

    /**
     * Counts min and max temperature of the year.
     */
    private void countTemperature() {
        minTemp = container.getMonthlyTemperatures()[0];
        maxTemp = minTemp;
        for (float temp : container.getMonthlyTemperatures()) {
            minTemp = temp < minTemp ? temp : minTemp;
            maxTemp = temp > maxTemp ? temp : maxTemp;
        }
    }

    /**
     * Calls placing method for all filtered plants and trees.
     * Trees give shadow, therefore they should be placed before plants.
     */
    private void generateFlora() {
        weightedTreeTypes.forEach((specimen, amount) -> placeTrees(specimen, amount));
        weightedPlantTypes.forEach((specimen, amount) -> placePlants(specimen, amount));
    }

    /**
     * Generates and places trees on local map.
     *
     * @param specimen PlantType key from PlantMap representing tree
     * @param amount   relative amount
     */
    private void placeTrees(String specimen, float amount) {
        try {
            float[][] forestArea = noiseGenerator.generateOctavedSimplexNoise(areaSize, areaSize, 6, 0.5f, 0.015f);
            int tries = 200;
            Random random = new Random();
            TreesGenerator treesGenerator = new TreesGenerator();
            Tree tree = treesGenerator.generateTree(specimen, 1);
            while (amount > 0 && tries > 0) {
                int x = random.nextInt(areaSize);
                int y = random.nextInt(areaSize);
                int z = container.getHeightsMap()[x][y] + 1;
                if (forestArea[x][y] > 0 && checkTreePlacing(tree, x, y, z)) {
                    placeTree(tree, x, y, z);
                    tree = treesGenerator.generateTree(specimen, 1);
                    amount--;
                }
                tries--;
            }
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Places tree on map
     *
     * @param tree tree to place
     */
    private void placeTree(Tree tree, int cx, int cy, int cz) {
        Plant[][][] treeParts = tree.getBlocks();
        int treeCenterZ = tree.getType().getRootDepth();
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    int mapX = cx + x;
                    int mapY = cy + y;
                    int mapZ = cz + z - treeCenterZ;
                    if (treeParts[x][y][z] != null) {
                        localMap.setPlantBlock(mapX, mapY, mapZ, treeParts[x][y][z].getBlock());
                    }
                }
            }
        }
    }

    /**
     * Checks that desired area for tree is free.
     *
     * @return true if placing possible.
     */
    private boolean checkTreePlacing(Tree tree, int cx, int cy, int cz) {
        Plant[][][] treeParts = tree.getBlocks();
        int treeCenterZ = tree.getType().getRootDepth();
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    int mapX = cx + x;
                    int mapY = cy + y;
                    int mapZ = cz + z - treeCenterZ;
                    if (!localMap.inMap(mapX, mapY, mapZ)
                            || (treeParts[x][y][z] != null && localMap.getPlantBlock(mapX, mapY, mapZ) != null)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Genarates and places plants in {@link LocalGenContainer}
     *
     * @param specimen PlantType key from PlantMap representing tree
     * @param amount   relative amount
     */
    private void placePlants(String specimen, float amount) {
        PlantGenerator plantGenerator = new PlantGenerator();
        Pair<boolean[][][], ArrayList<Position>> pair = findAllAvailablePositions(specimen);
        ArrayList<Position> positions = pair.getValue();
        boolean[][][] array = pair.getKey();
        Random random = new Random();
        for (int number = (int) (positions.size() * amount / 2); number > 0; number--) {
            try {
                Position position = positions.remove(random.nextInt(positions.size()));
                array[position.getX()][position.getY()][position.getZ()] = false;
                Plant plant = null;
                plant = plantGenerator.generatePlant(specimen);
                plant.setPosition(position);
                container.getPlants().add(plant);
            } catch (DescriptionNotFoundException e) {
                System.out.println("material for plant " + specimen + " not found");
            }
        }
    }

    /**
     * Collects all positions suitable for specific plant.
     *
     * @param specimen plant to check availability
     * @return
     */
    private Pair<boolean[][][], ArrayList<Position>> findAllAvailablePositions(String specimen) {
        //TODO should count plant requirements for light level, water source, soil type
        ArrayList<Position> positions = new ArrayList<>();
        boolean[][][] array = new boolean[localMap.getxSize()][localMap.getySize()][localMap.getzSize()];
        PlantType type = PlantMap.getInstance().getPlantType(specimen);
        int borderPadding = type.isTree() ? Math.max(type.getTreeType().getRootRadius(), type.getTreeType().getCrownRadius()) : 0; // set border padding for trees
        String soilType = type.getSoilType();
        for (int x = borderPadding; x < localMap.getxSize() - borderPadding; x++) {
            for (int y = borderPadding; y < localMap.getySize() - borderPadding; y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (localMap.getBlockType(x, y, z) == floorCode
                            && localMap.getPlantBlock(x,y,z) == null
                            && MaterialMap.getInstance().getMaterial(localMap.getMaterial(x, y, z)).getTypes().contains(soilType)) { // surface material should be suitable for plant
                        positions.add(new Position(x, y, z));
                        array[x][y][z] = true;
                    }
                }
            }
        }
        Pair<boolean[][][], ArrayList<Position>> pair = new Pair<>(array, positions);
        return pair;
    }

    /**
     * Filters all PlantMap with local climate parameters and adds passed plants to list
     */
    private void filterPlants() {
        PlantMap.getInstance().getAllTypes().forEach((type) -> {
            if (rainfall < type.getMinRainfall()
                    || rainfall > type.getMaxRainfall()) {
                return; // too dry or wet
            }
            if (minTemp < type.getMinRainfall()
                    || maxTemp > type.getMaxTemperature()) {
                return; // too hot or cold
            }
            if (minTemp > type.getMaxGrowingTemperature()
                    || maxTemp < type.getMinGrowingTemperature()) { // plant can grow
                return; // plant grow zone out of local temp zone
            }
            if (type.isTree()) { //is plant tree or not
                weightedTreeTypes.put(type.getSpecimen(), 100f);
            } else {
                weightedPlantTypes.put(type.getSpecimen(), 1f);
            }
        });
    }
}