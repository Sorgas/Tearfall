package stonering.enums.plants.raw;

import stonering.enums.plants.PlantLifeStage;
import stonering.enums.plants.PlantProduct;
import stonering.enums.plants.PlantType;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.generation.PlantPlacingTags.*;

/**
 * Utility class for processing data from json files.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class PlantTypeProcessor {
    private Set<Integer> allMonthsSet = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

    public PlantType processRawType(RawPlantType rawType) {
        PlantType type = new PlantType();
        type.name = rawType.name;
        type.title = rawType.title;
        type.materialName = rawType.materialName;
        type.description = rawType.description;
        type.temperatureBounds = rawType.temperatureBounds; // min and max temperature
        type.rainfallBounds = rawType.rainfallBounds;  // min and max painfall
        type.plantingStart = rawType.plantingStart;
        processPlacingTags(rawType, type);
        processRawLifeStages(rawType, type);
        type.setTypeFlags(); // should be after th setting of life stages
        return type;
    }

    /**
     * Fills list of stages for type.
     */
    private void processRawLifeStages(RawPlantType rawType, PlantType type) {
        int totalAge = 0;
        for (RawPlantLifeStage rawStage : rawType.lifeStages) {
            PlantLifeStage stage = processRawLifeStage(rawStage);
            totalAge += rawStage.stageLength;
            stage.stageEnd = totalAge;
            type.lifeStages.add(stage);
        }
    }

    /**
     * Creates {@link PlantLifeStage} from {@link RawPlantLifeStage}
     */
    private PlantLifeStage processRawLifeStage(RawPlantLifeStage rawStage) {
        PlantLifeStage stage = new PlantLifeStage();
        stage.titlePrefixSuffix = rawStage.titlePrefixSuffix;
        stage.stageLength = rawStage.stageLength;
        if(rawStage.harvestProduct != null)
            stage.harvestProduct = processHarvestProduct(rawStage.harvestProduct);
        stage.cutProducts = rawStage.cutProducts;
        stage.atlasXY = rawStage.atlasXY;
        stage.color = rawStage.color;
        stage.treeForm = rawStage.treeForm;
        return stage;
    }

    /**
     * Creates list of enum elements based on raw list of string.
     * Adds default values.
     */
    private void processPlacingTags(RawPlantType rawType, PlantType type) {
        for (String placingTag : rawType.placingTags) {
            type.placingTags.addAll(getTag(placingTag));
        }
        if (Collections.disjoint(type.placingTags, WATER_GROUP)) type.placingTags.add(WATER_FAR);
        if (Collections.disjoint(type.placingTags, SOIL_GROUP)) type.placingTags.add(SOIL_SOIL);
        if (Collections.disjoint(type.placingTags, LIGHT_GROUP)) {
            type.placingTags.add(LIGHT_HIGH);
            type.placingTags.add(LIGHT_LOW);
        }
    }

    private PlantProduct processHarvestProduct(List<String> productArgs) {
        PlantProduct product = new PlantProduct();
        product.name = productArgs.get(0);
        product.setFormulaArgs(Arrays.stream(productArgs.get(1).split("-")).map(s -> Integer.valueOf(s)).collect(Collectors.toList()).toArray(new Integer[3]));
        if ("all".equals(productArgs.get(2))) {
            product.months = allMonthsSet;
        } else {
            product.months = Arrays.stream(productArgs.get(2).split(",")).map(s -> Integer.valueOf(s)).collect(Collectors.toSet());
        }
        return product;
    }
}
