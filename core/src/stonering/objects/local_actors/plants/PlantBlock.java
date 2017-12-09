package stonering.objects.local_actors.plants;

/**
 * Created by Alexander on 30.11.2017.
 */
public class PlantBlock {
    private int material;
    private int blockType;

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
}
