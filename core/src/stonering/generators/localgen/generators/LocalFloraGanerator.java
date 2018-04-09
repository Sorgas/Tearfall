package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.global.utils.Position;
import stonering.objects.local_actors.plants.Plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LocalFloraGanerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private LocalMap localMap;
    private float maxTemp;
    private float minTemp;
    private float rainfall;

    private final int floorCode = BlockTypesEnum.FLOOR.getCode();

    private HashMap<String, Float> weightedPlantTypes;

    public LocalFloraGanerator(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        this.localMap = container.getLocalMap();
        int x = config.getLocation().getX();
        int y = config.getLocation().getY();
        rainfall = container.getWorldMap().getRainfall(x, y);
        minTemp = container.getMonthlyTemperatures()[0];
        maxTemp = minTemp;
        for (float temp : container.getMonthlyTemperatures()) {
            minTemp = temp < minTemp ? temp : minTemp;
            maxTemp = temp > maxTemp ? temp : maxTemp;
        }
    }

    public void execute() {
        weightedPlantTypes = new HashMap<>();
        PlantMap.getInstance().getAllTypes().forEach((type) -> filterPlant(type));
        generateFlora();
    }

    private void generateFlora() {
        weightedPlantTypes.forEach((specimen, amount) -> placePlants(specimen, amount));
    }

    private void placePlants(String specimen, float amount) {
        PlantGenerator plantGenerator = new PlantGenerator();
        ArrayList<Position> positions = findAllAvailablePositions(specimen);
        Random random = new Random();
        for (int number = (int) (positions.size() * amount / 2); number > 0; number--) {
            Position position = positions.remove(random.nextInt(positions.size()));
            Plant plant = plantGenerator.generatePlant(specimen);
            container.getPlants().add(plant);
            localMap.setPlantBlock(position, plant.getBlock());
        }
    }

    private ArrayList<Position> findAllAvailablePositions(String specimen) {
        ArrayList<Position> positions = new ArrayList<>();
        String soilType = PlantMap.getInstance().getPlantType(specimen).getSoilType();
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (localMap.getBlockType(x, y, z) == floorCode
                            && MaterialMap.getInstance().getMaterial(localMap.getMaterial(x, y, z)).getTypes().contains(soilType)) {
                        positions.add(new Position(x, y, z));
                    }
                }
            }
        }
        return positions;
    }

    private void randomizePlantAge(Plant plant) {

    }

    private void filterPlant(PlantType plantType) {
        if (rainfall < plantType.getMinRainfall()
                || rainfall > plantType.getMaxRainfall()) {
            return; // too dry or wet
        }
        if (minTemp < plantType.getMinRainfall()
                || maxTemp > plantType.getMaxTemperature()) {
            return; // too hot or cold
        }
        if (minTemp > plantType.getMaxGrowingTemperature()
                || maxTemp < plantType.getMinGrowingTemperature()) { // plant can grow
            return; // plant grow zone out of local temp zone
        }
        weightedPlantTypes.put(plantType.getSpecimen(), 1f);
    }
}