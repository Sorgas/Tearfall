package stonering.generators.plants;

import stonering.enums.plants.PlantType;
import stonering.enums.plants.PlantTypeMap;
import stonering.objects.local_actors.plants.Plant;

/**
 * Created by Alexander on 19.10.2017.
 */
public class PlantsGenerator {
    private PlantTypeMap map;

    public PlantsGenerator() {
        map = new PlantTypeMap();
    }

    public Plant generatePlant(String speciment, int age) {
        PlantType type = map.getTreeType(speciment);
        Plant plant = new Plant(speciment, age);
        return plant;
    }
}
