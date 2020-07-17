package stonering.entity.plant;

import stonering.entity.Entity;
import stonering.enums.plants.PlantBlocksTypeEnum;

/**
 * Class to be contained on LocalMap. Also stores render data
 *
 * @author Alexander Kuzyakov on 30.11.2017.
 */
public class PlantBlock extends Entity {
    public AbstractPlant plant;
    public final int material;
    public final int blockType; // type from enum
    public final int[] atlasXY;
    public boolean harvested;

    public PlantBlock(int material, int blockType) {
        this.material = material;
        this.blockType = blockType;
        atlasXY = new int[2];
    }

    public boolean isPassable() {
        return PlantBlocksTypeEnum.getType(blockType).passable;
    }

    public PlantBlocksTypeEnum getType() {
        return PlantBlocksTypeEnum.getType(blockType);
    }
}
