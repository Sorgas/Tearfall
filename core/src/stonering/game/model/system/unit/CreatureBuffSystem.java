package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.BuffAspect;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.health.Buff;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.logging.Logger;

import java.util.Iterator;

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
        if (!unit.has(BuffAspect.class)) return;
        for (Iterator<Buff> iterator = unit.get(BuffAspect.class).buffs.values().iterator(); iterator.hasNext(); ) {
            Buff buff = iterator.next();
            if (!buff.decrease(unit)) continue; // skip active buffs
            unapplyBuff(unit, buff);
            iterator.remove();
        }
    }

    /**
     * Adds new buff to unit, applies its effect, adds buff's icon.
     */
    public boolean addBuff(Unit unit, Buff buff) {
        Logger.UNITS.logDebug("Adding buff " + buff + " to creature " + unit);
        if (buff == null) return true;
        if (!unit.has(BuffAspect.class)) return failWithLog("Creature " + unit + " has no BuffAspect");
        if (!applyBuff(unit, buff)) return false;
        unit.get(BuffAspect.class).buffs.put(buff.tag, buff);
        return true;
    }

    public boolean removeBuff(Unit unit, String tag) {
        Logger.UNITS.logDebug("Removing buff with tag " + tag + " to creature " + unit);
        if (!unit.has(BuffAspect.class)) return failWithLog("Creature " + unit + " has no BuffAspect");
        Buff buff = unit.get(BuffAspect.class).buffs.get(tag);
        if (buff == null) return failWithLog("Buff with tag " + tag + " not found on creature " + unit);
        if (!unapplyBuff(unit, buff)) return false;
        unit.get(BuffAspect.class).buffs.remove(tag);
        return true;
    }

    private boolean applyBuff(Unit unit, Buff buff) {
        if (!buff.apply(unit)) return failWithLog("Failed to apply buff " + buff + " to creature " + unit);
        if (buff.icon != null) unit.get(RenderAspect.class).icons.add(buff.icon);
        return true;
    }

    private boolean unapplyBuff(Unit unit, Buff buff) {
        if (!buff.unapply(unit)) return failWithLog("Failed to unapply buff " + buff + " to creature " + unit);
        if (buff.icon != null) unit.get(RenderAspect.class).icons.remove(buff.icon);
        return true;
    }

    private boolean failWithLog(String message) {
        Logger.UNITS.logError(message);
        return false;
    }
}
