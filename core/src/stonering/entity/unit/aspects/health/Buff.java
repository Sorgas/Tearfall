package stonering.entity.unit.aspects.health;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.BuffAspect;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.game.model.system.unit.CreatureBuffSystem;

/**
 * Can be applied to creature by {@link CreatureBuffSystem}.
 * Is part of {@link BuffAspect}. Has optional item.
 * Has counter of ticks to expiration. Expired buffs are unapplied from unit by {@link CreatureBuffSystem}.
 * If counter is set to negative, buff is infinite and should be removed externally.
 *
 * @author Alexander on 16.09.2019.
 */
public abstract class Buff implements Cloneable {
    public final String tag;
    public final int delta; // some creature property is changed by this value

    public CreatureStatusIcon icon; // optional

    public int ticksLeft = -1; // decreases every tick. buff is removed, when reaches zero. -1 for infinite buffs

    /**
     * Buff with no icon.
     */
    public Buff(String tag, int delta) {
        this.tag = tag;
        this.delta = delta;
    }

    public Buff(String tag, int delta, int x, int y) {
        this(tag, delta);
        icon = new CreatureStatusIcon(x, y);
    }

    /**
     * Decreases buff timer. returns true, if buff expired.
     */
    public boolean decrease(Unit unit) {
        return expired() || ((ticksLeft > 0) && (ticksLeft-- == 0));
    }

    /**
     * Checks if buff should be removed.
     */
    public boolean expired() {
        return ticksLeft == 0;
    }

    public abstract boolean apply(Unit unit);

    public abstract boolean unapply(Unit unit);
}
