package stonering.entity.local.plants;

import stonering.enums.plants.PlantType;
import stonering.util.geometry.Position;

/**
 * Represents single-tile plant.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class Plant extends AbstractPlant {
    private PlantBlock block;

    public Plant(Position position, PlantType type, int age) {
        super(position, type, age);
    }

    @Override
    public boolean isHarvestable() {
        return getCurrentStage().harvestProduct != null;
    }

    public PlantBlock getBlock() {
        return block;
    }

    public void setBlock(PlantBlock block) {
        this.block = block;
        block.setPlant(this);
        block.setPosition(getPosition());
    }

    public Position getPosition() {
        return block.getPosition();
    }

    public void setPosition(Position position) {
        super.setPosition(position);
        block.setPosition(position);
    }
}
