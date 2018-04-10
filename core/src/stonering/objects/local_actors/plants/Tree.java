package stonering.objects.local_actors.plants;

import stonering.enums.plants.TreeType;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 19.10.2017.
 */
public class Tree {
    private Position position;
    private TreeType type;
    private Plant[][][] blocks;
    private int age;
    private int materialId;
    private int stompZ;

    public Tree(int age, int materialId) {
        this.age = age;
        this.materialId = materialId;
    }

    public Plant[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Plant[][][] blocks) {
        this.blocks = blocks;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWoodMaterial() {
        return materialId;
    }

    public void setWoodMaterial(int woodMaterial) {
        this.materialId = woodMaterial;
    }

    public int getStompZ() {
        return stompZ;
    }

    public void setStompZ(int stompZ) {
        this.stompZ = stompZ;
    }

    public int getX() {
        return position.getX();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        position.setY(y);
    }

    public int getZ() {
        return position.getZ();
    }

    public void setZ(int z) {
        position.setZ(z);
    }

    public TreeType getType() {
        return type;
    }

    public void setType(TreeType type) {
        this.type = type;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
