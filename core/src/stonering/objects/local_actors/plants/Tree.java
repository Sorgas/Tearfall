package stonering.objects.local_actors.plants;

/**
 * Created by Alexander on 19.10.2017.
 */
public class Tree {
    private String specimen;
    private PlantBlock[][][] blocks;
    private int age;
    private int[] lifeStages;
    private int woodMaterialId;
    private int stompZ;
    private int x;
    private int y;
    private int z;

    public Tree(String specimen, int age, int woodMaterialId) {
        this.specimen = specimen;
        this.age = age;
        this.woodMaterialId = woodMaterialId;
    }

    public PlantBlock[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(PlantBlock[][][] blocks) {
        this.blocks = blocks;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
