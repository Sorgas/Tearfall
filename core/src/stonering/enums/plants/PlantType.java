package stonering.enums.plants;

import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores plant parameters
 */
public class PlantType implements Initable {
    public String name;
    public String title;
    public String materialName;

    public String description;
    public int[] temperatureBounds;
    public int[] rainfallBounds;
    public ArrayList<PlantLifeStage> lifeStages;
    public ArrayList<String> placingTags;
    public List<Integer> plantingStart;

    public PlantType() {
        temperatureBounds = new int[4];
        rainfallBounds = new int[2];
        plantingStart = new ArrayList<>();
    }

    /**
     * Recounts life stages bounds.
     */
    @Override
    public void init() {
        int totalAge = 0;
        for (PlantLifeStage lifeStage : lifeStages) {
            totalAge += lifeStage.stageLength;
            lifeStage.stageEnd = totalAge;
        }
    }

    public int getMaxAge() {
        return lifeStages.get(lifeStages.size() - 1).getStageEnd();
    }

    public static class PlantLifeStage {
        public String[] titlePrefixSuffix;
        public int stageLength;
        public int stageEnd;
        public ArrayList<String> harvestProducts;
        public ArrayList<String> cutProducts;
        public int[] atlasXY;
        public String color;
        public List<Integer> treeForm;

        public int getTreeRadius() {
            return Math.max(treeForm.get(0), treeForm.get(3));
        }

        public Position getStompPosition() {
            return new Position(treeForm.get(0), treeForm.get(0), treeForm.get(2));
        }

        public int getStageEnd() {
            return stageEnd;
        }
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isTree() {
        return lifeStages.get(0).treeForm != null;
    }
}
