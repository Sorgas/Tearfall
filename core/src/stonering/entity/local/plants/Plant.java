package stonering.entity.local.plants;

import stonering.util.geometry.Position;

/**
 * Represents single-tile plant.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class Plant extends AbstractPlant {
    private PlantBlock block;

    public Plant(Position position, int age) {
        super(position);
        this.age = age;
    }

    @Override
    public boolean isHarvestable() {
        return getCurrentStage().getHarvestProducts() != null;
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
