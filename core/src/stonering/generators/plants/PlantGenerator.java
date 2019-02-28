package stonering.generators.plants;

import stonering.entity.local.plants.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.TreeBlocksTypeEnum;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;

/**
 * Generates single tile plants (not trees).
 *
 * @author Alexander Kuzyakov on 05.04.2018.
 */
public class PlantGenerator {

    public Plant generatePlant(String specimen, int age) throws DescriptionNotFoundException {
        Plant plant = new Plant(null, 0);
        if (PlantMap.getInstance().getPlantType(specimen) == null)
            throw new DescriptionNotFoundException("Plant type " + specimen + " not found");
        plant.setType(PlantMap.getInstance().getPlantType(specimen));
        plant.setAge(age);
        plant.setBlock(createPlantBlock(plant, age));
        plant.getAspects().put(PlantGrowthAspect.NAME, new PlantGrowthAspect(plant));
        return plant;
    }

    private void initBlockProducts(PlantBlock block, int age) {
        block.setHarvestProducts(block.getPlant().getCurrentStage().getHarvestProducts());
        block.setCutProducts(block.getPlant().getCurrentStage().getCutProducts());
    }

    private PlantBlock createPlantBlock(Plant plant, int age) {
        PlantBlock plantBlock = new PlantBlock(MaterialMap.getInstance().getId(plant.getCurrentStage().getMaterialName()), TreeBlocksTypeEnum.SINGLE_PASSABLE.getCode());
        plantBlock.setAtlasXY(new int[]{
                plant.getCurrentStage().getAtlasXY()[0],
                plant.getCurrentStage().getAtlasXY()[1]});
        plantBlock.setPlant(plant);
        initBlockProducts(plantBlock, age);
        return plantBlock;
    }

    public void applyPlantGrowth(Plant plant) {
        plant.setBlock(createPlantBlock(plant, plant.getAge()));
    }
}
