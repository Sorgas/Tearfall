package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;

import java.util.ArrayList;
import java.util.List;

/**
 * Produces {@link Buff}s basing on {@link HealthParameterState} relative value.
 * Stores priorities for needs tasks.
 *
 * @author Alexander on 06.10.2019.
 */
public abstract class HealthParameter {
    public final List<HealthParameterRange> RANGES;
    public final String TAG; // used by buffs

    public HealthParameter(String tag) {
        this.RANGES = new ArrayList<>();
        this.TAG = tag;
        fillRanges();
    }

    protected abstract void fillRanges();

    public HealthParameterRange getRange(float relativeValue) {
        return RANGES.stream()
                .filter(range -> range.checkRange(relativeValue))
                .findFirst()
                .orElse(null);
    }
}
