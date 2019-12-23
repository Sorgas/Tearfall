package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.health.HealthParameterState;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.HealthParameter;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.enums.unit.health.HealthParameterRange;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Logger;

/**
 * Updates health condition of a unit ({@link HealthAspect}).
 * Iterates health parameters of a creature, adding some constant value (delta) to them.
 * Health condition buffs depend on relative value of health parameter.
 * MVP: constant parameter ranges, increase/restore speeds, no treats.
 * <p>
 * FATIGUE - shows creature's tiredness. Performing actions, moving, and being awake increase fatigue.
 * Rest lowers fatigue. Maximum fatigue is based on endurance attribute, illness, worn items.
 * <p>
 * HUNGER - how hungry creature is. Hunger increased over time, by actions and movement. Eating lowers hunger.
 * Maximum hunger is based on endurance attribute and ilnesses.
 * TODO have 'fat' body parameter, decrease it on high hunger.
 * Creatures will look for food on 50%, eating priority increases with growing hunger.
 * TODO move constants to difficulty settings
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureHealthSystem extends EntitySystem<Unit> {
    public float moveParameterNoLoad = 0.05f;
    public float moveParameterFullLoad = 0.1f;

    public CreatureHealthSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
    }

    /**
     * Updates creatures health parameters to constant delta. Called by time.
     */
    @Override
    public void update(Unit unit) {
        HealthAspect health = unit.getAspect(HealthAspect.class);
        if (health == null) {
            Logger.UNITS.logError("Trying to update health of creature " + unit + " with no HealthAspect");
            return;
        }
        for (HealthParameterEnum parameter : health.parameters.keySet()) {
            changeParameter(unit, parameter, parameter.DEFAULT_DELTA);
        }
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
        HealthParameterState state = unit.getAspect(HealthAspect.class).parameters.get(parameterEnum);
        HealthParameter parameter = parameterEnum.PARAMETER;
        float oldValue = state.getRelativeValue();
        state.current += delta;
        if (state.current > state.max) {
            Logger.UNITS.logWarn("UNIT " + unit  + " DIED!"); // TODO
            return;
        }
        HealthParameterRange oldRange = parameter.getRange(oldValue);
        if (parameter.getRange(state.getRelativeValue()) != oldRange) resetParameter(unit, parameterEnum);
    }

    public void resetCreatureHealth(Unit unit) {
        for (HealthParameterEnum parameter : unit.getAspect(HealthAspect.class).parameters.keySet())
            resetParameter(unit, parameter);
    }

    /**
     * Recreate buffs related to given parameter.
     */
    private void resetParameter(Unit unit, HealthParameterEnum parameter) {
        HealthParameterState state = unit.getAspect(HealthAspect.class).parameters.get(parameter);
        HealthParameterRange range = parameter.PARAMETER.getRange(state.getRelativeValue());
        CreatureBuffSystem buffSystem = GameMvc.instance().model().get(UnitContainer.class).buffSystem;
        buffSystem.removeBuff(unit, parameter.TAG);
        buffSystem.addBuff(unit, range.produceBuff.get());
    }
}
