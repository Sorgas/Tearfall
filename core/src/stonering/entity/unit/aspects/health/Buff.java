package stonering.entity.unit.aspects.health;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.game.model.system.units.CreatureBuffSystem;

import java.util.HashSet;
import java.util.Set;

/**
 * Can be applied to creature by {@link CreatureBuffSystem}
 *
 * @author Alexander on 16.09.2019.
 */
public abstract class Buff {
    public final int delta; // some creature property is changed by this value
    public final int[] atlasXY;
    public final Drawable sprite = null; // buff icon
    public int ticksLeft; // decreases every tick. buff is removed, when reaches zero. -1 for infinite buffs
    public final Set<String> tags;

    public Buff(int delta, int x, int y) {
        this.delta = delta;
        tags = new HashSet<>();
        atlasXY = new int[]{x, y};
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

    public CreatureStatusIcon getIcon() {
        return new CreatureStatusIcon(atlasXY);
    }

    public abstract boolean apply(Unit unit);

    public abstract boolean unapply(Unit unit);
}
