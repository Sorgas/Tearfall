package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;

import java.util.ArrayList;
import java.util.List;

/**
 * Produces {@link Buff}s basing on {@link HealthParameterState} value.
 * Stores priorities for needs tasks.
 *
 * @author Alexander on 06.10.2019.
 */
public abstract class HealthParameter {
    public final List<HealthParameterRange> ranges;
    public final String tag; // used by buffs

    public HealthParameter(String tag) {
        this.ranges = new ArrayList<>();
        this.tag = tag;
        fillRanges();
    }

    protected abstract void fillRanges();

    public HealthParameterRange getRange(float relativeValue) {
        return ranges.stream().filter(range -> range.checkRange(relativeValue)).findFirst().orElse(null);
    }
}
