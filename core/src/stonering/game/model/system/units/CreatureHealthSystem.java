package stonering.game.model.system.units;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.game.GameMvc;
import stonering.util.global.Logger;
import stonering.util.math.MathUtil;

/**
 * Updates health condition of a unit ({@link HealthAspect}).
 * <p>
 * Fatigue - shows creature's tiredness. Performing actions, moving, and being awake increase fatigue.
 * Rest lowers fatigue. Maximum fatigue is based on endurance attribute, ilnesses, worn items.
 * Having fatigue <20% gives buff, every 10% above 50% gives stacking debuffs.
 * Creatures will seek rest on 50%, rest priority increases with growing fatigue.
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureHealthSystem {

    /**
     * Called for every walked tile, adds fatigue to counter. Walking with high load exhausts faster.
     * TODO check other effects (illness, )
     */
    public void applyMoveFatigue(Unit unit) {
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        if (aspect == null) {
            Logger.UNITS.logError("Trying to add move fatigue to creature " + unit + " with no HealthAspect");
            return;
        }
        changeFatigue(aspect, aspect.moveFatigueNoLoad + aspect.moveFatigueFullLoad * unit.getAspect(EquipmentAspect.class).getRelativeLoad());
    }

    private void changeFatigue(HealthAspect aspect, float delta) {
        float oldFatigue = aspect.fatigue;
        aspect.fatigue += delta;

    }

    private Buff getFatigueBuff(float fatigue, float newFatigue, float maxFatigue) {
        if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 20)) {
        } else if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 50)) {
        } else if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 60)) {
        } else if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 70)) {
        } else if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 80)) {
        } else if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 90)) {
        } else if (MathUtil.rangeChanged(fatigue, newFatigue, maxFatigue, 100)) {
        }
    }

    /**
     * Applies new and removes old buffs, when fatigue passes relative threshold.
     */
    private Buff updateFatigueBuffs(float value, float maxValue, float oldFatigue) {
        if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 0, 20)) { // rested buff
            GameMvc.instance().getModel().get(UnitContainer.class).buffSystem.addBuff();
        } else if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 20, 50)) { // no buffs

        } else if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 50, 60)) {
        } else if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 60, 70)) {
        } else if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 70, 80)) {
        } else if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 80, 90)) {
        } else if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 90, 100)) {
        } else if (fatigue > maxFatigue) { // die

        }
    }
}
