package stonering.generators.plants;

import stonering.entity.local.items.aspects.SeedAspect;
import stonering.entity.local.plants.SubstratePlant;
import stonering.entity.local.plants.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;

/**
 * Generates single tile plants (not trees).
 *
 * @author Alexander Kuzyakov on 05.04.2018.
 */
public class PlantGenerator {

    /**
     * Generates {@link Plant} object by name of its type and initial age.
     */
    public Plant generatePlant(String specimen, int age) throws DescriptionNotFoundException {
        if (PlantMap.getInstance().getPlantType(specimen) == null)
            throw new DescriptionNotFoundException("Plant type " + specimen + " not found");
        Plant plant = new Plant(null, 0);
        plant.setType(PlantMap.getInstance().getPlantType(specimen));
        plant.setBlock(createPlantBlock(plant, age));
        plant.addAspect(new PlantGrowthAspect(plant));
        return plant;
    }

    /**
     * Generates {@link SubstratePlant} object by name of its type and initial age.
     */
    public SubstratePlant generateSubstrate(String specimen, int age) throws DescriptionNotFoundException {
        if (PlantMap.getInstance().getPlantType(specimen) == null)
            throw new DescriptionNotFoundException("Plant type " + specimen + " not found");
        SubstratePlant plant = new SubstratePlant(null, 0);
        plant.setType(PlantMap.getInstance().getPlantType(specimen));
        plant.setBlock(createPlantBlock(plant, age));
        plant.addAspect(new PlantGrowthAspect(plant));
        return plant;
    }

    /**
     * Generates plant by seed aspect of item.
     * Used for planting on farms.
     */
    public Plant generatePlant(SeedAspect aspect) throws DescriptionNotFoundException {
        return generatePlant(aspect.getSpecimen(), 0);
    }

    private void initBlockProducts(PlantBlock block, int age) {
        block.setHarvestProducts(block.getPlant().getCurrentStage().harvestProducts);
        block.setCutProducts(block.getPlant().getCurrentStage().cutProducts);
    }

    private PlantBlock createPlantBlock(Plant plant, int age) {
        PlantBlock plantBlock = new PlantBlock(MaterialMap.getInstance().getId(plant.getType().materialName), PlantBlocksTypeEnum.SINGLE_PASSABLE.getCode());
        plantBlock.setAtlasXY(new int[]{
                plant.getCurrentStage().atlasXY[0],
                plant.getCurrentStage().atlasXY[1]});
        plantBlock.setPlant(plant);
        initBlockProducts(plantBlock, age);
        return plantBlock;
    }

    public void applyPlantGrowth(Plant plant) {
        plant.setBlock(createPlantBlock(plant, plant.getAge()));
    }
}
