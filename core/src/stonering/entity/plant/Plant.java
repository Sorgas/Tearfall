package stonering.entity.plant;

import stonering.enums.plants.PlantType;
import stonering.util.geometry.Position;

/**
 * Represents single-tile plant.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class Plant extends AbstractPlant {
    private PlantBlock block;

    public Plant(Position position, PlantType type) {
        super(position, type);
    }

    public Plant(PlantType type) {
        super(type);
    }

    public PlantBlock getBlock() {
        return block;
    }

    public void setBlock(PlantBlock block) {
        this.block = block;
        block.plant = this;
        block.position = getPosition();
    }

    public Position getPosition() {
        return block.position;
    }

    public void setPosition(Position position) {
        super.setPosition(position);
        block.position = position;
    }
}
