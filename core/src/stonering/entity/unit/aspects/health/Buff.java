package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.BuffAspect;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.game.model.system.units.CreatureBuffSystem;

import java.util.HashSet;
import java.util.Set;

/**
 * Can be applied to creature by {@link CreatureBuffSystem}.
 * Is part of {@link BuffAspect}.
 *
 * @author Alexander on 16.09.2019.
 */
public abstract class Buff implements Cloneable {
    public final int delta; // some creature property is changed by this value
    public final Set<String> tags; // shared between cloned instances
    public int ticksLeft = -1; // decreases every tick. buff is removed, when reaches zero. -1 for infinite buffs
    public final CreatureStatusIcon icon;

    public Buff(int delta, int x, int y) {
        this.delta = delta;
        tags = new HashSet<>();
        icon = new CreatureStatusIcon(x, y);
    }

    public Buff(Buff buff) {
        delta = buff.delta;
        tags = buff.tags;
        icon = buff.icon;
    }

    /**
     * Decreases buff timer
     */
    public void decrease() {
        if (ticksLeft > 0) ticksLeft--;
    }

    /**
     * Checks if buff should be removed.
     */
    public boolean expired() {
        return ticksLeft == 0;
    }

    public abstract boolean apply(Unit unit);

    public abstract boolean unapply(Unit unit);

    public abstract Buff copy();
}
