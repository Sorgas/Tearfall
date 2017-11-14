package stonering.objects.local_actors.units;

import stonering.objects.aspects.Aspect;
import stonering.global.utils.Position;

import java.util.HashMap;

/**
 * Created by Alexander on 06.10.2017.
 */
public class Unit {
    private Position position;
    private HashMap<String, Aspect> aspects;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public HashMap<String, Aspect> getAspects() {
        return aspects;
    }
}
