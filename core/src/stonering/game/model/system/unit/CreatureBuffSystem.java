package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.health.Buff;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.logging.Logger;

import java.util.stream.Collectors;

import com.sun.istack.Nullable;

/**
 * System for updating creatures {@link Buff}s.
 * Buff adding and removing be performed with this system.
 * Creature cannot have multiple buffs with same tag.
 * No buffs should have same icons.
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureBuffSystem extends EntitySystem<Unit> {

    public CreatureBuffSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
    }

    /**
     * Updates counters on creature's buffs, removing expired ones.
     */
    @Override
    public void update(Unit unit) {
        unit.optional(BodyAspect.class)
                .ifPresent(aspect ->
                        aspect.buffs.values().stream()
                                .peek(buff -> buff.decrease(unit)) // roll time for buffs
                                .filter(Buff::expired)
                                .map(buff -> buff.tag)
                                .collect(Collectors.toList())
                                .forEach(tag -> removeBuff(unit, tag))); // remove expired buffs
    }

    public void addBuff(Unit unit, @Nullable Buff buff) {
        Logger.UNITS.logDebug("Adding buff " + buff + " to " + unit);
        removeBuff(unit, buff.tag);
        unit.optional(BodyAspect.class)
                .ifPresent(aspect -> {
                    applyBuff(unit, buff);
                    unit.get(BodyAspect.class).buffs.put(buff.tag, buff);
                });
    }

    public void removeBuff(Unit unit, String tag) {
        Logger.UNITS.logDebug("Removing buff with tag " + tag + " to " + unit);
        unit.optional(BodyAspect.class)
                .map(aspect -> aspect.buffs.get(tag))
                .ifPresent(buff -> {
                    unapplyBuff(unit, buff);
                    unit.get(BodyAspect.class).buffs.remove(tag);
                });
    }

    private boolean applyBuff(Unit unit, Buff buff) {
        if (!buff.apply(unit)) return Logger.UNITS.logError("Failed to apply " + buff + " to " + unit, false);
        if (buff.icon != null) unit.get(RenderAspect.class).icons.add(buff.icon);
        return true;
    }

    private boolean unapplyBuff(Unit unit, Buff buff) {
        if (!buff.unapply(unit)) return Logger.UNITS.logError("Failed to unapply " + buff + " to " + unit, false);
        if (buff.icon != null) unit.get(RenderAspect.class).icons.remove(buff.icon);
        return true;
    }
}
