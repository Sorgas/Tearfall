package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.RenderAspect;
import stonering.enums.OrientationEnum;

import static stonering.enums.OrientationEnum.*;

/**
 * Holds direction creature is facing to. Is updated by {@link stonering.game.model.system.unit.CreatureMovementSystem}.
 * Updates {@link RenderAspect}
 *
 * @author Alexander on 05.03.2020.
 */
public class OrientationAspect extends Aspect {
    public OrientationEnum current;

    public OrientationAspect(Entity entity, OrientationEnum current) {
        super(entity);
        this.current = current;
    }

    public void rotate(boolean clockwise) {
        switch (current) {
            case N:
                current = clockwise ? E : W;
                break;
            case E:
                current = clockwise ? S : N;
                break;
            case S:
                current = clockwise ? W : E;
                break;
            case W:
                current = clockwise ? N : S;
                break;
        }

    }
}
