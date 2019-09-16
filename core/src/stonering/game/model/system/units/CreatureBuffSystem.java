package stonering.game.model.system.units;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.AttributeAspect;
import stonering.entity.unit.aspects.BuffAspect;
import stonering.entity.unit.aspects.health.AttributeBuff;
import stonering.entity.unit.aspects.health.Buff;
import stonering.game.model.Turnable;
import stonering.util.global.Logger;

import java.util.Iterator;

/**
 * System for updating creatures {@link Buff}s.
 * All buff changes should be performed through this system.
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureBuffSystem extends Turnable {

    public void updateCreatureBuffs(Unit unit) {
        if (!unitHasBuffAspect(unit)) return;
        for (Iterator<Buff> iterator = unit.getAspect(BuffAspect.class).buffs.iterator(); iterator.hasNext(); ) {
            Buff buff = iterator.next();
            buff.decrease();
            if (!buff.expired()) continue; // skip active buffs
            iterator.remove();
            unapplyBuff(unit, buff);
        }
    }

    public void addBuff(Unit unit, Buff buff) {
        if (unitHasBuffAspect(unit)) {
            if (unit.getAspect(BuffAspect.class).buffs.add(buff)) {
                if (buff instanceof AttributeBuff) {
                    unit.getAspect(AttributeAspect.class).update(((AttributeBuff) buff).attribute, buff.delta);
                    return;
                }
                Logger.UNITS.logError("Buff " + buff + " has unknown type");
            } else {
                Logger.UNITS.logError("Buff " + buff + " already applied to unit");
            }
        }
    }

    public void unapplyBuff(Unit unit, Buff buff) {
        if (buff instanceof AttributeBuff) {
            unit.getAspect(AttributeAspect.class).update(((AttributeBuff) buff).attribute, -buff.delta);
            return;
        }
        Logger.UNITS.logError("Buff " + buff + " has unknown type");
    }

    private boolean unitHasBuffAspect(Unit unit) {
        if (unit.hasAspect(BuffAspect.class)) return true;
        Logger.UNITS.logError("Trying to apply buff to unit " + unit + " which has no buff aspect.");
        return false;
    }
}
