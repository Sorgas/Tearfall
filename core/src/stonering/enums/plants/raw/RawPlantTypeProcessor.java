package stonering.enums.plants.raw;

import stonering.enums.generation.PlacingTagEnum;
import stonering.enums.items.type.raw.RawItemTypeProcessor;
import stonering.enums.plants.PlantLifeStage;
import stonering.enums.plants.PlantType;

import java.util.Arrays;
import java.util.Collections;

import static stonering.enums.generation.PlacingTagEnum.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for processing data from json files.
 * If type title is not set, it is generated from name.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class RawPlantTypeProcessor {
    private RawItemTypeProcessor processor = new RawItemTypeProcessor();

    public PlantType process(RawPlantType rawType) {
        PlantType type = new PlantType(rawType);
        processPlacingTags(rawType, type);
        processRawLifeStages(rawType, type);
        initTitle(type);
        type.setTypeFlags();
        return type;
    }

    private void initTitle(PlantType type) {
        if(type.title.isBlank()) {
            type.title = Arrays.stream(type.name.split("_"))
                    .map(StringUtils::capitalize)
                    .reduce((s1, s2) -> s1 + " " + s2)
                    .orElse("");
        }
    }
    
    /**
     * Fills list of stages for type.
     */
    private void processRawLifeStages(RawPlantType rawType, PlantType type) {
        int totalAge = 0;
        for (RawPlantLifeStage rawStage : rawType.lifeStages) {
            PlantLifeStage stage = new PlantLifeStage(rawStage);
            stage.harvestProduct = rawStage.harvestProduct;
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
        rawType.placingTags.stream().map(PlacingTagEnum::get).forEach(type.placingTags::add);
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
