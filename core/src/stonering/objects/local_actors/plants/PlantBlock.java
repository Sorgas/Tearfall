package stonering.objects.local_actors.plants;

/**
 * Created by Alexander on 30.11.2017.
 *
 * class to be contained on LocalMap. also stores render data
 */
public class PlantBlock {
    private Plant plant;
    private int material;
    private int blockType;
    private int atlasX;
    private int atlasY;

    public PlantBlock(int material, int blockType) {
        this.material = material;
        this.blockType = blockType;
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

    public int getAtlasX() {
        return atlasX;
    }

    public void setAtlasX(int atlasX) {
        this.atlasX = atlasX;
    }

    public int getAtlasY() {
        return atlasY;
    }

    public void setAtlasY(int atlasY) {
        this.atlasY = atlasY;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
