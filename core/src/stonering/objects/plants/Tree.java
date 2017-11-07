package stonering.objects.plants;

import stonering.enums.materials.Material;

/**
 * Created by Alexander on 19.10.2017.
 */
public class Tree {
    private String specimen;
    private int[][][] blockTypes;
    private int age;
    private int[] lifeStages;
    private int woodMaterialId;
    private int stompZ;

    public Tree(String specimen, int age, int woodMaterialId) {
        this.specimen = specimen;
        this.age = age;
        this.woodMaterialId = woodMaterialId;
    }

    public int[][][] getBlockTypes() {
        return blockTypes;
    }

    public void setBlockTypes(int[][][] blockTypes) {
        this.blockTypes = blockTypes;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWoodMaterial() {
        return woodMaterialId;
    }

    public void setWoodMaterial(int woodMaterial) {
        this.woodMaterialId = woodMaterial;
    }

    public int getStompZ() {
        return stompZ;
    }

    public void setStompZ(int stompZ) {
        this.stompZ = stompZ;
    }
}
