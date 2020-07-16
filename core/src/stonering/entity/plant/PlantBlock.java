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
    private int material;
    private int blockType; // type from enum
    private int[] atlasXY;
    private boolean harvested;

    public PlantBlock(int material, int blockType) {
        this.material = material;
        this.blockType = blockType;
    }

    public boolean isPassable() {
        return PlantBlocksTypeEnum.getType(blockType).passable;
    }

    public PlantBlocksTypeEnum getType() {
        return PlantBlocksTypeEnum.getType(blockType);
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    public int[] getAtlasXY() {
        return atlasXY;
    }

    public void setAtlasXY(int[] atlasXY) {
        this.atlasXY = atlasXY;
    }

    public void setPlant(AbstractPlant plant) {
        this.plant = plant;
    }

    public AbstractPlant getPlant() {
        return plant;
    }

    public boolean isHarvested() {
        return harvested;
    }

    public void setHarvested(boolean harvested) {
        this.harvested = harvested;
    }
}
