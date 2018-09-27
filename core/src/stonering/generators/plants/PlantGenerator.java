package stonering.generators.plants;

import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.TreeBlocksTypeEnum;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;

/**
 * Generates single tile plants (not trees).
 *
 * @author Alexander Kuzyakov on 05.04.2018.
 */
public class PlantGenerator {

    public Plant generatePlant(String specimen, int age) throws DescriptionNotFoundException {
        if(specimen.equals("blue_berry")) {
            System.out.println("asd");
        }
        Plant plant = new Plant(0);
        plant.setType(PlantMap.getInstance().getPlantType(specimen));

        plant.setBlock(new PlantBlock(MaterialMap.getInstance().getId(plant.getCurrentStage().getMaterialName()), TreeBlocksTypeEnum.SINGLE_PASSABLE.getCode()));
        plant.getBlock().setAtlasX(plant.getType().getAtlasX());
        plant.getBlock().setAtlasY(plant.getType().getAtlasY());
        plant.getBlock().setPlant(plant);
        plant.setAge(age);
        initBlockProducts(plant.getBlock(), age);
        return plant;
    }

    private void initBlockProducts(PlantBlock block, int age) {
        block.setHarvestProducts(block.getPlant().getCurrentStage().getHarvestProducts());
        block.setCutProducts(block.getPlant().getCurrentStage().getCutProducts());
    }
}
