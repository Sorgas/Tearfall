package stonering.generators.plants;

import stonering.entity.RenderAspect;
import stonering.entity.item.aspects.SeedAspect;
import stonering.entity.plant.Plant;
import stonering.entity.plant.PlantBlock;
import stonering.entity.plant.SubstratePlant;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.enums.plants.PlantTypeMap;
import stonering.stage.renderer.atlas.AtlasesEnum;

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
        updatePlantBlock(plant);
        return plant;
    }

    public Plant generatePlant(SeedAspect aspect) {
        return generatePlant(aspect.specimen, 0);
    }

    public SubstratePlant generateSubstrate(String specimen, int age) {
        SubstratePlant plant = new SubstratePlant(PlantTypeMap.getSubstrateType(specimen));
        createPlantAspects(plant, age);
        createPlantBlock(plant);
        updatePlantBlock(plant);
        return plant;
    }

    public void applyPlantGrowth(Plant plant) {

        createPlantBlock(plant);
    }

    private void updatePlantBlock(Plant plant) {
        PlantBlock block = plant.getBlock();
        int stageIndex = plant.optional(PlantGrowthAspect.class)
                .map(aspect -> aspect.stageIndex)
                .orElse(0);
        block.optional(RenderAspect.class)
                .ifPresent(aspect -> aspect.region = AtlasesEnum.plants.getBlockTile(plant.type.atlasName, plant.type.atlasXY[0] + stageIndex, plant.type.atlasXY[1]));
    }

    private <T extends Plant> void createPlantBlock(T plant) {
        // TODO add product aspect
        String materialName = plant.type.materialName;
        PlantBlock block = new PlantBlock(MaterialMap.getId(materialName), PlantBlocksTypeEnum.SINGLE_PASSABLE.getCode());
        block.add(new RenderAspect(null));
        plant.setBlock(block);
    }

    private <T extends Plant> T createPlantAspects(T plant, int age) {
        plant.add(new PlantGrowthAspect(plant, age));
        return plant;
    }
}
