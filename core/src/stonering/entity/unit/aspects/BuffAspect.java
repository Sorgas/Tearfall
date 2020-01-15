package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.health.Buff;

import java.util.HashMap;

import java.util.Map;

/**
 * Stores {@link Buff}s, applied to a creature. Should be updated only by CreatureBuffSystem.
 * Buffs are stored in map by their tags, so only one buff with specific tag can be applied at a time.
 *
 * @author Alexander on 16.09.2019.
 */
public class BuffAspect extends Aspect {
    public final Map<String, Buff> buffs;

    public BuffAspect(Entity entity) {
        super(entity);
        buffs = new HashMap<>();
    }
}
