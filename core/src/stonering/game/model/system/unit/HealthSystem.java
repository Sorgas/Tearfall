package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.DiseaseState;
import stonering.entity.unit.aspects.body.HealthEffect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.disease.DiseaseMap;
import stonering.enums.unit.health.disease.DiseaseType;
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
public class HealthSystem extends EntitySystem<Unit> {

    public HealthSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Unit unit) {
    }

    public void applyEffect(HealthEffect effect, Unit unit) {

    }

    public void unapplyEffect(HealthEffect effect, Unit unit) {

    }
}
