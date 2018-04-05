package stonering.generators.localgen.generators;

import com.badlogic.gdx.scenes.scene2d.ui.List;
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

public class LocalFloraGanerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;
    private float maxTemp;
    private float minTemp;
    private float rainfall;

    private final int wallCode = BlockTypesEnum.WALL.getCode();
    private final int floorCode = BlockTypesEnum.FLOOR.getCode();

    private PlantGenerator plantGenerator;
    private HashMap<String, Float> weightedPlantTypes;

    public LocalFloraGanerator(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        this.localAreaSize = config.getAreaSize();
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

    }

    private void generateFlora() {
        weightedPlantTypes.forEach((specimen, amount) -> placePlant(specimen, amount));
    }

    private void placePlant(String specimen, float amount) {
        Plant plant = plantGenerator.generatePlant(specimen);

    }

    private ArrayList<Position> findAllAvailablePositions(String specimen) {
        ArrayList<Position> positions = new ArrayList<>();
        ArrayList<String> soilTypes = PlantMap.getInstance().getPlantType(specimen).getSoilType();
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (localMap.getBlockType(x, y, z) == floorCode) {
                        ArrayList<String> cellTypes =  MaterialMap.getInstance().getMaterial(localMap.getMaterial(x, y, z)).getTypes();
                            for (String type : soilTypes) {
                                if(cellTypes.contains(type)) {

                                }
                            }
                        }
                    }
                }
            }
        }
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
