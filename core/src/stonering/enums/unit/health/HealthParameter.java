package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.need.NeedState;

import java.util.ArrayList;
import java.util.List;

/**
 * Health parameter represents some aspect(hunger, thirst) of creature's health.
 * Creature health aspect is represented as float number [0, max], where max is specific to the creature (influenced by attributes or traits).
 * Parameter defines ranges of relative value where creature gets different {@link Buff}s and need priorities.
 * Buffs of the same health parameter have the same tag, and replace each other.
 * Produces buffs basing on {@link NeedState} relative value.
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

    public HealthParameterRange getRange(NeedState state) {
        return getRange(state.getRelativeValue());
    }
}
