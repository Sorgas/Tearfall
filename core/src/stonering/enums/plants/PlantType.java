package stonering.enums.plants;

import stonering.enums.generation.PlacingTagEnum;
import stonering.enums.plants.raw.RawPlantType;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores plant parameters. Created from {@link RawPlantType}
 *
 * @author Alexander_Kuzyakov on 06.03.2018
 */
public class PlantType {
    public String name;
    public String title;
    public String materialName; // is null for substrates
    public String description;
    public String atlasName;
    
    public int[] temperatureBounds; // min and max temperature
    public int[] rainfallBounds;  // min and max painfall
    public final List<PlantLifeStage> lifeStages = new ArrayList<>();
    public final List<PlacingTagEnum> placingTags = new ArrayList<>();
    public List<Integer> plantingStart; // months, when plant can be planted on farms
    public int[] atlasXY;
    public boolean destroyOnHarvest;
    
    public boolean isPlant;
    public boolean isTree;
    public boolean isSubstrate;

    public PlantType(RawPlantType rawType) {
        name = rawType.name;
        title = rawType.title;
        materialName = rawType.materialName;
        description = rawType.description;
        temperatureBounds = rawType.temperatureBounds; // min and max temperature
        rainfallBounds = rawType.rainfallBounds;  // min and max rainfall
        plantingStart = rawType.plantingStart;
        atlasXY = rawType.atlasXY;
        destroyOnHarvest = rawType.destroyOnHarvest;
    }

    public void setTypeFlags() {
        isTree = lifeStages.get(0).treeForm != null;
        isSubstrate = materialName == null;
        isPlant = !isTree && !isSubstrate;
    }

    public int getMaxAge() {
        return lifeStages.get(lifeStages.size() - 1).stageEnd;
    }

    @Override
    public String toString() {
        return title;
    }
}
