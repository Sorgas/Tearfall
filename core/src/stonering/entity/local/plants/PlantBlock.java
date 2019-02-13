package stonering.entity.local.plants;

import stonering.util.geometry.Position;

import java.util.ArrayList;

/**
 * Class to be contained on LocalMap. Also stores render data
 *
 * @author Alexander Kuzyakov on 30.11.2017.
 */
public class PlantBlock {
    private AbstractPlant plant;
    private Position position; // position on map
    private int material;
    private int blockType;
    private int[] atlasXY;
    private ArrayList<String> harvestProducts;
    private ArrayList<String> cutProducts;

    public PlantBlock(int material, int blockType) {
        this.material = material;
        this.blockType = blockType;
        harvestProducts = new ArrayList<>();
        cutProducts = new ArrayList<>();
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ArrayList<String> getHarvestProducts() {
        return harvestProducts;
    }

    public void setHarvestProducts(ArrayList<String> harvestProducts) {
        this.harvestProducts = harvestProducts;
    }

    public ArrayList<String> getCutProducts() {
        return cutProducts;
    }

    public void setCutProducts(ArrayList<String> cutProducts) {
        this.cutProducts = cutProducts;
    }

    public AbstractPlant getPlant() {
        return plant;
    }
}
