package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.action.TaskPriorityEnum;

import java.util.function.Supplier;

/**
 * Represents single range of {@link HealthParameterState} value.
 * Stores task priority, can create {@link Buff}
 *
 * @author Alexander on 10.12.2019.
 */
public class HealthParameterRange {
    public final int min;
    public final int max;
    public final TaskPriorityEnum priority;
    public final Supplier<Buff> produceBuff;

    public HealthParameterRange(int min, int max, TaskPriorityEnum priority, Supplier<Buff> produceBuff) {
        this.min = min;
        this.max = max;
        this.priority = priority;
        this.produceBuff = produceBuff;
    }

    public boolean checkRange(float relativeValue) {
        return relativeValue < max && relativeValue >= min;
    }
}
