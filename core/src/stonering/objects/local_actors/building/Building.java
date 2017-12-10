package stonering.objects.local_actors.building;

import stonering.global.utils.Position;
import stonering.objects.aspects.Aspect;
import stonering.objects.local_actors.unit.Unit;

import java.util.HashMap;

/**
 * Created by Alexander on 07.12.2017.
 *
 * Represents furniture, workbenches and other builded game objects
 */
public class Building {
    private Position position;
    private String name;
    private Unit owner;
    private HashMap<String, Aspect> aspects;
    private int material;
    private BuildingBlock block;

    public Building() {
        aspects = new HashMap<>();
        block = new BuildingBlock(this);
    }

    public void addAspect(Aspect aspect) {
        aspects.put(aspect.getName(), aspect);
    }

    public HashMap<String, Aspect> getAspects() {
        return aspects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
