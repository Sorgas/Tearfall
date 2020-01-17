package stonering.enums.plants.raw;

import stonering.enums.items.type.raw.RawItemType;

import java.util.List;

/**
 * Simple bean for reading data from json files.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class RawPlantLifeStage {
    public String[] titlePrefixSuffix;
    public int stageLength;
    public RawItemType harvestProduct;
    public List<String> cutProducts;
    public String color = "0xffffffff"; // white is default
    public List<Integer> treeForm; // not null only for trees
    public int productDropRatio;
    public int productGrowRatio;
}
