package stonering.generators.plants;

import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.plant.SubstratePlant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.entity.plant.Plant;
import stonering.entity.plant.PlantBlock;

import java.util.Arrays;
import java.util.Optional;

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
    public Plant generatePlant(String specimen, int age) {
        //TODO set age
        return Optional.ofNullable(PlantTypeMap.getPlantType(specimen))
                .map(Plant::new)
                .map(this::createPlantBlock)
                .map(this::createPlantAspects)
                .orElse(null);
    }

    /**
     * Generates {@link SubstratePlant} object by name of its type and initial age.
     */
    public SubstratePlant generateSubstrate(String specimen, int age) {
        //TODO set age
        return Optional.ofNullable(PlantTypeMap.getSubstrateType(specimen))
                .map(SubstratePlant::new)
                .map(this::createPlantBlock)
                .map(this::createPlantAspects)
                .orElse(null);
    }

    /**
     * Generates plant by seed aspect of item.
     * Used for planting on farms.
     */
    public Plant generatePlant(SeedAspect aspect) {
        return generatePlant(aspect.specimen, 0);
    }

    public void applyPlantGrowth(Plant plant) {
        createPlantBlock(plant);
    }

    private <T extends Plant> T createPlantBlock(T plant) {
        String materialName = plant.type.materialName;
        if (materialName == null) materialName = "generic_plant";
        PlantBlock block = new PlantBlock(MaterialMap.getId(materialName), PlantBlocksTypeEnum.SINGLE_PASSABLE.getCode());
        int[] atlasXY = Arrays.copyOf(plant.type.atlasXY, 2);
        atlasXY[0] += plant.get(PlantGrowthAspect.class).currentStage;
        block.setAtlasXY(atlasXY);
        block.setHarvested(false);
        plant.setBlock(block);
        return plant;
    }
    
    private <T extends Plant> T createPlantAspects(T plant) {
        plant.add(new PlantGrowthAspect(plant));
        return plant;
    }
}
