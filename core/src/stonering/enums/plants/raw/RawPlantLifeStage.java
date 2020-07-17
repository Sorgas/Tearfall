package stonering.enums.plants.raw;

import java.util.List;

/**
 * Simple bean for reading data from json files.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class RawPlantLifeStage {
    public String[] titlePrefixSuffix;
    public int stageLength;
    public String harvestProduct;
    public String color = "0xffffffff"; // white is default
    public List<Integer> treeForm; // not null only for trees
    public int productDropRatio;
}
