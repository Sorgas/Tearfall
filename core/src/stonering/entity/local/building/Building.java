package stonering.entity.local.building;

import stonering.global.utils.Position;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.unit.Unit;

import java.util.HashMap;

/**
 * Represents furniture, workbenches and other built game entities.
 *
 * @author Alexander Kuzyakov on 07.12.2017.
 */
public class Building extends AspectHolder {
    private String name;
    private Unit owner;
    private int material;
    private BuildingBlock block;

    public Building(Position position) {
        super(position);
        aspects = new HashMap<>();
        block = new BuildingBlock(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getOwner() {
        return owner;
    }

    public void setOwner(Unit owner) {
        this.owner = owner;
    }

    public void setAspects(HashMap<String, Aspect> aspects) {
        this.aspects = aspects;
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
}
