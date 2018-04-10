package stonering.objects.local_actors.plants;

import stonering.enums.plants.PlantType;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 19.10.2017.
 */
public class Plant {
    private Position position;
    private Tree tree; // every tile of a tree is a plant. this is a back link for affecting whole tree by actions with one tile(branches cut off, infection, stomp cut)
    private PlantType type;
    private PlantBlock block;
    private int age;

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
