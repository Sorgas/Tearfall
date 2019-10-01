package stonering.game.model.system.units;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.game.GameMvc;
import stonering.util.global.Logger;
import stonering.util.math.MathUtil;

/**
 * Updates health condition of a unit ({@link HealthAspect}).
 * <p>
 * FATIGUE - shows creature's tiredness. Performing actions, moving, and being awake increase fatigue.
 * Rest lowers fatigue. Maximum fatigue is based on endurance attribute, ilnesses, worn items.
 * Having fatigue <20% gives buff, every 10% above 50% give stacking debuffs.
 * Creatures will seek rest on 50%, rest priority increases with growing fatigue.
 *
 * HUNGER - how hungry creature is. Hunger increased over time, by actions and movement. Eating lowers hunger.
 * Maximum hunger is based on endurance attribute and ilnesses.
 * Having fatigue <20% gives debuff, every 10% above 50% give stacking debuff. TODO have 'fat' body parameter, decreace it on high hunger.
 * Creatures will look for food on 50%, eating priority increases with growing hunger.
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureHealthSystem {
    private int[] fatigueRanges = {20, 50, 60, 70, 80, 90};



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
        changeFatigue(unit, aspect.moveFatigueNoLoad + aspect.moveFatigueFullLoad * unit.getAspect(EquipmentAspect.class).getRelativeLoad());
    }

    private void changeFatigue(Unit unit, float delta) {
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        float relativeOldFatigue = aspect.fatigue;
        aspect.fatigue += delta;
        if (aspect.fatigue > aspect.maxFatigue) {
            // die
        }
        float relativeFatigue = aspect.maxFatigue / aspect.fatigue;
        if (MathUtil.inDifferentRanges(relativeOldFatigue, relativeFatigue, fatigueRanges)) {
            CreatureBuffSystem buffSystem = GameMvc.instance().getModel().get(UnitContainer.class).buffSystem;
            buffSystem.addBuff(unit, getFatigueBuff(relativeFatigue));
        }
    }

    /**
     * Creates {@link Buff} for each fatigue range.
     */
    private Buff getFatigueBuff(float fatigue) {
        if (fatigue < 20) return new HealthBuff(10, "performance"); // performance in increased after sleep
        if (fatigue < 50) return null; // no buff normally
        if (fatigue < 60) return new HealthBuff(-10, "performance"); // performance decreased
        if (fatigue < 70) return new HealthBuff(-15, "performance");
        if (fatigue < 80) return new HealthBuff(-20, "performance");
        if (fatigue < 90) return new HealthBuff(-25, "performance");
        return new HealthBuff(-30, "performance");
    }
}
