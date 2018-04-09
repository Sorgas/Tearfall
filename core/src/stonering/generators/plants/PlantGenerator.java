package stonering.generators.plants;

import stonering.enums.plants.PlantMap;
import stonering.objects.local_actors.plants.Plant;

/**
 * Created by Alexander on 05.04.2018.
 */
public class PlantGenerator {

    public Plant generatePlant(String specimen) {
        Plant plant = new Plant(0);
        plant.setType(PlantMap.getInstance().getPlantType(specimen));
        return plant;
    }
}
