package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.NeedEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;

/**
 * Updates health condition of a unit ({@link HealthAspect}).
 * Iterates health parameters of a creature, adding some constant (delta) to them.
 * Updates buffs if needed.
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
    private CreatureBuffSystem buffSystem;
    
    public CreatureHealthSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
    }
    
    @Override
    public void update(Unit unit) {

    }

    public void changeParameter(Unit unit, NeedEnum parameter, float delta) {
        if(unit.get(NeedAspect.class).needs.get(parameter).changeValue(delta)) resetParameter(unit, parameter);
    }

    public void resetCreatureHealth(Unit unit) {
        for (NeedEnum parameter : unit.get(NeedAspect.class).needs.keySet())
            resetParameter(unit, parameter);
    }

    private void resetParameter(Unit unit, NeedEnum parameter) {
        buffSystem().removeBuff(unit, parameter.TAG); // remove previous buff
        unit.getOptional(HealthAspect.class)
                .map(aspect -> aspect.needStates.get(parameter))
                .map(parameter.PARAMETER::getRange)
                .map(range -> range.produceBuff.get())
                .ifPresent(buff -> buffSystem().addBuff(unit, buff)); // add new buff if any
    }
    
    private CreatureBuffSystem buffSystem() {
        return buffSystem == null ? buffSystem = GameMvc.model().get(UnitContainer.class).buffSystem : buffSystem; 
    }
}
