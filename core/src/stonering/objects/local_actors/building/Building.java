package stonering.objects.local_actors.building;

import stonering.global.utils.Position;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;
import stonering.objects.local_actors.unit.Unit;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov on 07.12.2017.
 *
 * Represents furniture, workbenches and other builded game objects
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
