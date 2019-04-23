package stonering.enums.plants;

import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores plant parameters.
 */
public class PlantType implements Initable {
    public String name;
    public String title;
    public String materialName; // in null for substrates

    public String description;
    public int[] temperatureBounds;
    public int[] rainfallBounds;
    public ArrayList<PlantLifeStage> lifeStages;
    public ArrayList<String> placingTags;
    public List<Integer> plantingStart;

    // set after loading types
    private boolean isTree;
    private boolean isSubstrate;

    public PlantType() {
        temperatureBounds = new int[4];
        rainfallBounds = new int[2];
        plantingStart = new ArrayList<>();
    }

    /**
     * Recounts life stages bounds. Sets type flags.
     */
    @Override
    public void init() {
        int totalAge = 0;
        for (PlantLifeStage lifeStage : lifeStages) {
            totalAge += lifeStage.stageLength;
            lifeStage.stageEnd = totalAge;
        }
        isTree = lifeStages.get(0).treeForm != null;
        isSubstrate = materialName == null;
    }

    public int getMaxAge() {
        return lifeStages.get(lifeStages.size() - 1).getStageEnd();
    }

    /**
     * Represents period in plant's life.
     */
    public static class PlantLifeStage {
        public String[] titlePrefixSuffix;
        public int stageLength;
        public int stageEnd;
        public ArrayList<String> harvestProducts;
        public ArrayList<String> cutProducts;
        public int[] atlasXY;
        public String color;
        public List<Integer> treeForm; // in null for non-trees

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
        return isTree;
    }

    public boolean isSubstrate() {
        return isSubstrate;
    }
}
