package stonering.generators.plants;

import stonering.entity.RenderAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.plant.SubstratePlant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.entity.plant.Plant;
import stonering.entity.plant.PlantBlock;
import stonering.stage.renderer.AtlasesEnum;

import java.util.Optional;

/**
 * Generates single tile plants (not trees).
 * Does not set plant's positions.
 *
 * @author Alexander Kuzyakov on 05.04.2018.
 */
public class PlantGenerator {

    public Plant generatePlant(String specimen, int age) {
        Plant plant = new Plant(PlantTypeMap.getPlantType(specimen));
        createPlantAspects(plant, age);
        createPlantBlock(plant);
        return plant;
    }

    public Plant generatePlant(SeedAspect aspect) {
        return generatePlant(aspect.specimen, 0);
    }

    public SubstratePlant generateSubstrate(String specimen, int age) {
        SubstratePlant plant = new SubstratePlant(PlantTypeMap.getSubstrateType(specimen));
        createPlantAspects(plant, age);
        createPlantBlock(plant);
        return plant;
    }

    public void applyPlantGrowth(Plant plant) {
        createPlantBlock(plant);
    }

    private <T extends Plant> void createPlantBlock(T plant) {
        String materialName = plant.type.materialName;
        PlantBlock block = new PlantBlock(MaterialMap.getId(materialName), PlantBlocksTypeEnum.SINGLE_PASSABLE.getCode());
        plant.getOptional(PlantGrowthAspect.class)
                .map(aspect -> aspect.currentStage)
                .or(() -> Optional.of(0))
                .map(stageIndex -> new RenderAspect(AtlasesEnum.plants, plant.type.atlasName, plant.type.atlasXY[0] + stageIndex, plant.type.atlasXY[1]))
                .ifPresent(block::add);
        plant.setBlock(block);
    }

    private <T extends Plant> T createPlantAspects(T plant, int age) {
        plant.add(new PlantGrowthAspect(plant, age));
        return plant;
    }
}
