package stonering.objects.local_actors.unit;

import stonering.game.core.model.LocalMap;
import stonering.objects.aspects.Aspect;
import stonering.global.utils.Position;
import stonering.objects.aspects.MovementAspect;

import java.util.HashMap;

/**
 * Created by Alexander on 06.10.2017.
 */
public class Unit {
    private Position position;
    private HashMap<String, Aspect> aspects;
    private LocalMap localMap;

    public Unit(Position position) {
        this.position = position;
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
        if(aspects.containsKey("movement")) {
            ((MovementAspect) aspects.get("movement")).move();
        }
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }
}