package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;

import java.util.ArrayList;
import java.util.List;

/**
 * Health parameter represents some aspect of creature's health.
 * Defines ranges where creature gets {@link Buff} and need priority.
 * Buffs same health parameter have same tag, and replace each other.
 * Produces buffs basing on {@link HealthParameterState} relative value.
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
    }

    public HealthParameterRange getRange(float relativeValue) {
        return RANGES.stream()
                .filter(range -> range.check(relativeValue))
                .findFirst()
                .orElse(null);
    }

    public HealthParameterRange getRange(HealthParameterState state) {
        return getRange(state.getRelativeValue());
    }
}
