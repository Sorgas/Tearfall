package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.BuffAspect;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.health.Buff;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.istack.Nullable;

/**
 * System for updating creatures {@link Buff}s.
 * Buff adding and removing be performed with this system.
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
        unit.getOptional(BuffAspect.class)
                .ifPresent(aspect ->
                        aspect.buffs.values().stream()
                                .peek(buff -> buff.decrease(unit)) // roll time for buffs
                                .filter(Buff::expired)
                                .map(buff -> buff.tag)
                                .collect(Collectors.toList())
                                .forEach(aspect.buffs::remove)); // remove expired buffs
    }

    /**
     * Adds new buff to unit, applies its effect, adds buff's icon.
     */
    public boolean addBuff(Unit unit, @Nullable Buff buff) {
        Logger.UNITS.logDebug("Adding buff " + buff + " to creature " + unit);
        if (buff == null) return true;

        if (unit.has(BuffAspect.class)) {
            if (!applyBuff(unit, buff)) return false;
            unit.get(BuffAspect.class).buffs.put(buff.tag, buff);
            return true;
        }
        return false;
    }

    public boolean removeBuff(Unit unit, String tag) {
        Logger.UNITS.logDebug("Removing buff with tag " + tag + " to creature " + unit);
        if (!unit.has(BuffAspect.class)) return Logger.UNITS.logError("Creature " + unit + " has no BuffAspect", false);
        Buff buff = unit.get(BuffAspect.class).buffs.get(tag);
        if (buff != null) {
            if (!unapplyBuff(unit, buff)) return false;
            unit.get(BuffAspect.class).buffs.remove(tag);
            return true;
        }
        return false;
    }

    private boolean applyBuff(Unit unit, Buff buff) {
        if (!buff.apply(unit))
            return Logger.UNITS.logError("Failed to apply buff " + buff + " to creature " + unit, false);
        if (buff.icon != null) unit.get(RenderAspect.class).icons.add(buff.icon);
        return true;
    }

    private boolean unapplyBuff(Unit unit, Buff buff) {
        if (!buff.unapply(unit))
            return Logger.UNITS.logError("Failed to unapply buff " + buff + " to creature " + unit, false);
        if (buff.icon != null) unit.get(RenderAspect.class).icons.remove(buff.icon);
        return true;
    }
}
