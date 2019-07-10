package stonering.enums.plants;

import stonering.util.geometry.Position;

import java.util.List;

/**
 * One period of plant life. Stages differs by name modificators, used sprites, color, and plant's products.
 * //TODO add additional description
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class PlantLifeStage {
    public String[] titlePrefixSuffix;
    public int stageLength;
    public int stageEnd;
    public PlantProduct harvestProduct;
    public List<String> cutProducts;
    public int xOffset;
    public String color;
    public List<Integer> treeForm; // is null for non-trees

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
