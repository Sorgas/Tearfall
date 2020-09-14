package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.enums.unit.health.HealthEffect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.HealthParameterMapping;
import stonering.game.model.system.EntitySystem;
import stonering.util.logging.Logger;

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
    private final HealthParameterMapping mapping = new HealthParameterMapping();

    public HealthSystem() {
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Unit unit) {
    }

    public void applyEffect(HealthEffect effect, Unit unit) {
        HealthAspect health = unit.get(HealthAspect.class);
        effect.attributeEffects.forEach(health::change); // apply effect
        effect.functionEffects.forEach(health::change); // apply effect
        mapping.collectProperties(effect).forEach(health::update); // update dependent properties
        effect.statEffects.forEach(health::change); // apply effect
        health.effects.put(effect.name, effect); // save effect as applied
    }

    public void unapplyEffect(HealthEffect effect, Unit unit) {
        HealthAspect health = unit.get(HealthAspect.class);
        if(!health.effects.containsKey(effect.name)) {
            Logger.UNITS.logError("Attempt to remove unpresent effect " + effect.name);
            return;
        }
        effect.attributeEffects.forEach(health::change);
        effect.functionEffects.forEach(health::change);
        mapping.collectProperties(effect).forEach(health::update);
        effect.statEffects.forEach(health::change);
    }
}
