package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.action.TaskPrioritiesEnum;
import stonering.util.global.Logger;
import stonering.util.math.MathUtil;

import java.util.Arrays;

/**
 * Produces {@link Buff}s basing on {@link HealthParameterState} value.
 * Stores priorities for needs tasks.
 *
 * @author Alexander on 06.10.2019.
 */
public abstract class HealthParameter {
    public final int[] ranges;
    public final String tag; // used by buffs
    public final TaskPrioritiesEnum[] priorities;

    public HealthParameter(int[] ranges, String tag) {
        this.ranges = ranges;
        this.tag = tag;
        priorities = new TaskPrioritiesEnum[ranges.length];
        fillPriorities();
    }

    public abstract Buff getBuffForRange(int rangeIndex);

    protected abstract void fillPriorities();

    public boolean isRangeChanged(float newValue, float oldValue) {
        return Arrays.stream(ranges).anyMatch(range -> MathUtil.onDifferentSides(oldValue, newValue, range));
    }

    public int getRangeIndex(float value) {
        for (int i = 0; i < ranges.length; i++) {
            if (value < ranges[i]) return i;
        }
        Logger.UNITS.logError("health parameter " + this + " value is out of range");
        return 0;
    }
}
