package stonering.objects.local_actors.plants;

import stonering.enums.plants.TreeType;
import stonering.global.utils.Position;

/**
 * Multitile plant, generally for trees. Position is coordinates of tree stomp on map.
 *
 * Created by Alexander on 19.10.2017.
 */
public class Tree extends AbstractPlant {
    private Position position;
    private PlantBlock[][][] blocks;

    public Tree(int age) {
        this.age = age;
    }

    public Position getRelativePosition(Position mapPos) {
        return new Position(
                mapPos.getX() + type.getTreeType().getTreeRadius() - position.getX(),
                mapPos.getY() + type.getTreeType().getTreeRadius() - position.getY(),
                mapPos.getZ() + type.getTreeType().getRootDepth() - position.getZ()
        );
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
