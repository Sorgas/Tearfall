package stonering.enums.plants;

import java.util.List;

/**
 * Simple bean for reading data from json files.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class RawPlantLifeStage {
    public String[] titlePrefixSuffix;
    public int stageLength;
    public List<String> harvestProduct;
    public List<String> cutProducts;
    public int[] atlasXY;
    public String color;
    public List<Integer> treeForm; // not null only for trees
}
