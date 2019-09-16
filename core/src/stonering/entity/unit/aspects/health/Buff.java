package stonering.entity.unit.aspects.health;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.game.model.system.units.CreatureBuffSystem;

/**
 * Can be applied to creature by {@link CreatureBuffSystem}
 *
 * @author Alexander on 16.09.2019.
 */
public abstract class Buff {
    public final int delta; // some creature property is changed by this value
    public final boolean displayed = true; // is buff icon displayed
    public final Drawable sprite = null; // buff icon
    public int ticksLeft; // decreases every tick. buff is removed, when reaches zero. -1 for infinite buffs

    public Buff(int delta) {
        this.delta = delta;
    }

    public void decrease() {
        if (ticksLeft > 0) ticksLeft--;
    }

    public boolean expired() {
        return ticksLeft == 0;
    }
}
