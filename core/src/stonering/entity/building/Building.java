package stonering.entity.building;

import stonering.entity.Entity;
import stonering.enums.buildings.BuildingType;
import stonering.util.geometry.Position;

/**
 * Represents furniture, workbenches and other built game entities.
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class Building extends Entity {
    private int material;
    public BuildingType type;
    private BuildingBlock block; //TODO ad multiple blocks for buildings
    public boolean occupied = false;

    public Building(Position position, BuildingType type) {
        super(position);
        this.type = type;
        block = new BuildingBlock(this);
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public BuildingBlock getBlock() {
        return block;
    }

    public void setBlock(BuildingBlock block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return type.title;
    }
}
