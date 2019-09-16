package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.health.Buff;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Buff> getBuffsByTag(String tag) {
        return buffs.stream().filter(buff -> buff.tags.contains(tag)).collect(Collectors.toSet());
    }
}
