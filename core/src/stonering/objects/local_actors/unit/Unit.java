package stonering.objects.local_actors.unit;

import stonering.game.core.model.LocalMap;
import stonering.objects.aspects.Aspect;
import stonering.global.utils.Position;
import stonering.objects.aspects.MovementAspect;
import stonering.objects.aspects.PlanningAspect;

import java.util.HashMap;

/**
 * Created by Alexander on 06.10.2017.
 *
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
        aspects.put(aspect.getName(),aspect);
    }

    public void turn() {
        localMap.setUnitBlock(position.getX(), position.getY(),position.getZ(), null);
        if(aspects.containsKey("planning")) {
            ((PlanningAspect) aspects.get("planning")).turn();
        }
//        if(aspects.containsKey("movement")) {
//            ((MovementAspect) aspects.get("movement")).move();
//        }
        localMap.setUnitBlock(position.getX(), position.getY(),position.getZ(), block);
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