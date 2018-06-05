package stonering.objects.local_actors.plants;

import stonering.enums.plants.TreeType;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 19.10.2017.
 */
public class Tree extends AbstractPlant{
    private Position position;
    private Plant[][][] blocks;

    public Tree(int age) {
        this.age = age;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
