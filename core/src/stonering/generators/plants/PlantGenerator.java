package stonering.generators.plants;

import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.plant.SubstratePlant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.entity.plant.Plant;
import stonering.entity.plant.PlantBlock;

import java.util.Arrays;

/**
 * Generates single tile plants (not trees).
 * Does not set plant's positions.
 *
 * @author Alexander Kuzyakov on 05.04.2018.
 */
public class PlantGenerator {

    /**
     * Generates {@link Plant} object by name of its type and initial age.
     */
    public Plant generatePlant(String specimen, int age) throws DescriptionNotFoundException {
        PlantType type = PlantTypeMap.getInstance().getPlantType(specimen);
        if (type == null) throw new DescriptionNotFoundException("Plant type " + specimen + " not found");
        Plant plant = new Plant(type, age);
        plant.setBlock(createPlantBlock(plant));
        plant.addAspect(new PlantGrowthAspect(plant));
        return plant;
    }

    /**
     * Generates {@link SubstratePlant} object by name of its type and initial age.
     */
    public SubstratePlant generateSubstrate(String specimen, int age) throws DescriptionNotFoundException {
        PlantType type = PlantTypeMap.getInstance().getSubstrateType(specimen);
        if (type == null) throw new DescriptionNotFoundException("Plant type " + specimen + " not found");
        SubstratePlant plant = new SubstratePlant(type, age);
        plant.setBlock(createPlantBlock(plant));
        plant.addAspect(new PlantGrowthAspect(plant));
        return plant;
    }

    /**
     * Generates plant by seed aspect of item.
     * Used for planting on farms.
     */
    public Plant generatePlant(SeedAspect aspect) throws DescriptionNotFoundException {
        return generatePlant(aspect.specimen, 0);
    }

    public void applyPlantGrowth(Plant plant) {
        plant.setBlock(createPlantBlock(plant));
    }

    private PlantBlock createPlantBlock(Plant plant) {
        String materialName = plant.getType().materialName;
        if (materialName == null) materialName = "generic_plant";
        PlantBlock block = new PlantBlock(MaterialMap.instance().getId(materialName), PlantBlocksTypeEnum.SINGLE_PASSABLE.getCode());
        int[] atlasXY = Arrays.copyOf(plant.getType().atlasXY, 2);
        atlasXY[0] += plant.getCurrentStageIndex();
        block.setAtlasXY(atlasXY);
        block.setHarvested(false);
        return block;
    }
}
