package stonering.entity.unit.aspects.health;


import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.util.math.MathUtil;

/**
 * Stores health condition of a unit.
 * <p>
 * Fatigue - shows creature's tiredness. Performing actions, moving, and being awake increase fatigue.
 * Rest lowers fatigue. Maximum fatigue is based on endurance attribute, ilnesses, worn items.
 * Having fatigue <20% gives buff, every 10% above 50% gives stacking debuffs.
 * Creatures will seek rest on 50%, rest priority increases with growing fatigue.
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {
    public float fatigue;
    public float maxFatigue;
    public float moveFatigueNoLoad;
    public float moveFatigueFullLoad;

    public HealthAspect(Entity entity) {
        super(entity);
    }

    /**
     * Called for every walked tile, adds fatigue to counter. Walking with high load exhausts faster.
     * TODO check other effects (illness, )
     */
    public void applyMoveFatigue() {
        float oldFatigue = fatigue;
        fatigue += moveFatigueNoLoad + moveFatigueFullLoad * entity.getAspect(EquipmentAspect.class).getRelativeLoad();
        updateFatigueDebuffs(oldFatigue);
    }

    private void updateFatigueDebuffs(float oldFatigue) {
        if (MathUtil.inSamePercentRange(oldFatigue, fatigue, maxFatigue, 0, 20)) { // rested buff

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
