package stonering.enums.plants;

import stonering.enums.plants.raw.RawPlantLifeStage;

import java.util.List;

/**
 * One period of plant life. Stages differs by name modifiers, used sprites, color, and plant's products.
 * Sprite of first stage defined in {@link PlantType}, every next stage adds 1 to atlas x.
 * <p>
 * //TODO add additional description
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class PlantLifeStage {
    public String[] titlePrefixSuffix;
    public int stageLength;
    public String harvestProduct; // products differ between stages
    public List<Integer> treeForm; // is null for non-trees
    public int productDropRatio; // number of product items per 1 block
    public String color;

    public int stageEnd; // calculated for faster checking

    public PlantLifeStage(RawPlantLifeStage rawStage) {
        titlePrefixSuffix = rawStage.titlePrefixSuffix;
        stageLength = rawStage.stageLength;
        color = rawStage.color;
        treeForm = rawStage.treeForm;
        productDropRatio = rawStage.productDropRatio;
    }
}
