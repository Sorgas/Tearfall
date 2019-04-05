package stonering.entity.local.building;

import stonering.util.geometry.Position;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.unit.Unit;

import java.util.HashMap;

/**
 * Represents furniture, workbenches and other built game entities.
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class Building extends AspectHolder { // TODO split to aspects
    private Unit owner;
    private int material;
    private BuildingType type;
    private BuildingBlock block;

    public Building(Position position, BuildingType type) {
        super(position);
        this.type = type;
        aspects = new HashMap<>();
        block = new BuildingBlock(this);
    }

    public Unit getOwner() {
        return owner;
    }

    public void setOwner(Unit owner) {
        this.owner = owner;
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

    public BuildingType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getTitle();
    }
}
