package stonering.game.model.system.units;

import stonering.entity.Entity;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthBuff;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.unit.health.HealthParameter;
import stonering.enums.unit.health.HealthParameterEnum;
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
 * <p>
 * HUNGER - how hungry creature is. Hunger increased over time, by actions and movement. Eating lowers hunger.
 * Maximum hunger is based on endurance attribute and ilnesses.
 * Having fatigue <20% gives debuff, every 10% above 50% give stacking debuff. TODO have 'fat' body parameter, decreace it on high hunger.
 * Creatures will look for food on 50%, eating priority increases with growing hunger.
 * TODO move constants to difficulty settings
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureHealthSystem {
    public float moveParameterNoLoad = 0.05f;
    public float moveParameterFullLoad = 0.1f;

    /**
     * Updates creatures health parameters to constant delta. Called by time.
     */
    public void updateCreatureHealth(Entity entity) {
        HealthAspect aspect = entity.getAspect(HealthAspect.class);
        if (aspect == null) {
            Logger.UNITS.logError("Trying to update health of creature " + entity + " with no HealthAspect");
            return;
        }
        changeParameter((Unit) entity, HealthParameterEnum.FATIGUE, 0.01f);
        changeParameter((Unit) entity, HealthParameterEnum.HUNGER, 0.01f);
    }

    /**
     * Called for every walked tile, adds delta to counter. Walking with high load increases delta.
     * TODO check other effects (illness, )
     */
    public void applyMoveChange(Unit unit) {
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        if (aspect == null) {
            Logger.UNITS.logError("Trying to add move fatigue to creature " + unit + " with no HealthAspect");
            return;
        }
        changeParameter(unit, HealthParameterEnum.FATIGUE, moveParameterNoLoad + moveParameterFullLoad * unit.getAspect(EquipmentAspect.class).getRelativeLoad());
    }

    private void changeParameter(Unit unit, HealthParameterEnum parameterEnum, float delta) {
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        HealthParameter parameter = parameterEnum.PARAMETER;
        HealthParameterState state = aspect.parameters.get(parameterEnum);
        float old = state.current;
        state.current += delta;
        if (state.current > state.max) {
            // TODO die
        }
        for (int i = 0; i < parameter.ranges.length; i++) { // check ranges
            if (MathUtil.onDifferentSides(old, state.current, parameter.ranges[i])) {
                CreatureBuffSystem buffSystem = GameMvc.instance().getModel().get(UnitContainer.class).buffSystem;
                buffSystem.unapplyByTag(unit, parameterEnum.TAG);
                if(parameter.buffs[i] != null) buffSystem.addBuff(unit, parameter.buffs[i].copy());
                return;
            }
        }
    }

    public void resetCreatureHealth(Unit unit) {
        resetParameter(unit, HealthParameterEnum.FATIGUE);
        resetParameter(unit, HealthParameterEnum.HUNGER);
    }

    private void resetParameter(Unit unit, HealthParameterEnum parameterEnum) {
        HealthAspect aspect = unit.getAspect(HealthAspect.class);
        HealthParameter parameter = parameterEnum.PARAMETER;
        HealthParameterState state = aspect.parameters.get(parameterEnum);
        for (int i = 0; i < parameter.ranges.length; i++) { // check ranges
            if (state.current < parameter.ranges[i]) {
                CreatureBuffSystem buffSystem = GameMvc.instance().getModel().get(UnitContainer.class).buffSystem;
                buffSystem.unapplyByTag(unit, parameterEnum.TAG);
                if(parameter.buffs[i] != null) buffSystem.addBuff(unit, parameter.buffs[i].copy());
                return;
            }
        }
    }
}
