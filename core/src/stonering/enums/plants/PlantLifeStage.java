package stonering.enums.plants;

import java.util.List;

/**
 * One period of plant life. Stages differs by name modifiers, used sprites, color, and plant's products.
 * //TODO add additional description
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class PlantLifeStage {
    public String[] titlePrefixSuffix;
    public int stageLength;
    public int stageEnd; // calculated for faster checking
    public PlantProduct harvestProduct; // products differ between stages
    public List<String> cutProducts;
    public String color;
    public List<Integer> treeForm; // is null for non-trees
}
