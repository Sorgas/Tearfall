package stonering.objects.local_actors.unit;

import stonering.game.core.model.LocalMap;
import stonering.objects.local_actors.unit.aspects.Aspect;
import stonering.global.utils.Position;
import stonering.objects.local_actors.unit.aspects.PlanningAspect;

import java.util.HashMap;

/**
 * Created by Alexander on 06.10.2017.
 * <p>
 * Represents living creatures
 */
public class Unit {
    private Position position;
    private HashMap<String, Aspect> aspects;
    private LocalMap localMap;
    private UnitBlock block;

    public Unit(Position position) {
        this.position = position;
        block = new UnitBlock(this);
        aspects = new HashMap<>();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public HashMap<String, Aspect> getAspects() {
        return aspects;
    }

    public void addAspect(Aspect aspect) {
        aspects.put(aspect.getName(), aspect);
    }

    public void turn() {
        localMap.freeUnitBlock(position);
        aspects.values().forEach((aspect) -> aspect.turn());
        localMap.setUnitBlock(position, block);
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public UnitBlock getBlock() {
        return block;
    }

    public void setBlock(UnitBlock block) {
        this.block = block;
    }
}