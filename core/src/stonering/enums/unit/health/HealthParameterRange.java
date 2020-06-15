package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.util.math.ValueRange;

import java.util.function.Supplier;

/**
 * Represents single range of {@link HealthParameterState} value.
 * Stores task priority, can create {@link Buff}
 *
 * @author Alexander on 10.12.2019.
 */
public class HealthParameterRange extends ValueRange {
    public final TaskPriorityEnum priority;
    public final Supplier<Buff> produceBuff;

    public HealthParameterRange(Float min, Float max, TaskPriorityEnum priority, Supplier<Buff> produceBuff) {
        super(min, max);
        this.priority = priority;
        this.produceBuff = produceBuff;
    }
}
