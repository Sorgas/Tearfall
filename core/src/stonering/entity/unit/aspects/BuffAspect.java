package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.health.Buff;

import java.util.HashSet;
import java.util.Set;

/**
 * Stores {@link Buff}s, applied to a creature. Should be updated only by CreatureBuffSystem.
 *
 * @author Alexander on 16.09.2019.
 */
public class BuffAspect extends Aspect {
    public final Set<Buff> buffs;

    public BuffAspect(Entity entity) {
        super(entity);
        buffs = new HashSet<>();
    }
}
