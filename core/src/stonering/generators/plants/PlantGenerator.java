package stonering.generators.plants;

import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.TreeBlocksTypeEnum;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;

/**
 * Created by Alexander on 05.04.2018.
 */
public class PlantGenerator {

    public Plant generatePlant(String specimen) throws DescriptionNotFoundException {
        Plant plant = new Plant(0);
        plant.setType(PlantMap.getInstance().getPlantType(specimen));

        plant.setBlock(new PlantBlock(MaterialMap.getInstance().getId(plant.getType().getMaterialName()), TreeBlocksTypeEnum.SINGLE_PASSABLE.getCode()));
        plant.getBlock().setAtlasX(plant.getType().getAtlasX());
        plant.getBlock().setAtlasY(plant.getType().getAtlasY());
        return plant;
    }
}
