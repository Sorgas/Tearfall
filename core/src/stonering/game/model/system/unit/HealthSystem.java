package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.enums.unit.health.CreatureAttributeEnum;
import stonering.enums.unit.health.GameplayStatEnum;
import stonering.enums.unit.health.HealthEffect;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.enums.time.TimeUnitEnum;
import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.util.logging.Logger;

/**
 * Updates health condition of a unit ({@link HealthAspect}).
 * Applies {@link HealthEffect} to unit's {@link HealthAspect}.
 * {@link GameplayStatEnum} depends on {@link HealthFunctionEnum} and {@link CreatureAttributeEnum} values, and changed after change of these values takes effect.
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
        unit.optional(HealthAspect.class).ifPresentOrElse(health -> {
                    effect.attributeEffects.forEach(health::change); // apply effect
                    effect.functionEffects.forEach(health::change); // apply effect
                    GameplayStatEnum.collectProperties(effect).forEach(health::update); // update dependent properties
                    effect.statEffects.forEach(health::change); // apply effect
                    health.effects.put(effect.name, effect); // save effect as applied
                },
                () -> Logger.UNITS.logError("Cannot apply health effect " + effect.name + "Unit " + unit + " has no health aspect"));
    }

    public void unapplyEffect(HealthEffect effect, Unit unit) {
        unit.optional(HealthAspect.class).ifPresentOrElse(health -> {
                    if (!health.effects.containsKey(effect.name)) {
                        Logger.UNITS.logError("Attempt to remove unpresent effect " + effect.name);
                        return;
                    }
                    effect.attributeEffects.forEach((attribute, delta) -> health.change(attribute, -delta));
                    effect.functionEffects.forEach((function, delta) -> health.change(function, -delta));
                    GameplayStatEnum.collectProperties(effect).forEach(health::update);
                    effect.statEffects.forEach((stat, delta) -> health.change(stat, -delta));
                },
                () -> Logger.UNITS.logError("Cannot unapply health effect " + effect.name + "Unit " + unit + " has no health aspect"));
    }

    public void kill(Unit unit) {
        GameMvc.model().get(UnitContainer.class).objects.remove(unit);
        GameMvc.model().get(UnitContainer.class).unitsMap.get(unit.position).remove(unit);
    }
}
