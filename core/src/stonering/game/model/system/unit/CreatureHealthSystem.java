package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.DiseaseState;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.NeedEnum;
import stonering.enums.unit.health.disease.DiseaseMap;
import stonering.enums.unit.health.disease.DiseaseType;
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
 * Creatures will look for food on 50%, eating priority increases with growing hunger.
 *
 * @author Alexander on 16.09.2019.
 */
public class CreatureHealthSystem extends EntitySystem<Unit> {

    public CreatureHealthSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Unit unit) {
        BodyAspect body = unit.get(BodyAspect.class);
        HealthAspect health = unit.get(HealthAspect.class);
        for (DiseaseState state : body.diseases.values()) {
            String prevStage = state.stageName;
            if (!state.change(0.01f)) continue; // stage dod not changed
            DiseaseType type = DiseaseMap.get(state.name);
            type.stages.get(prevStage).effectsMap
                    .forEach((function, value) -> health.functions.get(function).changeCurrent(-value)); // unapply previous
            type.stages.get(state.stageName).effectsMap
                    .forEach((function, value) -> health.functions.get(function).changeCurrent(value)); // apply new
        }
    }


}
