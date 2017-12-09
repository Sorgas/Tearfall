package stonering.objects.aspects;

import stonering.objects.local_actors.unit.Unit;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alexander on 10.10.2017.
 *
 * Holds current creature's task and it's steps. resolves behavior, if some step fails.
 */
public class PlanningAspect extends Aspect {
    private ArrayList<Position> route;

    public PlanningAspect(Unit unit) {
        this.unit = unit;
        this.name = "planning";
    }

    @Override
    public void init() {

    }

    public Position getStep() {
//        if(route.size() > 0) {
//        return route.get(0);}else {
//            return unit.getPosition();
//        }
        Random random = new Random();
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        Position current = unit.getPosition();
        Position newPosition = new Position(current.getX() + dx, current.getY() + dy, current.getZ());
        if (newPosition.getX() < 0 || newPosition.getX() > 191) newPosition.setX(current.getX());
        if (newPosition.getY() < 0 || newPosition.getY() > 191) newPosition.setY(current.getY());
        return newPosition;
    }

    public void poll() {
//        if (route.size() > 0) route.remove(0);
    }

}
