package stonering.enums.plants;

import stonering.enums.generation.PlantPlacingTags;
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
    public String materialName; // in null for substrates
    public String description;

    public int[] temperatureBounds = new int[2]; // min and max temperature
    public int[] rainfallBounds = new int[2];  // min and max painfall
    public List<PlantLifeStage> lifeStages = new ArrayList<>();
    public List<PlantPlacingTags> placingTags = new ArrayList<>();
    public List<Integer> plantingStart = new ArrayList<>(); // months, when plant can be planted on farms
    public int[] atlasXY = new int[2];

    private boolean isPlant;
    private boolean isTree;
    private boolean isSubstrate;

    public void setTypeFlags() {
        isTree = lifeStages.get(0).treeForm != null;
        isSubstrate = materialName == null;
        isPlant = !isTree && !isSubstrate;
    }

    public int getMaxAge() {
        return lifeStages.get(lifeStages.size() - 1).getStageEnd();
    }

    public boolean isTree() {
        return isTree;
    }

    public boolean isSubstrate() {
        return isSubstrate;
    }

    public boolean isPlant() {
        return isPlant;
    }

    @Override
    public String toString() {
        return title;
    }
}
