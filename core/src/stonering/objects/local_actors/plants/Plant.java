package stonering.objects.local_actors.plants;

import stonering.global.utils.Position;

/**
 * Represents plant
 * <p>
 * Created by Alexander on 19.10.2017.
 */
public class Plant extends AbstractPlant {
    private PlantBlock block;

    public Plant(int age) {
        this.age = age;
    }

    public PlantBlock getBlock() {
        return block;
    }

    public void setBlock(PlantBlock block) {
        this.block = block;
    }

    public Position getPosition() {
        return block.getPosition();
    }

    public void setPosition(Position position) {
        block.setPosition(position);
    }
}
