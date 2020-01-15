package stonering.enums.plants.raw;

import stonering.enums.generation.PlantPlacingTagEnum;
import stonering.enums.items.type.raw.RawItemTypeProcessor;
import stonering.enums.plants.PlantLifeStage;
import stonering.enums.plants.PlantType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static stonering.enums.generation.PlantPlacingTagEnum.*;

/**
 * Utility class for processing data from json files.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class RawPlantTypeProcessor {
    private Set<Integer> allMonthsSet = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    private RawItemTypeProcessor processor = new RawItemTypeProcessor();

    public PlantType processRawType(RawPlantType rawType) {
        PlantType type = new PlantType(rawType);
        processPlacingTags(rawType, type);
        processRawLifeStages(rawType, type);
        type.setTypeFlags();
        return type;
    }

    /**
     * Fills list of stages for type.
     */
    private void processRawLifeStages(RawPlantType rawType, PlantType type) {
        int totalAge = 0;
        for (RawPlantLifeStage rawStage : rawType.lifeStages) {
            PlantLifeStage stage = new PlantLifeStage(rawStage);
            if (rawStage.harvestProduct != null)
                stage.harvestProduct = processor.processExtendedType(rawStage.harvestProduct, type.name);
            totalAge += rawStage.stageLength;
            stage.stageEnd = totalAge;
            type.lifeStages.add(stage);
        }
    }

    /**
     * Creates list of enum elements based on raw list of string.
     * Adds default values.
     */
    private void processPlacingTags(RawPlantType rawType, PlantType type) {
        rawType.placingTags.stream().map(PlantPlacingTagEnum::getTag).forEach(type.placingTags::addAll);
        if (Collections.disjoint(type.placingTags, WATER_GROUP))
            type.placingTags.add(WATER_FAR); // water placement not defined
        if (Collections.disjoint(type.placingTags, SOIL_GROUP))
            type.placingTags.add(SOIL_SOIL); // soil type not defined
        if (Collections.disjoint(type.placingTags, LIGHT_GROUP)) { // light requirements nod defined
            type.placingTags.add(LIGHT_HIGH);
            type.placingTags.add(LIGHT_LOW);
        }
    }
}
