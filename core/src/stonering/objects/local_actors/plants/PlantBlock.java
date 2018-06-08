package stonering.objects.local_actors.plants;

import stonering.global.utils.Position;

import java.util.ArrayList;

/**
 * Created by Alexander on 30.11.2017.
 *
 * class to be contained on LocalMap. also stores render data
 */
public class PlantBlock {
    private AbstractPlant plant;
    private Position position; // position on map
    private int material;
    private int blockType;
    private int atlasX;
    private int atlasY;
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
