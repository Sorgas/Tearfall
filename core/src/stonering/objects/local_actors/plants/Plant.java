package stonering.objects.local_actors.plants;

import stonering.enums.plants.PlantType;

/**
 * Created by Alexander on 19.10.2017.
 */
public class Plant {
    private Tree tree;
    private PlantType type;
    private PlantBlock block;
    private int age;
    private int x;
    private int y;
    private int z;

    public Plant(int age) {
        this.age = age;
    }

    public PlantType getType() {
        return type;
    }

    public void setType(PlantType type) {
        this.type = type;
    }

    public PlantBlock getBlock() {
        return block;
    }

    public void setBlock(PlantBlock block) {
        this.block = block;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
